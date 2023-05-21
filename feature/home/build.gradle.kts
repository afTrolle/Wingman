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
                implementation(projects.library.pullToRefresh)
                implementation(projects.library.accompanist.placeholderMaterial)
                implementation(projects.library.accompanist.placeholder)
                implementation(libs.voyager.tab)
            }
        }
    }
}
