import dev.trolle.wingman.gradle.compose
import dev.trolle.wingman.gradle.libs

plugins {
    `library-plugin`
    aliasId(libs.plugins.jetbrains.compose)
}

// Code cloned from https://github.com/google/accompanist/tree/main/placeholder
// Done to support compose multiplatform
// TODO in future could probably use subtree or similar from git so we don't need to commit this code.

kotlin {
    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.animation)
                implementation(libs.napier)
                implementation(libs.androidx.annotation)
            }
        }
    }
}
