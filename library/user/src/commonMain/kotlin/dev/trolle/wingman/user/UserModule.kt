package dev.trolle.wingman.user

import dev.trolle.wingman.user.tinder.tinder
import org.koin.dsl.module

val userModule = module {
    single { tinder(get()) }
    single { UserDatabase(get()) }
    single { user(get(), get(), get()) }
}