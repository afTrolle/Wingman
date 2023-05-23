@file:JvmName("ModuleKtJvm")

package dev.trolle.af.wingman

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dev.trolle.wingman.ui.string.LocalStrings
import dev.trolle.wingman.ui.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.KoinAppDeclaration

@Composable
internal actual fun buildKoinAppDeclaration(): KoinAppDeclaration {
    val context: Context = LocalContext.current.applicationContext
    return remember(context) {
        {
            androidContext(context)
            modules(appModule)
        }
    }
}
