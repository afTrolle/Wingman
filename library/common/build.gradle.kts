import dev.trolle.wingman.gradle.compose

plugins {
    `base-plugin`
    aliasId(libs.plugins.buildconfig)
    aliasId(libs.plugins.jetbrains.compose)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(libs.koin.core)
            }
        }
    }
}
buildConfig {
    val openApiKey: String = project.properties.getOrDefault("open.api.key", "").toString()
    val enableLogging: String by project.properties
    buildConfigField("String", "OPEN_API_TOKEN", "\"$openApiKey\"")
    buildConfigField("boolean", "LOGGING_ENABLED", enableLogging)
}
