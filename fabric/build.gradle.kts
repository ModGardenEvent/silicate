import net.modgarden.silicate.gradle.Properties
import net.modgarden.silicate.gradle.Versions
import org.gradle.jvm.tasks.Jar
import org.gradlex.javamodule.moduleinfo.ModuleInfo
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.ClassNode
import java.lang.invoke.MethodHandles
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.util.zip.ZipFile

plugins {
	id("conventions.loader")
	id("fabric-loom")
	id("me.modmuss50.mod-publish-plugin")
}

repositories {
	maven {
		name = "TerraformersMC"
		url = uri("https://maven.terraformersmc.com/")
	}
	maven {
		name = "NeoForge"
		url = uri("https://maven.neoforged.net/releases/")
	}
}

sourceSets {
	getByName("main") {
		runtimeClasspath += project(":common").sourceSets["test"].output
	}
	getByName("test") {
		runtimeClasspath += project(":common").sourceSets["test"].output
	}
}

dependencies {
	runtimeOnly(project(":common"))
	minecraft("com.mojang:minecraft:${Versions.MINECRAFT}")
	mappings(loom.layered {
		officialMojangMappings()
		parchment("org.parchmentmc.data:parchment-${Versions.PARCHMENT_MINECRAFT}:${Versions.PARCHMENT}")
	})

	modImplementation("net.fabricmc:fabric-loader:${Versions.FABRIC_LOADER}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${Versions.FABRIC_API}")
	modLocalRuntime("com.terraformersmc:modmenu:${Versions.MOD_MENU}")
	compileOnly("io.github.llamalad7:mixinextras-common:${Versions.MIXIN_EXTRAS}")
	annotationProcessor("io.github.llamalad7:mixinextras-common:${Versions.MIXIN_EXTRAS}")

	implementation("dev.lukebemish:codecextras:${Versions.CODEC_EXTRAS}")
	include("dev.lukebemish:codecextras:${Versions.CODEC_EXTRAS}")
	compileOnly("cpw.mods:modlauncher:11.0.5")
}

loom {
	val aw = file("src/main/resources/${Properties.MOD_ID}.accesswidener")
	if (aw.exists())
		accessWidenerPath.set(aw)
	mixin {
		defaultRefmapName.set("${Properties.MOD_ID}.refmap.json")
	}
	interfaceInjection { // off for now to get modules working
		this.getIsEnabled().set(false)
		enableDependencyInterfaceInjection = false
	}
	mods {
		register(Properties.MOD_ID) {
			sourceSet(sourceSets["main"])
		}
		register(Properties.MOD_ID + "_test") {
			sourceSet(sourceSets["test"])
		}
	}
	runs {
		named("client") {
			client()
			configName = "Fabric Client"
			setSource(sourceSets["test"])
			ideConfigGenerated(true)
			vmArgs("-Dmixin.debug.verbose=true", "-Dmixin.debug.export=true")
		}
		named("server") {
			server()
			configName = "Fabric Server"
			setSource(sourceSets["test"])
			ideConfigGenerated(true)
			vmArgs("-Dmixin.debug.verbose=true", "-Dmixin.debug.export=true")
		}
		register("datagen") {
			server()
			configName = "Fabric Datagen"
			setSource(sourceSets["test"])
			ideConfigGenerated(true)
			vmArg("-Dfabric-api.datagen")
			vmArg("-Dfabric-api.datagen.output-dir=${file("../common/src/generated/resources")}")
			vmArg("-Dfabric-api.datagen.modid=${Properties.MOD_ID}")
			runDir("build/datagen")
		}
		register("gameTest") {
			inherit(runs.getByName("server"))
			configName = "Fabric Game Test"
			vmArgs("-Dfabric-api.gametest")
			vmArgs("-Dfabric-api.gametest.report-file=${layout.buildDirectory.get()}/junit.xml")
			runDir("build/gametest")
		}
	}
}

tasks {
	named<ProcessResources>("processResources").configure {
		exclude("${Properties.MOD_ID}.cfg")
	}

	withType<AbstractArchiveTask> {
		from(files(project(":common").sourceSets["main"].output)) {
			duplicatesStrategy = DuplicatesStrategy.INCLUDE
			rename {
				if (it == "module-info.class") {
					return@rename "common-module-info.class"
				} else {
					return@rename it
				}
			}
		}
	}

	withType<Jar> {
		doLast {
			try {
				val jar = this@withType.archiveFile.get().asFile
				val zipFile = ZipFile(jar)
				val fabricModuleInfo = zipFile.getEntry("module-info.class")
				val commonModuleInfo = zipFile.getEntry("common-module-info.class") ?: return@doLast
				zipFile.getInputStream(commonModuleInfo).use { common ->
					zipFile.getInputStream(fabricModuleInfo).use { fabric ->
						val classReader = ClassReader(common.readBytes())
						val classNode = ClassNode(ASM9)
						classReader.accept(classNode, 0)

						val fabricClassReader = ClassReader(fabric.readBytes())
						val fabricClassNode = ClassNode(ASM9)
						fabricClassReader.accept(fabricClassNode, 0)

						// remove exports to fabric module
						classNode.module.exports?.removeIf { exports ->
							exports.modules.any { it.startsWith(classNode.module.name) }
						}
						// only add non-duplicate or non-project requires
						fabricClassNode.module.requires?.filter {
							!it.module.startsWith(classNode.module.name) &&
									!classNode.module.requires?.map { it.module }?.contains(it.module)!!
						}?.apply { classNode.module.requires?.addAll(this@apply) }

						val classWriter = ClassWriter(0)
						classNode.accept(classWriter)
						jar.plopInZip("module-info-meow.class", classWriter.toByteArray())
						println(common)
						println(fabric)
					}
				}
				jar.pluckFromZip("common-module-info.class")
				jar.pluckFromZip("module-info.class")
				val newZipFile = ZipFile(jar)
				newZipFile.getInputStream(newZipFile.getEntry("module-info-meow.class")).use {
					jar.plopInZip("module-info.class", it.readAllBytes())
				}
				jar.pluckFromZip("module-info-meow.class")
			} catch (_: java.nio.file.NoSuchFileException) {
			} catch (e: Exception) {
				logger.error("messing with jpms:")
				throw e
			}
		}
	}
}

