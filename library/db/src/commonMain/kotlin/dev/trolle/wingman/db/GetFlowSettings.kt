
package dev.trolle.wingman.db

import com.russhwolf.settings.coroutines.FlowSettings
import org.koin.core.scope.Scope


internal expect fun Scope.getFlowSettings(): FlowSettings