plugins {
    kotlin("jvm") version "2.2.0"
    id("maven-publish")
}

group = "net.exoad"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}


publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/exoad/kansi")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create("gpr", MavenPublication::class) {
            groupId = "net.exoad"
            artifactId = "kansi"
            version = "1.0"
            from(components["kotlin"])
        }
    }
}


kotlin {
    jvmToolchain(21)
}