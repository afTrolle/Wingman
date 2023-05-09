@file:Suppress("UNUSED_VARIABLE")

plugins {
    `feature-plugin`
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.library.user)
                implementation(projects.library.paging.pagingCompose)
                implementation(projects.library.paging.pagingCommon)
            }
        }
        val androidMain by getting {
            dependencies {
            }
        }
    }
}
