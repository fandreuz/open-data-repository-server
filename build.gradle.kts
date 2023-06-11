plugins {
	java
	id("io.quarkus")
	id("io.freefair.lombok") version "8.0.1"
	id("com.diffplug.spotless") version "6.19.0"
}

repositories {
	mavenCentral()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val slf4jVersion: String by project
val logbackVersion: String by project
val mongoDriverSyncVersion: String by project
val mongoCryptVersion: String by project
val apacheCommonsVersion: String by project
val apacheCommonsCsvVersion: String by project
val jsoupVersion: String by project

dependencies {
	// Quarkus
	implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
	implementation("io.quarkus:quarkus-resteasy-reactive")
	implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
	implementation("io.quarkus:quarkus-arc")
	implementation("io.quarkus:quarkus-hibernate-validator")
	implementation("io.quarkus:quarkus-smallrye-openapi")
	implementation("io.quarkus:quarkus-mongodb-client")
	// Logging
	implementation("org.slf4j:slf4j-api:${slf4jVersion}")
	implementation("ch.qos.logback:logback-classic:${logbackVersion}")
	// Utilities
	implementation("org.apache.commons:commons-lang3:${apacheCommonsVersion}")
	implementation("org.apache.commons:commons-csv:${apacheCommonsCsvVersion}")
	implementation("org.jsoup:jsoup:${jsoupVersion}")
}

group = "io.github.fandreuz"
version = "1.0.0-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<Test> {
	systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
	options.compilerArgs.add("-parameters")
}

tasks.quarkusDev {
	environmentVariables.set(mapOf("mongodb.uri" to "mongodb://localhost:27017", "root.executables.path" to "/home/fandreuz/go/bin"))
}

spotless {
	format("misc") {
		// define the files to apply `misc` to
		target("*.gradle.kts", ".gitignore")

		trimTrailingWhitespace()
		indentWithTabs()
		endWithNewline()
	}
	java {
		eclipse()
		indentWithSpaces(3)
		removeUnusedImports()
	}
}
