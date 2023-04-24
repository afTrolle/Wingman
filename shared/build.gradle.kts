plugins {
    aliasId(libs.plugins.kotlin.multiplatform)
    aliasId(libs.plugins.android.library)
    aliasId(libs.plugins.kotlin.cocoapods)
    aliasId(libs.plugins.jetbrains.compose)
    aliasId(libs.plugins.kotlin.serialization)
    aliasId(libs.plugins.buildconfig)
    aliasId(libs.plugins.ktlint)
}

kotlin {
    jvmToolchain(11)

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
        all {
            languageSettings {
                optIn("com.russhwolf.settings.ExperimentalSettingsApi")
                optIn("com.aallam.openai.api.BetaOpenAI")
            }
        }
        val commonMain by getting {
            dependencies {
                // referenced from compose plugin
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(libs.accompanist.systemuicontroller)
                implementation(libs.voyager.navigator)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.json)
                implementation(libs.napier)
                implementation(libs.settings.coroutines)
                implementation(libs.settings.serialization)
                implementation(libs.settings.core)
                implementation(libs.openai.client)
                implementation(libs.image.loader.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.androidx.activity.compose)
                api(compose.preview)
                api(compose.uiTooling)
                api(libs.androidx.core)
                api(libs.koin.android)
                api(libs.play.services.auth)
                api(libs.kotlinx.coroutines.play)
                api(libs.libphonenumber)
                api(libs.image.loader.core)
                api(libs.accompanist.systemuicontroller)
                implementation(libs.settings.datastore)
                implementation(libs.androidx.datastore.preferences)
                implementation(libs.ktor.client.okhttp)
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
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
