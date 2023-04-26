package dev.trolle.wingman.db

import org.koin.dsl.module

val databaseModule = module {
    single { getFlowSettings() }
    single { db(get(), get()) }
}
