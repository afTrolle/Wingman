rootProject.name = "Wingman"
include(":androidApp")
include(":shared")

include(":library:ai")
include(":library:common")
include(":library:db")
include(":library:ui")
include(":library:user")

// Remove modules when paging 3 has compose multiplatform support
// Check https://github.com/androidx/androidx/pulls?q=+is%3Apr+author%3Aveyndan+
include(":library:paging:paging-common")
include(":library:paging:paging-compose")

include(":library:pull-to-refresh")

include(":library:accompanist:placeholder")
include(":library:accompanist:placeholder-material")

include(":feature:home")
include(":feature:settings")
include(":feature:sign-in")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev/")
    }
}

// Generate type safe accessors when referring to other projects eg.
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")