package dev.trolle.af.wingman

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorContent
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.navigator.OnBackPressed
import dev.trolle.wingman.common.koin.rememberKoinInject
import dev.trolle.wingman.home.HomeScreen
import dev.trolle.wingman.ui.Navigation
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

internal class NavigationImpl : Navigation {

    private val navigatorState = MutableSharedFlow<Navigator>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    // Caution might have race-conditions calling navigate multiple times on the same navigator object.
    // Think of pressing to navigation's at the same time.
    @Composable
    internal fun setNavigator(navigator: Navigator) {
        LaunchedEffect(navigator) {
            navigatorState.emit(navigator)
        }
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

    override suspend fun openHomeScreen() {
        replaceAll(HomeScreen)
    }
}

@Composable
internal fun Navigation(
    screen: Screen,
    disposeBehavior: NavigatorDisposeBehavior = NavigatorDisposeBehavior(),
    onBackPressed: OnBackPressed = { true },
    content: NavigatorContent = { },
) {
    val navigation: NavigationImpl = rememberKoinInject()
    Navigator(
        screen,
        disposeBehavior,
        onBackPressed,
    ) { navigator ->
        // Store  navigator in navigation Service so screenModels can change screen
        navigation.setNavigator(navigator)
        // draw current Screen
        content(navigator)
    }
}
