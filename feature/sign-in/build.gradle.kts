@file:Suppress("UNUSED_VARIABLE")

plugins {
    `feature-plugin`
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.library.user)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.play.services.auth)
                implementation(libs.kotlinx.coroutines.play)
                implementation(libs.libphonenumber)
                implementation(libs.androidx.activity.compose)
            }
        }
    }
}
