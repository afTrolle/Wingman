@file:Suppress("UNUSED_VARIABLE", "unused")

package dev.trolle.wingman.gradle

import com.android.build.gradle.BaseExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.plugin.use.PluginDependency
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jlleitschuh.gradle.ktlint.KtlintExtension

class FeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) = target.configure()

    private fun Project.configure() {
        plugins.run {
            alias(libs.plugins.library)
            alias(libs.plugins.jetbrains.compose)
        }
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets {
                val commonMain by getting {
                    dependencies {
                        implementation(compose.runtime)
                        implementation(compose.foundation)
                        implementation(compose.material3)
                        implementation(compose.ui)
                        implementation(compose.materialIconsExtended)
                        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                        implementation(compose.components.resources)
                        implementation(libs.image.loader.core)
                        implementation(libs.voyager.navigator)
                        implementation(project(":library:ui"))
                    }
                }
                val androidMain by getting {
                    dependencies {
                        implementation(compose.preview)
                        implementation(compose.uiTooling)
                        implementation(libs.androidx.core)
                        implementation(libs.koin.android)
                        implementation(libs.image.loader.core) // Check to disble later
                    }
                }
                val iosMain by getting {
                }
            }
        }

        extensions.configure<BaseExtension> {
            buildFeatures.compose = true
        }
    }
}


class LibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) = target.configure()

    private fun Project.configure() {
        plugins.run {
            alias(libs.plugins.base)
        }
        kotlin {
            sourceSets {
                val commonMain by getting {
                    dependencies {
                        implementation(libs.koin.core)
                        implementation(project(":library:common"))
                    }
                }
            }
        }
    }
}

class BasePlugin : Plugin<Project> {
    override fun apply(target: Project) = target.configure()

    private fun Project.configure() {
        plugins.run {
            alias(libs.plugins.kotlin.multiplatform)
            alias(libs.plugins.android.library)
            alias(libs.plugins.ktlint)
        }
        extensions.configure<KotlinMultiplatformExtension> {
            jvmToolchain(11)

            android()
            iosX64()
            iosArm64()
            iosSimulatorArm64()

            sourceSets {
                val commonMain by getting {
                    dependencies {
                        implementation(libs.kotlinx.coroutines.core)
                        implementation(libs.kotlinx.datetime)
                        implementation(libs.napier)
                    }
                }
                val commonTest by getting {
                    dependencies {
                        implementation(libs.kotlin.test)
                    }
                }
                val androidMain by getting
                val androidUnitTest by getting
                val iosX64Main by getting
                val iosArm64Main by getting
                val iosSimulatorArm64Main by getting
                val iosMain by creating {
                    dependsOn(commonMain)
                    iosX64Main.dependsOn(this)
                    iosArm64Main.dependsOn(this)
                    iosSimulatorArm64Main.dependsOn(this)
                }
                val iosX64Test by getting
                val iosArm64Test by getting
                val iosSimulatorArm64Test by getting
                val iosTest by creating {
                    dependsOn(commonTest)
                    iosX64Test.dependsOn(this)
                    iosArm64Test.dependsOn(this)
                    iosSimulatorArm64Test.dependsOn(this)
                }
            }
        }
        extensions.configure<BaseExtension> {
            namespace = "dev.trolle.af.wingman.${project.name.replace("-",".")}"
            compileSdkVersion(libs.versions.compileSdk.get().toInt())
            defaultConfig {
                minSdk = libs.versions.minSdk.get().toInt()
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
        }

        extensions.configure<KtlintExtension>() {
            filter {
                // Filter exclude issue - https://github.com/JLLeitschuh/ktlint-gradle/issues/579
                // exclude("**/generated/**")
                exclude { entry ->
                    entry.file.toString().contains("generated")
                }
            }
        }
    }
}

fun KotlinMultiplatformExtension.sourceSets(configure: Action<NamedDomainObjectContainer<KotlinSourceSet>>) =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("sourceSets", configure)

fun Project.`kotlin`(configure: Action<KotlinMultiplatformExtension>) =
    extensions.configure("kotlin", configure)

fun PluginContainer.alias(provider: Provider<PluginDependency>) {
    apply(provider.get().pluginId)
}

val org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension.`compose`: org.jetbrains.compose.ComposePlugin.Dependencies
    get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("compose") as org.jetbrains.compose.ComposePlugin.Dependencies

val Project.libs: LibrariesForLibs
    get() = (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("libs") as LibrariesForLibs
