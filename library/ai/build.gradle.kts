plugins {
    `library-plugin`
}

kotlin {
    sourceSets {
        all {
            languageSettings {
                optIn("com.aallam.openai.api.BetaOpenAI")
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(libs.openai.client)
            }
        }
    }
}
