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
                implementation(libs.ktor.client.encoding)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.json)
                implementation(libs.ktor.serialization.protobuf)
                implementation(libs.kotlinx.serialization.json)

                implementation(projects.library.paging.pagingCompose)
                implementation(projects.library.paging.pagingCommon)

                implementation(projects.library.db)
                implementation(projects.library.ai)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test.common)
                implementation(libs.kotlin.test.annotations.common)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}
