package dev.trolle.af.wingman.koin

import androidx.compose.runtime.Composable
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import dev.trolle.af.wingman.service.phoneNumberService
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
internal actual fun buildKoinAppDeclaration(): KoinAppDeclaration = {
    modules(appModule)
}
