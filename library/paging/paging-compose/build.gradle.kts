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
                implementation(projects.library.paging.pagingCommon)
                implementation(compose.foundation)
                implementation(compose.runtime)
            }
        }
    }
}
