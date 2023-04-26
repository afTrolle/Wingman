package dev.trolle.wingman.common.koin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import org.koin.core.Koin
import org.koin.core.annotation.KoinInternalApi
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.koinApplication
import org.koin.mp.KoinPlatformTools

/*
    Copied from : https://github.com/InsertKoinIO/koin/blob/main/compose/koin-compose/src/commonMain/kotlin/org/koin/compose/KoinApplication.kt
    Remove this when they have added support for ios target.
 */

val LocalKoinApplicationOrNull = compositionLocalOf { KoinPlatformTools.defaultContext().getOrNull() }

@OptIn(KoinInternalApi::class)
val LocalKoinScopeOrNull = compositionLocalOf { KoinPlatformTools.defaultContext().getOrNull()?.scopeRegistry?.rootScope }

/**
 * Current Koin Application context
 */
val LocalKoinApplication = compositionLocalOf { getKoinContext() }

/**
 * Current Koin Scope
 */
@OptIn(KoinInternalApi::class)
val LocalKoinScope = compositionLocalOf { getKoinContext().scopeRegistry.rootScope }
private fun getKoinContext() = KoinPlatformTools.defaultContext().get()

@Composable
fun getKoin(): Koin = LocalKoinApplication.current

/**
 * Start Koin Application from Compose
 *
 * @param application - Koin Application declaration lambda (like startKoin)
 * @param content - following compose function
 *
 * @author Arnaud Giuliani
 */
@OptIn(KoinInternalApi::class)
@Composable
fun KoinApplication(
    application: KoinAppDeclaration,
    content: @Composable () -> Unit,
) {
    // Added remember to not recreate koin on re-composition.
    // Guessing they didn't have it in the original because it's usually is declared first
    val koinApplication = remember { koinApplication(application) }
    CompositionLocalProvider(
        LocalKoinApplication provides koinApplication.koin,
        LocalKoinScope provides koinApplication.koin.scopeRegistry.rootScope,
    ) {
        content()
    }
}
