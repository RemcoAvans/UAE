plugins {
    kotlin("jvm") version "1.9.10"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.3.5")
    implementation("io.ktor:ktor-server-netty:2.3.5")
    implementation("ch.qos.logback:logback-classic:1.4.11")
}

application {
    mainClass.set("MainKt")
}

kotlin {
    jvmToolchain(17) // of 20
}
