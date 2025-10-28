@file:Suppress("UnstableApiUsage") // respectfully, shut the f*#@ up

import net.modgarden.silicate.gradle.Properties
import net.modgarden.silicate.gradle.Versions
import org.apache.tools.ant.filters.LineContains
import org.gradle.jvm.tasks.Jar

plugins {
	id("conventions.loader")
	id("net.neoforged.moddev")
	id("me.modmuss50.mod-publish-plugin")
}

neoForge {
	version = Versions.NEOFORGE
	parchment {
		minecraftVersion = Versions.PARCHMENT_MINECRAFT
		mappingsVersion = Versions.PARCHMENT
	}
	addModdingDependenciesTo(sourceSets["test"])

	val at = project(":common").file("src/main/resources/${Properties.MOD_ID}.cfg")
	if (at.exists())
		setAccessTransformers(at)
	validateAccessTransformers = true

	runs {
		configureEach {
			systemProperty("forge.logging.markers", "REGISTRIES")
			systemProperty("forge.logging.console.level", "debug")
			systemProperty("neoforge.enabledGameTestNamespaces", Properties.MOD_ID)
		}
		create("client") {
			client()
			ideName = "NeoForge Client (:${project.name})"
			gameDirectory.set(file("runs/client"))
			sourceSet = sourceSets["test"]
			jvmArguments.set(setOf("-Dmixin.debug.verbose=true", "-Dmixin.debug.export=true"))
		}
		create("server") {
			server()
			ideName = "NeoForge Server (:${project.name})"
			gameDirectory.set(file("runs/server"))
			programArgument("--nogui")
			sourceSet = sourceSets["test"]
			jvmArguments.set(setOf("-Dmixin.debug.verbose=true", "-Dmixin.debug.export=true"))
		}
		create("gameTest") {
			type = "gameTestServer"
			ideName = "NeoForge Game Test (:${project.name})"
			sourceSet = sourceSets["test"]
			gameDirectory = project.file("build/gametest")
		}
	}

	mods {
		register(Properties.MOD_ID) {
			sourceSet(sourceSets["main"])
		}
		register(Properties.MOD_ID + "_test") {
			sourceSet(sourceSets["test"])
		}
	}
}

dependencies {
	jarJar("dev.lukebemish:codecextras:${Versions.CODEC_EXTRAS}")
}

tasks {
	named<ProcessResources>("processResources").configure {
		filesMatching("*.mixins.json") {
			filter<LineContains>("negate" to true, "contains" to setOf("refmap"))
		}
	}
}

publishMods {
	file.set(tasks.named<Jar>("jar").get().archiveFile)
	modLoaders.add("neoforge")
	changelog = rootProject.file("CHANGELOG.md").readText()
	displayName = "v${Versions.MOD} (NeoForge ${Versions.MINECRAFT})"
	version = "${Versions.MOD}+${Versions.MINECRAFT}-neoforge"
	type = BETA

	modrinth {
		projectId = Properties.MODRINTH_PROJECT_ID
		accessToken = providers.environmentVariable("MODRINTH_TOKEN")

		minecraftVersions.add(Versions.MINECRAFT)
	}

	forgejo {
		type = STABLE
		accessToken = providers.environmentVariable("FORGEJO_TOKEN")
		parent(project(":common").tasks.named("publishForgejo"))
	}
}
