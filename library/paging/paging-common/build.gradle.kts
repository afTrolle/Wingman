import dev.trolle.wingman.gradle.libs

plugins {
    `library-plugin`
    aliasId(libs.plugins.atomicfu)
}

kotlin {
    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(libs.androidx.annotation)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.atomicfu)
                implementation(libs.stately.common)
                implementation(libs.stately.collection)
            }
        }
    }
}
