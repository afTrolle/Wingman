package dev.trolle.wingman.ui.ext

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import dev.trolle.wingman.common.koin.getKoin
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

/*
Copied from: https://github.com/adrielcafe/voyager/blob/main/voyager-koin/src/main/java/cafe/adriel/voyager/koin/ScreenModel.kt
Remove when Voygaer supports iOS Target
 */

@Composable
inline fun <reified T : ScreenModel> Screen.getScreenModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    val koin = getKoin()
    return rememberScreenModel(tag = qualifier?.value) { koin.get(qualifier, parameters) }
}