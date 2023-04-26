package dev.trolle.af.wingman.koin

import androidx.compose.runtime.Composable
import dev.trolle.af.wingman.service.phoneNumberService
import dev.trolle.wingman.ui.di.uiModule
import dev.trolle.wingman.ui.string.LocalStrings
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

internal actual val platformModule: Module = module {

    single { phoneNumberService() }
}

@Composable
internal actual fun buildKoinAppDeclaration(): KoinAppDeclaration {
    val strings = LocalStrings.current
    return {
        modules(
            appModule(
                uiModule = uiModule(strings),
            ),
        )
    }
}
