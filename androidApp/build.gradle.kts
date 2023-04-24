plugins {
    aliasId(libs.plugins.android.application)
    aliasId(libs.plugins.kotlin.android)
    aliasId(libs.plugins.jetbrains.compose)
    aliasId(libs.plugins.ktlint)
}

android {
    namespace = "dev.trolle.af.wingman.android"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "dev.trolle.af.wingman.android"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.compileSdk.get().toInt()
        versionCode = libs.versions.buildVersion.get().toInt()
        versionName = libs.versions.appVersion.get()
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            // FIX-Me https://github.com/Kotlin/kotlinx.coroutines/issues/3668
            excludes += "META-INF/versions/9/previous-compilation-data.bin"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }
}

dependencies {
    implementation(projects.shared)
    implementation(compose.uiTooling)
}
