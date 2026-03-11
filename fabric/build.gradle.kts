import me.modmuss50.mpp.ReleaseType
import net.modgarden.silicate.gradle.Properties
import net.modgarden.silicate.gradle.Versions
import org.gradle.jvm.tasks.Jar
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.StandardOpenOption

evaluationDependsOn(":xplat")

plugins {
	id("conventions.loader")
	id("fabric-loom")
	id("me.modmuss50.mod-publish-plugin")
}

repositories {
	maven {
		name = "TerraformersMC"
		url = uri("https://maven.terraformersmc.com/releases/")
	}
	maven {
		name = "NeoForge"
		url = uri("https://maven.neoforged.net/releases/")
	}
}

sourceSets {
	getByName("main") {
		runtimeClasspath += project(":xplat").sourceSets["test"].output
	}
	getByName("test") {
		runtimeClasspath += project(":xplat").sourceSets["test"].output
	}
}

dependencies {
	runtimeOnly(project(":xplat"))
	minecraft("com.mojang:minecraft:${Versions.MINECRAFT}")
	mappings(loom.layered {
		officialMojangMappings()
		parchment("org.parchmentmc.data:parchment-${Versions.PARCHMENT_MINECRAFT}:${Versions.PARCHMENT}")
	})

	modImplementation("net.fabricmc:fabric-loader:${Versions.FABRIC_LOADER}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${Versions.FABRIC_API}")
	modLocalRuntime("com.terraformersmc:modmenu:${Versions.MOD_MENU}")
	compileOnly("io.github.llamalad7:mixinextras-fabric:${Versions.MIXIN_EXTRAS}")
	annotationProcessor("io.github.llamalad7:mixinextras-fabric:${Versions.MIXIN_EXTRAS}")
	compileOnly("com.mojang:datafixerupper:${Versions.DFU}")

	implementation("dev.lukebemish:codecextras:${Versions.CODEC_EXTRAS}")
	include("dev.lukebemish:codecextras:${Versions.CODEC_EXTRAS}")
	compileOnly("cpw.mods:modlauncher:${Versions.MODLAUNCHER}") {
		exclude("org.ow2.asm")
	}
}

loom {
	val aw = file("src/main/resources/${Properties.MOD_ID}.accesswidener")
	if (aw.exists())
		accessWidenerPath.set(aw)
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
			vmArg("-Dfabric-api.datagen.output-dir=${file("../xplat/src/generated/resources")}")
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


	withType<Javadoc> {
		// no javadoc for fabric jar
		if (this@withType.title?.contains("fabric") == true) {
			exclude("*")
		}
	}
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

publishMods {
	file.set(tasks.named<Jar>("remapJar").get().archiveFile)
	modLoaders.add("fabric")
	changelog = rootProject.file("CHANGELOG.md").readText()
	displayName = "v${Versions.MOD} (Fabric ${Versions.MINECRAFT})"
	version = "${Versions.MOD}+${Versions.MINECRAFT}-fabric"
	type = ReleaseType.of(Versions.MOD_CHANNEL)

	modrinth {
		projectId = Properties.MODRINTH_PROJECT_ID
		accessToken = providers.environmentVariable("MODRINTH_TOKEN")
		type = ReleaseType.of(Versions.MOD_CHANNEL)

		requires {
			slug = "fabric-api"

			version = Versions.FABRIC_API
		}

		minecraftVersions.add(Versions.MINECRAFT)
	}

	forgejo {
		type = ReleaseType.of(Versions.MOD_CHANNEL)
		accessToken = providers.environmentVariable("FORGEJO_TOKEN")
		parent(project(":xplat").tasks.named("publishForgejo"))
	}
}
