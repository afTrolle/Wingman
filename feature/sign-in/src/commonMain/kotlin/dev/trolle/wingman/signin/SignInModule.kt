package dev.trolle.wingman.signin

import dev.trolle.wingman.signin.service.phoneNumberService
import dev.trolle.wingman.signin.service.phoneValidateService
import org.koin.dsl.module

val signInModule = module {
    single { phoneValidateService() }
    single { phoneNumberService() }

    // viewModels
    factory { SignInScreenModel(get(), get(), get(), get(), get()) }
    factory { params -> OneTimePasswordModel(params.get(), get(), get()) }
}
