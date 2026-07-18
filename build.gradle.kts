plugins {
    kotlin("jvm") version "2.2.20"
}

group = "cn.afeibaili.mchat"
version = properties["mchat.version"] as String

val jacksonVersion = "2.18.6"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}