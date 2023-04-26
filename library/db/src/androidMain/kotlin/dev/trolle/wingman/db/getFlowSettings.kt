package dev.trolle.wingman.db

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.datastore.DataStoreSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

private val Context.dataStore by preferencesDataStore("settings")

internal actual fun Scope.getFlowSettings(): FlowSettings {
    val dateStore = androidContext().dataStore
    @OptIn(ExperimentalSettingsImplementation::class)
    return DataStoreSettings(dateStore)
}