package dev.trolle.wingman.user

import dev.trolle.wingman.user.tinder.tinder
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.dsl.module

@OptIn(ExperimentalSerializationApi::class)
val userModule = module {
    single { tinder(get(), get(),get()) }
    single { UserDatabase(get()) }
    single { user(get(), get(), get()) }
}
