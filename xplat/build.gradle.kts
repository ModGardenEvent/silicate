import net.modgarden.silicate.gradle.Properties
import net.modgarden.silicate.gradle.Versions

plugins {
	id("conventions.common")
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
	compileOnly("net.fabricmc:sponge-mixin:${Versions.FABRIC_MIXIN}")
	compileOnly("com.mojang:datafixerupper:${Versions.DFU}")
	compileOnly("cpw.mods:modlauncher:${Versions.MODLAUNCHER}")

	implementation("dev.lukebemish:codecextras:${Versions.CODEC_EXTRAS}")
}

tasks.withType<JavaCompile> {
	options.compilerArgs.addAll(listOf(
		"--upgrade-module-path", classpath.asPath
	))
	options.compilerArgs.addAll(Properties.JAVAC_ARGS)
}

configurations {
	register("commonJava") {
		isCanBeResolved = false
		isCanBeConsumed = true
	}
	register("commonResources") {
		isCanBeResolved = false
		isCanBeConsumed = true
	}
	register("commonTestJava") {
		isCanBeResolved = false
		isCanBeConsumed = true
	}
	register("commonTestResources") {
		isCanBeResolved = false
		isCanBeConsumed = true
	}
}

artifacts {
	add("commonJava", sourceSets["main"].java.sourceDirectories.singleFile)
	add("commonResources", sourceSets["main"].resources.sourceDirectories.singleFile)
	add("commonTestJava", sourceSets["test"].java.sourceDirectories.singleFile)
	add("commonTestResources", sourceSets["test"].resources.sourceDirectories.singleFile)
}

publishMods {
	changelog = rootProject.file("CHANGELOG.md").readText()
	displayName = "v${Versions.MOD} (Minecraft ${Versions.MINECRAFT})"
	version = "${Versions.MOD}+${Versions.MINECRAFT}"
	type = STABLE

	forgejo {
		accessToken = providers.environmentVariable("FORGEJO_TOKEN")
		host(Properties.FORGEJO_HOST)
		repository = Properties.FORGEJO_REPO
		tagName = "v${Versions.MOD}+${Versions.MINECRAFT}"
		commitish = Properties.FORGEJO_COMITISH

		allowEmptyFiles = true
	}
}
