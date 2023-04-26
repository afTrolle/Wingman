package dev.trolle.wingman.home

import org.koin.dsl.module

val homeModule = module {

    factory { HomeScreenModel(get()) }
}
