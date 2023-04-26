package dev.trolle.wingman.db

import com.russhwolf.settings.coroutines.FlowSettings
import org.koin.core.scope.Scope
import org.koin.dsl.module

val databaseModule = module {
    single { getFlowSettings() }
    single { db(get(), get()) }
}

internal expect fun Scope.getFlowSettings() : FlowSettings