package dev.trolle.af.wingman

import androidx.compose.runtime.Composable
import dev.trolle.wingman.ui.di.uiModule
import dev.trolle.wingman.ui.string.LocalStrings
import org.koin.dsl.KoinAppDeclaration

@Composable
internal actual fun buildKoinAppDeclaration(): KoinAppDeclaration {
    val strings = LocalStrings.current
    return {
        modules(appModule(uiModule(strings)))
    }
}
