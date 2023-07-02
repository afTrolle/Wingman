package dev.trolle.af.wingman

import androidx.compose.runtime.Composable
import dev.trolle.wingman.ui.string.LocalStrings
import dev.trolle.wingman.ui.uiModule
import org.koin.dsl.KoinAppDeclaration

@Composable
internal actual fun buildKoinAppDeclaration(): KoinAppDeclaration {
    return {
        modules(appModule)
    }
}
