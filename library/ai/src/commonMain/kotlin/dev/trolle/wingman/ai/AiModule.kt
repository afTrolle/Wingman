package dev.trolle.wingman.ai

import Wingman.library.common.BuildConfig
import org.koin.dsl.module

val aiModule = module {
    single { ai(BuildConfig.OPEN_API_TOKEN) }
}
