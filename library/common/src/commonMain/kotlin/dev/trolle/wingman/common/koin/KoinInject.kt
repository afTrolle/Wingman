package dev.trolle.wingman.common.koin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

/*
    Copied from : https://github.com/InsertKoinIO/koin/blob/main/compose/koin-compose/src/commonMain/kotlin/org/koin/compose/Inject.kt
    Remove this when they have added support for ios target.
 */

/**
 * Resolve Koin dependency
 *
 * @param qualifier
 * @param scope - Koin's root default
 * @param parameters - injected parameters
 *
 * @author Arnaud Giuliani
 */
@Composable
inline fun <reified T> koinInject(
    qualifier: Qualifier? = null,
    scope: Scope = LocalKoinScope.current,
    noinline parameters: ParametersDefinition? = null,
): T = rememberKoinInject(qualifier, scope, parameters)

/**
 * alias of koinInject()
 *
 * @see koinInject
 *
 * @author Arnaud Giuliani
 */
@Composable
inline fun <reified T> rememberKoinInject(
    qualifier: Qualifier? = null,
    scope: Scope = LocalKoinScope.current,
    noinline parameters: ParametersDefinition? = null,
): T = remember(qualifier, scope, parameters) {
    scope.get(qualifier, parameters)
}

@Composable
inline fun <reified T> rememberKoinInjectOrNull(
    qualifier: Qualifier? = null,
    scope: Scope? = LocalKoinScopeOrNull.current,
    noinline parameters: ParametersDefinition? = null,
): T? = remember(qualifier, scope, parameters) {
    scope?.getOrNull(qualifier, parameters)
}
