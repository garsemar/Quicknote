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
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                // Mongo / Realm
                implementation("org.mongodb:mongodb-driver-sync:4.7.1")
                implementation("io.realm.kotlin:library-sync:1.5.0")
                implementation("org.slf4j:slf4j-log4j12:1.7.25")

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
            licenseFile.set(project.file("LICENSE.txt"))
        }
    }
}
