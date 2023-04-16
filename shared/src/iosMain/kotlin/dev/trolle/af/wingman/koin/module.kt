package dev.trolle.af.wingman.koin

import androidx.compose.runtime.Composable
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

actual val platformModule: Module = module {

    single<FlowSettings> {
        NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults).toFlowSettings()
    }

}

@Composable
actual fun BuildKoinAppDeclaration(): KoinAppDeclaration = {
    modules(appModule)
}