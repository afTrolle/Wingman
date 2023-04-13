rootProject.name = "Wingman"
include(":androidApp")
include(":shared")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

// Generate type safe accessors when referring to other projects eg.
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")