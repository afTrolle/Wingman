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
//                implementation("androidx.annotation:annotation:1.7.0-alpha02")
//                implementation(libs.kotlinx.coroutines.core)
//                implementation("org.jetbrains.kotlinx:atomicfu:0.20.2")
//                implementation("co.touchlab:stately-concurrent-collections:2.0.0-rc1")
            }
        }
    }
}


//android {
//    namespace "androidx.paging.compose"
//}
