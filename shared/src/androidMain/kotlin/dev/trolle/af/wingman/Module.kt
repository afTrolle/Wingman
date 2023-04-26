@file:JvmName("ModuleKtJvm")

package dev.trolle.af.wingman

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dev.trolle.wingman.ui.di.uiModule
import dev.trolle.wingman.ui.string.LocalStrings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.KoinAppDeclaration

@Composable
internal actual fun buildKoinAppDeclaration(): KoinAppDeclaration {
    val context: Context = LocalContext.current.applicationContext
    val strings = LocalStrings.current
    return remember(context) {
        {
            androidContext(context)
            modules(appModule(uiModule(strings)))
        }
    }
}
