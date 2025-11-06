plugins {
	id("conventions.common")
}

configurations {
	register("commonJava") {
		isCanBeResolved = true
	}
	register("commonResources") {
		isCanBeResolved = true
	}
	register("commonTestJava") {
		isCanBeResolved = true
	}
	register("commonTestResources") {
		isCanBeResolved = true
	}
}

gradle.projectsEvaluated {
	sourceSets {
		getByName("main") {
			compileClasspath += project(":xplat").sourceSets["main"].output
			runtimeClasspath += project(":xplat").sourceSets["main"].output
		}
		getByName("test") {
			compileClasspath += project(":xplat").sourceSets["test"].output
			runtimeClasspath += project(":xplat").sourceSets["test"].output
		}
	}
}

dependencies {
	testCompileOnly(project(":xplat"))

	"commonJava"(project(":xplat", "commonJava"))
	"commonResources"(project(":xplat", "commonResources"))
	"commonTestJava"(project(":xplat", "commonTestJava"))
	"commonTestResources"(project(":xplat", "commonTestResources"))
}

tasks {
	named<JavaCompile>("compileJava").configure {
		dependsOn(configurations.getByName("commonJava"))
	}
	named<JavaCompile>("compileTestJava").configure {
		dependsOn(configurations.getByName("commonTestJava"))
	}
	named<ProcessResources>("processResources").configure {
		dependsOn(configurations.getByName("commonResources"))
		from(configurations.getByName("commonResources"))
		from(configurations.getByName("commonResources"))
	}
	named<ProcessResources>("processTestResources").configure {
		dependsOn(configurations.getByName("commonTestResources"))
		from(configurations.getByName("commonTestResources"))
		from(configurations.getByName("commonTestResources"))
	}
	named<Javadoc>("javadoc").configure {
		dependsOn(configurations.getByName("commonJava"))
		source(configurations.getByName("commonJava"))
	}
	named<Jar>("sourcesJar").configure {
		dependsOn(configurations.getByName("commonJava"))
		from(configurations.getByName("commonJava"))
		dependsOn(configurations.getByName("commonResources"))
		from(configurations.getByName("commonResources"))
	}
}
