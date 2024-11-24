# Silicate
A library providing powerful client-server-agnostic predicates with a simple API.
> [!IMPORTANT]
> This library is **new; use it with caution.** The API may change frequently, and not all features have been implemented.
> Check the issue tracker for updates.

## Usage
Add this to your `build.gradle`(`.kts`)
```gradle
repositories {
	exclusiveContent {
		forRepositories(
			maven("https://maven.muonmc.org/releases") {
				name = "MuonMC"
			}
		)
		filter { includeGroup("house.greenhouse.silicate") }
	}
}

dependencies {
	implementation("house.greenhouse.silicate:silicate-PLATFORM:VERSION")
	// Or, if using multiloader
	// compileOnly("house.greenhouse.silicate:silicate-common:VERSION")
}
```

## Goal
The goal of Silicate is to complete a context-aware predicate system on par with Minecraft's Loot Context-aware predicates. Available for Fabric and NeoForge, Silicate provides a common API for interfacing advanced predicates between mods, across platforms. Silicate achieves this by implementing a clean-slate predicate architecture, initially independent of Minecraft.

## Features
Silicate provides out-of-the-box Codec and datapack support, enabling developers and modpack designers to more easily implement and configure desired behavior. Silicate also comes with built-in predicates for Minecraft's various game interactions, easing the amount of boilerplate necessary.

## Contributing
Before you contribute, please read about [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/). This repository uses Conventional Commits for all commit messages, and any non-conforming commit messages will be converted.