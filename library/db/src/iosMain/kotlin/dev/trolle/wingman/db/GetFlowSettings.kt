package dev.trolle.wingman.db

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import org.koin.core.scope.Scope
import platform.Foundation.NSUserDefaults

internal actual fun Scope.getFlowSettings(): FlowSettings {
    return NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults).toFlowSettings()
}
