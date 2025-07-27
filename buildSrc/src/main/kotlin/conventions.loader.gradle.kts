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
			compileClasspath += project(":common").sourceSets["main"].output
			runtimeClasspath += project(":common").sourceSets["main"].output
		}
		getByName("test") {
			compileClasspath += project(":common").sourceSets["test"].output
			runtimeClasspath += project(":common").sourceSets["test"].output
		}
	}
}

dependencies {
	testCompileOnly(project(":common"))

	"commonJava"(project(":common", "commonJava"))
	"commonResources"(project(":common", "commonResources"))
	"commonTestJava"(project(":common", "commonTestJava"))
	"commonTestResources"(project(":common", "commonTestResources"))
}

tasks {
	named<JavaCompile>("compileJava").configure {
		dependsOn(configurations.getByName("commonJava"))
		source(configurations.getByName("commonJava"))
	}
	named<JavaCompile>("compileTestJava").configure {
		dependsOn(configurations.getByName("commonTestJava"))
		source(configurations.getByName("commonTestJava"))
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
