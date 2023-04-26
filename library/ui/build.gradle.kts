import dev.trolle.wingman.gradle.compose

plugins {
    `library-plugin`
    aliasId(libs.plugins.jetbrains.compose)
    aliasId(libs.plugins.google.ksp)
}

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.core)
            }
        }
        val commonMain by getting {
            // Temp-fix https://github.com/google/ksp/issues/567
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")

            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

//                implementation(libs.koin.core)
                implementation(libs.image.loader.core)
                implementation(libs.voyager.navigator)
                implementation(libs.lyricist.library)
                implementation(libs.accompanist.systemuicontroller)
            }
        }
    }
}

// https://github.com/adrielcafe/lyricist#multiplatform-setup
// Temp-fix https://github.com/google/ksp/issues/567
dependencies {
    add("kspCommonMainMetadata", libs.lyricist.processor)
}

ksp {
    arg("lyricist.internalVisibility", "true")
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>> {
    if(name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
