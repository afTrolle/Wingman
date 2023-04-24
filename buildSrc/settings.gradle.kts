rootProject.name = "WingmanBuildSrc"
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
