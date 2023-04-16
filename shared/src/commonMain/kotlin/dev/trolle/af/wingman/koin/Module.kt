package dev.trolle.af.wingman.koin


import dev.trolle.af.wingman.ext.isDebug
import dev.trolle.af.wingman.navigationService
import dev.trolle.af.wingman.repository.userRepository
import dev.trolle.af.wingman.screen.HomeScreenModel
import dev.trolle.af.wingman.screen.OneTimePasswordScreen
import dev.trolle.af.wingman.screen.SignInScreen
import dev.trolle.af.wingman.service.openAIService
import dev.trolle.af.wingman.service.persistenceService
import dev.trolle.af.wingman.service.phoneValidateService
import dev.trolle.app.service.tinder.tinderService
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

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
    single { openAIService() }
    single { navigationService() }
    single { tinderService(get()) }
    single { persistenceService() }
    single { phoneValidateService() }

    // Repository (concatenate multiple services)
    single { userRepository(get(), get()) }

    // Screen View Models
    factory { HomeScreenModel() }
    factory { SignInScreen.SignInScreenModel(get(), get(), get()) }
    factory { params -> OneTimePasswordScreen.OneTimePasswordModel(params.get()) }
}


val appModule get() = listOf(platformModule, sharedModule)
