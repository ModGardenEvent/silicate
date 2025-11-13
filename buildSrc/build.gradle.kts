plugins {
	`kotlin-dsl`
}

repositories {
	gradlePluginPortal()
}

dependencies {
	implementation("org.gradlex:extra-java-module-info:1.13.1")
	implementation("org.ow2.asm:asm-commons:9.6")
	implementation("org.ow2.asm:asm-tree:9.6")
	implementation("org.ow2.asm:asm-util:9.6")
}
