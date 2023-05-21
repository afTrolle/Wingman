plugins {
    `library-plugin`
    aliasId(libs.plugins.jetbrains.compose)
    aliasId(libs.plugins.kotlin.parcelize)
}

kotlin {
    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(compose.foundation)
                implementation(compose.runtime)
                implementation(compose.material3)
            }
        }
    }
}
