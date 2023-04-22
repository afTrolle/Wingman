package dev.trolle.af.wingman.service

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

internal interface NavigationService {
    suspend fun setNavigator(navigator: Navigator)
    suspend fun open(screen: Screen)
    suspend fun pop()
    suspend fun replaceAll(screen: Screen)
}

@Composable
internal fun NavigationService.Register(
    navigator: Navigator,
) {
    LaunchedEffect(navigator) {
        setNavigator(navigator)
    }
}

internal fun navigationService() = object : NavigationService {

    private val navigatorState = MutableSharedFlow<Navigator>(
        replay = 1,
        onBufferOverflow = DROP_OLDEST,
    )

    // Caution might have race-conditions calling navigate multiple times on the same navigator object.
    // TODO check underlying implementation if this is an issue.
    override suspend fun setNavigator(navigator: Navigator) {
        navigatorState.emit(navigator)
    }

    override suspend fun open(screen: Screen) {
        navigatorState.first().push(screen)
    }

    override suspend fun replaceAll(screen: Screen) {
        navigatorState.first().replaceAll(screen)
    }

    override suspend fun pop() {
        navigatorState.first().pop()
    }
}
