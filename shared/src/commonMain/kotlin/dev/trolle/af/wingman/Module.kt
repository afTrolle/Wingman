package dev.trolle.af.wingman

import Wingman.library.common.BuildConfig
import androidx.compose.runtime.Composable
import dev.trolle.wingman.ai.aiModule
import dev.trolle.wingman.db.databaseModule
import dev.trolle.wingman.home.homeModule
import dev.trolle.wingman.signin.signInModule
import dev.trolle.wingman.ui.Navigation
import dev.trolle.wingman.ui.uiModule
import dev.trolle.wingman.user.userModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

internal val sharedModule: Module = module {
    single {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

    single(createdAtStart = true) {
        if (BuildConfig.LOGGING_ENABLED) {
            Napier.base(DebugAntilog())
        }
    }
    single { NavigationImpl() }.bind<Navigation>()
}

internal val appModule =
    listOf(
        uiModule,
        sharedModule,
        aiModule,
        databaseModule,
        userModule,
        signInModule,
        homeModule,
    )

@Composable
internal expect fun buildKoinAppDeclaration(): KoinAppDeclaration
