@file:Suppress("UNUSED_VARIABLE")

import dev.trolle.wingman.gradle.libs

plugins {
    aliasId(libs.plugins.kotlin.multiplatform)
    aliasId(libs.plugins.android.library)
    aliasId(libs.plugins.kotlin.cocoapods)
    aliasId(libs.plugins.jetbrains.compose)
    aliasId(libs.plugins.kotlin.serialization)
    aliasId(libs.plugins.ktlint)
}

kotlin {
    jvmToolchain(17)

    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "common shared code module"
        version = libs.versions.appVersion.get()
        ios.deploymentTarget = libs.versions.iosTarget.get()
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            homepage = " Website"
            isStatic = true
        }
        extraSpecAttributes["resources"] =
            "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.ui)

                implementation(libs.voyager.navigator)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.serialization.protobuf)
                implementation(libs.napier)

                implementation(projects.library.ui)
                implementation(projects.library.ai)
                implementation(projects.library.common)
                implementation(projects.library.db)
                implementation(projects.library.user)

                implementation(projects.feature.signIn)
                implementation(projects.feature.home)
            }
        }
        val commonTest by getting
        val androidMain by getting {
            dependencies {
                implementation(libs.koin.android)
            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "dev.trolle.af.wingman"
    compileSdk = libs.versions.compileSdk.get().toInt()
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
