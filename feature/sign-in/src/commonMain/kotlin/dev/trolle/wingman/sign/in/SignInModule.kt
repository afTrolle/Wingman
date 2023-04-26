package dev.trolle.wingman.sign.`in`

import dev.trolle.wingman.sign.`in`.service.phoneNumberService
import dev.trolle.wingman.sign.`in`.service.phoneValidateService
import org.koin.dsl.module

val signInModule = module {
    single { phoneValidateService() }
    single { phoneNumberService() }

    // viewModels
    factory { SignInScreenModel(get(), get(), get(), get(), get()) }
    factory { params -> OneTimePasswordModel(params.get(), get(), get()) }
}
