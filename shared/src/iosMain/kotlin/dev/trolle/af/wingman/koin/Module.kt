package dev.trolle.af.wingman.koin

import androidx.compose.runtime.Composable
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import dev.trolle.af.wingman.service.phoneNumberService
import dev.trolle.wingman.ui.di.uiModule
import dev.trolle.wingman.ui.string.LocalStrings
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

internal actual val platformModule: Module = module {

    single<FlowSettings> {
        NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults).toFlowSettings()
    }

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
