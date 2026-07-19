plugins {
    kotlin("jvm") version "2.0.0"
    `maven-publish`
}

group = "cn.afeibaili.mchat"
version = properties["mchat.version"] as String
val mavenPackageName = "core"

val jacksonVersion = "2.18.6"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

java {
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/afeibaili/mchatlibrary")
            credentials {
                username = System.getenv("GITHUB_ACCOUNT")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("gpr") {
            groupId = group.toString()
            artifactId = mavenPackageName
            version = project.version.toString()

            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}