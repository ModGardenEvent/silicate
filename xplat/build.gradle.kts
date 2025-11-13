import me.modmuss50.mpp.ReleaseType
import net.modgarden.silicate.gradle.Properties
import net.modgarden.silicate.gradle.Versions

plugins {
	id("conventions.xplat")
	id("net.neoforged.moddev")
	id("me.modmuss50.mod-publish-plugin")
}

sourceSets {
	create("generated") {
		resources {
			srcDir("src/generated/resources")
		}
	}
}

neoForge {
	neoFormVersion = Versions.NEOFORM
	parchment {
		minecraftVersion = Versions.PARCHMENT_MINECRAFT
		mappingsVersion = Versions.PARCHMENT
	}
	addModdingDependenciesTo(sourceSets["test"])

	configurations {
	}

	val at = file("src/main/resources/${Properties.MOD_ID}.cfg")
	if (at.exists())
		setAccessTransformers(at)
	validateAccessTransformers = true
}

repositories {
	maven {
		name = "Minecraft Libraries"
		url = uri("https://libraries.minecraft.net")
	}
}

dependencies {
	compileOnly("io.github.llamalad7:mixinextras-common:${Versions.MIXIN_EXTRAS}")
	annotationProcessor("io.github.llamalad7:mixinextras-common:${Versions.MIXIN_EXTRAS}")
	compileOnly("net.fabricmc:sponge-mixin:${Versions.FABRIC_MIXIN}") {
		exclude("org.ow2.asm")
	}
	compileOnly("com.mojang:datafixerupper:${Versions.DFU}")
	compileOnly("cpw.mods:modlauncher:${Versions.MODLAUNCHER}") {
		exclude("org.ow2.asm")
	}

	implementation("dev.lukebemish:codecextras:${Versions.CODEC_EXTRAS}")
}

extraJavaModuleInfo {
	skipLocalJars = true
}

try {
	tasks.withType<JavaCompile> {
		options.compilerArgs.addAll(listOf(
			"--module-path", classpath.asPath
		))
		options.compilerArgs.addAll(Properties.JAVAC_ARGS)
	}
} catch (e: Exception) {
	logger.error("when configuring javac args: ", e)
}

tasks {
	withType<Javadoc> {
		this@withType.options.modulePath.addAll(classpath)
		this@withType.isFailOnError = false
	}
}

configurations {
	register("xplatJava") {
		isCanBeResolved = false
		isCanBeConsumed = true
	}
	register("xplatResources") {
		isCanBeResolved = false
		isCanBeConsumed = true
	}
	register("xplatTestJava") {
		isCanBeResolved = false
		isCanBeConsumed = true
	}
	register("xplatTestResources") {
		isCanBeResolved = false
		isCanBeConsumed = true
	}
}

artifacts {
	add("xplatJava", sourceSets["main"].java.sourceDirectories.singleFile)
	add("xplatResources", sourceSets["main"].resources.sourceDirectories.singleFile)
	add("xplatTestJava", sourceSets["test"].java.sourceDirectories.singleFile)
	add("xplatTestResources", sourceSets["test"].resources.sourceDirectories.singleFile)
}

publishMods {
	changelog = rootProject.file("CHANGELOG.md").readText()
	displayName = "v${Versions.MOD} (Minecraft ${Versions.MINECRAFT})"
	version = "${Versions.MOD}+${Versions.MINECRAFT}"
	type = ReleaseType.of(Versions.MOD_CHANNEL)

	forgejo {
		accessToken = providers.environmentVariable("FORGEJO_TOKEN")
		host(Properties.FORGEJO_HOST)
		repository = Properties.FORGEJO_REPO
		tagName = "v${Versions.MOD}+${Versions.MINECRAFT}"
		commitish = Properties.FORGEJO_COMITISH
		type = ReleaseType.of(Versions.MOD_CHANNEL)

		allowEmptyFiles = true
	}
}
