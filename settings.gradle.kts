rootProject.name = "Wingman"
include(":androidApp")
include(":shared")

include(":library:ai")
include(":library:common")
include(":library:ui")
include(":library:user")

include(":feature:home")
include(":feature:settings")
include(":feature:sign-in")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

// Generate type safe accessors when referring to other projects eg.
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")