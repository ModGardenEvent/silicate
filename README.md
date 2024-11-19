# Greenhouse Multiloader Template
This is a version of the Multiloader Template that is tailored towards Greenhouse's mods.

## Swapping Versions
Versions and properties are not within gradle.properties. This is because Kotlin DSL does not play nicely with gradle.properties.
Versions are contained within `buildSrc/src/main/kotlin/dev/greenhouseteam/examplemod/gradle/Versions.kt`.
Properties are contained within `buildSrc/src/main/kotlin/dev/greenhouseteam/examplemod/gradle/Properties.kt`.

It is ideal to change any mention of `examplemod` within the codebase when creating from this template.

## Changes from MLT
The main changes from the Multiloader Template, which we have forked are:
- Uses Kotlin DSL instead of Groovy DSL.
- Rewrites to expanded properties, to fit loader conventions and create less overhead.
- Removal of access transformer file from Fabric's build and refmap line in common mixins.json for NeoForge.
- Platform Helpers are done without services, instead being added to the common class within Fabric pre-launch and NeoForge init.
  - This must be done in pre-launch on Fabric due to random load order for mods. Otherwise the game may crash if a mod depends on the helper.
- Cut down on a few classes.
- Set up client mixins from the example in the `mixin.client` package, following convention for Greenhouse mixins.
- Modmuss' mod publish plugin is set up for both loaders. For CurseForge, Modrinth and GitHub.
  - Feel free to remove any of these, you may desire GitHub only if the mod is supposed to be an internal library
  - You can run this with `publishMods`. Just make sure tokens are set up.

# Remember to change this README for any projects!