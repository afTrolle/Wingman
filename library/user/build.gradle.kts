plugins {
    `library-plugin`
    aliasId(libs.plugins.kotlin.serialization)
}
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.json)
                implementation(libs.kotlinx.serialization.json)

                implementation(projects.library.db)
                implementation(projects.library.ai)
            }
        }
    }
}
