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
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            // FIX-Me https://github.com/Kotlin/kotlinx.coroutines/issues/3668
            excludes += "META-INF/versions/9/previous-compilation-data.bin"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(projects.shared)
    debugImplementation(compose.uiTooling)
    implementation(compose.material3)
    implementation(libs.material)
    implementation(libs.androidx.core.splashscreen)
//    implementation(libs.conscrypt.android) // used for proguard
    implementation(libs.slf4j.simple)
    implementation(libs.androidx.activity.compose)
}
