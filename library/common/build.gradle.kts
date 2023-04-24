plugins {
    `base-plugin`
    aliasId(libs.plugins.buildconfig)
}

buildConfig {
    val openApiKey: String = project.properties.getOrDefault("open.api.key", "").toString()
    val enableLogging: String by project.properties
    buildConfigField("String", "OPEN_API_TOKEN", "\"$openApiKey\"")
    buildConfigField("boolean", "LOGGING_ENABLED", enableLogging)
}
