plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins.register("feature-plugin") {
        id = "feature-plugin"
        implementationClass = "dev.trolle.wingman.gradle.FeaturePlugin"
    }
    plugins.register("library-plugin") {
        id = "library-plugin"
        implementationClass = "dev.trolle.wingman.gradle.LibraryPlugin"
    }
    plugins.register("base-plugin") {
        id = "base-plugin"
        implementationClass = "dev.trolle.wingman.gradle.BasePlugin"
    }
}

dependencies {
    implementation(libs.gradlePlugin.android.library)
    implementation(libs.gradlePlugin.android.application)
    implementation(libs.gradlePlugin.kotlin.multiplatform)
    implementation(libs.gradlePlugin.kotlin.android)
    implementation(libs.gradlePlugin.kotlin.cocoapods)
    implementation(libs.gradlePlugin.kotlin.serialization)
    implementation(libs.gradlePlugin.jetbrains.compose)
    implementation(libs.gradlePlugin.buildconfig)
    implementation(libs.gradlePlugin.ktlint)
    // include generated from "/gradle/libs.versions.toml"
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}