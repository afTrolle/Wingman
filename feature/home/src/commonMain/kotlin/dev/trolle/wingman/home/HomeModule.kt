package dev.trolle.wingman.home

import dev.trolle.wingman.home.compose.screen.home.HomeScreenModel
import org.koin.dsl.module

val homeModule = module {
    factory { HomeScreenModel(get(), get()) }
}
