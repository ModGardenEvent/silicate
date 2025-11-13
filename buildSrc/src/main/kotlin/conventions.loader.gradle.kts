plugins {
	id("conventions.xplat")
}

configurations {
	register("xplatJava") {
		isCanBeResolved = true
	}
	register("xplatResources") {
		isCanBeResolved = true
	}
	register("xplatTestJava") {
		isCanBeResolved = true
	}
	register("xplatTestResources") {
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

	"xplatJava"(project(":xplat", "xplatJava"))
	"xplatResources"(project(":xplat", "xplatResources"))
	"xplatTestJava"(project(":xplat", "xplatTestJava"))
	"xplatTestResources"(project(":xplat", "xplatTestResources"))
}

tasks {
	named<JavaCompile>("compileJava").configure {
		dependsOn(configurations.getByName("xplatJava"))
	}
	named<JavaCompile>("compileTestJava").configure {
		dependsOn(configurations.getByName("xplatTestJava"))
	}
	named<ProcessResources>("processResources").configure {
		dependsOn(configurations.getByName("xplatResources"))
		from(configurations.getByName("xplatResources"))
		from(configurations.getByName("xplatResources"))
	}
	named<ProcessResources>("processTestResources").configure {
		dependsOn(configurations.getByName("xplatTestResources"))
		from(configurations.getByName("xplatTestResources"))
		from(configurations.getByName("xplatTestResources"))
	}
	named<Javadoc>("javadoc").configure {
		dependsOn(configurations.getByName("xplatJava"))
	}
	named<Jar>("sourcesJar").configure {
		dependsOn(configurations.getByName("xplatJava"))
		from(configurations.getByName("xplatJava"))
		dependsOn(configurations.getByName("xplatResources"))
		from(configurations.getByName("xplatResources"))
	}
}