beforeEvaluate {
	tasks.withType<JavaCompile> {
		options.compilerArgs.addAll(Properties.JAVAC_ARGS)
	}
}

fun fsModule(file: File, identifier: String, moduleName: String, action: Action<ModuleInfo>) {
	val relativeFile = file.relativeTo(projectDir)
	val constructor = ModuleInfo::class.java.declaredConstructors[0]
	constructor.isAccessible = true
	val methodHandle = MethodHandles.lookup().unreflectConstructor(constructor)
	val moduleInfo = methodHandle.invoke(identifier, moduleName, null, objects) as ModuleInfo
	action.execute(moduleInfo)
	extraJavaModuleInfo.moduleSpecs.put(relativeFile.name, moduleInfo)
}

extraJavaModuleInfo {
	afterEvaluate {
		activate(project.configurations.named("modCompileClasspath"))
		val mcJars = loom.namedMinecraftJars
		for (mcJar in mcJars) {
			fsModule(
				mcJar,
				"com.mojang:minecraft-" + mcJars.indexOf(mcJar),
				"vanilla"
			) {
				exportAllPackages()
			}
		}
	}
	module("lgbt.greenhouse.silicate:common", "lgbt.greenhouse.silicate")
	automaticModule("com.mojang:authlib", "authlib")
}

fun String.toPath(): java.nio.file.Path {
	return kotlin.io.path.Path(this)
}

fun File.plopInZip(name: String, content: ByteArray) {
	if (!this.isFile) {
		throw IllegalArgumentException("file is not a zip file ${this.path}")
	}
	FileSystems.newFileSystem(this.path.toPath()).use { fs ->
		val ploppedFilePath = fs.getPath(name)
		Files.newOutputStream(ploppedFilePath, StandardOpenOption.CREATE).use { outputStream ->
			outputStream.write(content)
		}
	}
}

fun File.pluckFromZip(name: String) {
	if (!this.isFile) {
		throw IllegalArgumentException("file is not a zip file ${this.path}")
	}
	FileSystems.newFileSystem(this.path.toPath()).use { fs ->
		val pluckedFilePath = fs.getPath(name)
		Files.delete(pluckedFilePath)
	}
}

run {
	val modList = rootDir.run {
		resolve(".gradle")
			.resolve("loom-cache")
			.resolve("remapped_mods")
	}.walkTopDown().filter {
		return@filter it.isFile && it.name.endsWith(".jar") && !it.name.endsWith("-sources.jar")
	}

	for (jar in modList) {
		if (!jar.name.endsWith(".jar")) continue
		var name = jar.name
		repeat(10) {
			name = name.split("-$it")[0]
		}
		val moduleName = name.split("-").joinToString(".")

		// first we find packages in the jar
		val initialPackages = mutableSetOf<String>()
		val packages = mutableSetOf<String>()
		zipTree(jar).visit {
			if (!this@visit.isDirectory) {
				if (!this@visit.name.endsWith(".class")) return@visit
				// do not add impl or mixin packages
				if (this@visit.path.contains("/impl/")) return@visit
				if (this@visit.path.contains("/mixin/")) return@visit

				val parentPath = this@visit.relativePath.parent.pathString
				if (initialPackages.contains(parentPath)) {
					packages.add(parentPath)
				}

				return@visit
			}

			initialPackages.add(this@visit.path)
		}

		// then we make the module-info.class
		val classWriter = ClassWriter(0)
		classWriter.visit(
			V21,
			ACC_MODULE,
			"module-info",
			null,
			null,
			null
		)
		val moduleVisitor = classWriter.visitModule(moduleName, 0, null)
		moduleVisitor.visitRequire("vanilla", 0, null)
		moduleVisitor.visitRequire("com.mojang.authlib", 0, null)
		moduleVisitor.visitRequire("com.mojang.datafixerupper", 0, null)
		moduleVisitor.visitRequire("java.base", 0, null)
		for (pkg in packages) {
			moduleVisitor.visitExport(pkg, 0)
		}
		moduleVisitor.visitEnd()
		classWriter.visitEnd()
		jar.plopInZip("module-info.class", classWriter.toByteArray())
	}
}

publishMods {
	file.set(tasks.named<Jar>("remapJar").get().archiveFile)
	modLoaders.add("fabric")
	changelog = rootProject.file("CHANGELOG.md").readText()
	displayName = "v${Versions.MOD} (Fabric ${Versions.MINECRAFT})"
	version = "${Versions.MOD}+${Versions.MINECRAFT}-fabric"
	type = BETA

	modrinth {
		projectId = Properties.MODRINTH_PROJECT_ID
		accessToken = providers.environmentVariable("MODRINTH_TOKEN")

		requires {
			slug = "fabric-api"

			version = Versions.FABRIC_API
		}

		minecraftVersions.add(Versions.MINECRAFT)
	}

	forgejo {
		type = STABLE
		accessToken = providers.environmentVariable("FORGEJO_TOKEN")
		parent(project(":common").tasks.named("publishForgejo"))
	}
}
