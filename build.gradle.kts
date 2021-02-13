import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30"
    application
}

group = "me.aivean"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlinx") }
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
    maven { url = uri("https://dl.bintray.com/scottpjohnson/generic/") }
}

dependencies {
    testImplementation(kotlin("test-testng"))
    implementation("io.ktor:ktor-server-netty:1.4.0")
    implementation("io.ktor:ktor-html-builder:1.4.0")
    implementation("io.ktor:ktor-websockets:1.4.0")

    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")

    implementation("net.justmachinery:shade:0.5.3")

    implementation("io.github.microutils:kotlin-logging-jvm:2.0.2")
    implementation("ch.qos.logback:logback-classic:1.2.1")
}

tasks.test {
    useTestNG()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "ServerKt"
}