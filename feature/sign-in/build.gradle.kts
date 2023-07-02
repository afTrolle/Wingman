@file:Suppress("UNUSED_VARIABLE")

plugins {
    `feature-plugin`
    aliasId(libs.plugins.google.ksp)
    aliasId(libs.plugins.kotlin.parcelize)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.library.user)
                implementation(projects.library.ui)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.material3)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.play.services.auth)
                implementation(libs.kotlinx.coroutines.play)
                implementation(libs.libphonenumber)
                implementation(libs.androidx.activity.compose)
                implementation(compose.preview)
            }
        }
    }
}
