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
            }
        }
    }
}
