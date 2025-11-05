import net.modgarden.silicate.gradle.Properties
import net.modgarden.silicate.gradle.Versions

plugins {
	id("conventions.common")
	id("net.neoforged.moddev")
	id("me.modmuss50.mod-publish-plugin")
	id("org.gradlex.extra-java-module-info") version "1.13"
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

extraJavaModuleInfo {
	module("net.fabricmc:sponge-mixin", "org.spongepowered.mixin") {
		patchRealModule()
		requireAllDefinedDependencies()
		exportAllPackages()
		knownModule("com.google.guava:guava", "com.google.common")
		knownModule("com.google.code.gson:gson", "com.google.gson")
		knownModule("org.ow2.asm:asm-tree", "org.objectweb.asm.tree")
		knownModule("org.ow2.asm:asm-commons", "org.objectweb.asm.commons")
		knownModule("org.ow2.asm:asm-util", "org.objectweb.asm.util")
	}
	module("com.mojang:datafixerupper", "com.mojang.datafixerupper") {
		patchRealModule()
		requireAllDefinedDependencies()
		exportAllPackages()
		knownModule("org.slf4j:slf4j-api", "org.slf4j")
		knownModule("it.unimi.dsi:fastutil", "it.unimi.dsi.fastutil")
		module("com.google.code.findbugs:jsr305", "com.google.code.findbugs.jsr305")
	}
	failOnMissingModuleInfo = false
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
