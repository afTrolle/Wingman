@file:Suppress("UNUSED_VARIABLE")

plugins {
    `library-plugin`
    aliasId(libs.plugins.kotlin.serialization)
}
kotlin {
    sourceSets {
        val androidMain  by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.json)
                implementation(libs.kotlinx.serialization.json)

                implementation(projects.library.db)
                implementation(projects.library.ai)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}
