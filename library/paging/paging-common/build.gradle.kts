plugins {
    `library-plugin`
    aliasId(libs.plugins.atomicfu)
}

kotlin {
    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation("androidx.annotation:annotation:1.7.0-alpha02")
                implementation(libs.kotlinx.coroutines.core)
                implementation("org.jetbrains.kotlinx:atomicfu:0.20.2")
                implementation("co.touchlab:stately-concurrent-collections:2.0.0-rc1")
            }
        }
    }
}

//dependencies {
//    constraints {
//        implementation(project(":paging:paging-runtime"))
//        // syntax is temp workaround to allow project dependency on cross-project-set
//        // i.e. COMPOSE + MAIN project sets
//        // update syntax when b/239979823 is fixed
//        implementation("androidx.paging:paging-compose:${androidx.LibraryVersions.PAGING_COMPOSE}")
//    }

//    api(libs.kotlinStdlib)
//    api(libs.kotlinCoroutinesCore)

//}
