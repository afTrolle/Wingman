package dev.trolle.af.wingman.koin


import dev.trolle.af.wingman.repository.userRepository
import dev.trolle.af.wingman.screen.HomeScreenModel
import dev.trolle.af.wingman.screen.SignInScreen
import dev.trolle.af.wingman.service.openAIService
import dev.trolle.af.wingman.service.persistenceService
import dev.trolle.af.wingman.service.phoneValidateService
import dev.trolle.af.wingman.service.tinderService
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val platformModule: Module

internal val sharedModule: Module = module {
    // Service (dependency-less)
    single { openAIService() }
    single { tinderService() }
    single { persistenceService() }
    single { phoneValidateService() }

    // Repository (concatenate multiple services)
    single { userRepository(get(), get()) }

    // Screen View Models
    factory { HomeScreenModel() }
    factory { SignInScreen.SignInScreenModel(get(), get()) }
}


val appModule get() = listOf(platformModule, sharedModule)
