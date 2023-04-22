@file:JvmName("ModuleKtJvm")

package dev.trolle.af.wingman.koin

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.preferencesDataStore
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.datastore.DataStoreSettings
import dev.trolle.af.wingman.service.phoneNumberService
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

private val Context.dataStore by preferencesDataStore("settings")

// Platform specific module.
internal actual val platformModule: Module = module {

    single<FlowSettings> {
        // Use android data store as underlying persistence
        val dateStore = androidContext().dataStore
        @OptIn(ExperimentalSettingsImplementation::class)
        DataStoreSettings(dateStore)
    }

    single { phoneNumberService(get()) }
}

@Composable
internal actual fun buildKoinAppDeclaration(): KoinAppDeclaration {
    val context = LocalContext.current.applicationContext
    return {
        modules(appModule)
        androidContext(context)
    }
}
