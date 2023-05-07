import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.garsemar"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    val exposedVersion: String by project
    val slf4jVersion: String by project
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                // Exposed
                implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

                // SQLite
                implementation("org.xerial:sqlite-jdbc:3.30.1")

                // slf4j
                implementation ("org.slf4j:slf4j-api:$slf4jVersion")
                implementation ("org.slf4j:slf4j-simple:$slf4jVersion")

                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "Quicknote"
            version = "1.0.1"
            description = "Desktop app for quick notes"
            copyright = "© 2023 Marti Garcia. All rights reserved."
            vendor = "garsemar"
            licenseFile.set(project.file("LICENSE"))
        }
    }
}
