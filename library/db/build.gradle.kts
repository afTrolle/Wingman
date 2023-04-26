@file:Suppress("UNUSED_VARIABLE")

plugins {
    `library-plugin`
}

kotlin {
    sourceSets {
        all {
            languageSettings {
                optIn("com.russhwolf.settings.ExperimentalSettingsApi")
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(libs.settings.coroutines)
                implementation(libs.settings.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.koin.android)
                implementation(libs.settings.datastore)
                implementation(libs.androidx.datastore.preferences)
            }
        }
    }
}
