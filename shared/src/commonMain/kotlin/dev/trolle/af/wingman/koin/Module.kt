package dev.trolle.af.wingman.koin


import Wingman.shared.BuildConfig
import androidx.compose.runtime.Composable
import dev.trolle.af.wingman.ext.isDebug
import dev.trolle.af.wingman.repository.userRepository
import dev.trolle.af.wingman.screen.HomeScreenModel
import dev.trolle.af.wingman.screen.OneTimePasswordModel
import dev.trolle.af.wingman.screen.SignInScreenModel
import dev.trolle.af.wingman.service.navigationService
import dev.trolle.af.wingman.service.openAIService
import dev.trolle.af.wingman.service.persistenceService
import dev.trolle.af.wingman.service.phoneValidateService
import dev.trolle.af.wingman.service.tinder.tinderService
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

// Consider chaning this into shared module and depend on each item to minimize implicit providers.
// Ie, something working on android might be missing on iOS
internal expect val platformModule: Module

internal val sharedModule: Module = module {
    // Externals (objects from libraries)
    single {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    }
    single(createdAtStart = true) {
        if (isDebug)
            Napier.base(DebugAntilog())
    }

    // Service (dependency-less)
    single {
        // Specified in build.gradle.kts
        val token = BuildConfig.OPEN_API_TOKEN
        openAIService(token)
    }
    single { navigationService() }
    single { tinderService(get()) }

    single { persistenceService(get(), get()) }
    single { phoneValidateService() }

    // Repository (concatenate multiple services)
    single { userRepository(get(), get(), get()) }

    // Screen View Models
    factory { HomeScreenModel(get()) }
    factory { SignInScreenModel(get(), get(), get(), get()) }
    factory { params -> OneTimePasswordModel(params.get(), get(), get()) }
}
internal val appModule get() = listOf(platformModule, sharedModule)

@Composable
expect fun buildKoinAppDeclaration(): KoinAppDeclaration
