package dev.trolle.af.wingman.koin

import Wingman.shared.BuildConfig
import androidx.compose.runtime.Composable
import dev.trolle.af.wingman.screen.HomeScreenModel
import dev.trolle.af.wingman.screen.OneTimePasswordModel
import dev.trolle.af.wingman.screen.SignInScreenModel
import dev.trolle.af.wingman.service.NavigationImpl
import dev.trolle.af.wingman.service.phoneValidateService
import dev.trolle.wingman.ai.aiModule
import dev.trolle.wingman.db.databaseModule
import dev.trolle.wingman.ui.Navigation
import dev.trolle.wingman.user.userModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

// Consider changing this into shared module and depend on each item to minimize implicit providers.
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
        if (BuildConfig.LOGGING_ENABLED) {
            Napier.base(DebugAntilog())
        }
    }

    single { NavigationImpl() }.bind<Navigation>()

    single { phoneValidateService() }

    // Screen View Models
    factory { HomeScreenModel(get()) }
    factory { SignInScreenModel(get(), get(), get(), get(), get()) }
    factory { params -> OneTimePasswordModel(params.get(), get(), get()) }
}


internal fun appModule(uiModule: Module) =
    listOf(uiModule, platformModule, sharedModule, aiModule, databaseModule, userModule)

@Composable
internal expect fun buildKoinAppDeclaration(): KoinAppDeclaration
