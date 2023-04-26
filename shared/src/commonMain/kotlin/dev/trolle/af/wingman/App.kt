package dev.trolle.af.wingman

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import dev.trolle.wingman.common.koin.KoinApplication
import dev.trolle.wingman.common.koin.rememberKoinInject
import dev.trolle.wingman.home.HomeScreen
import dev.trolle.wingman.sign.`in`.SignInScreen
import dev.trolle.wingman.sign.`in`.service.InitPhoneNumberService
import dev.trolle.wingman.ui.UiBase
import dev.trolle.wingman.ui.di.UpdateStrings
import dev.trolle.wingman.user.User

@Composable
internal fun App() {
    UiBase {
        KoinApplication(buildKoinAppDeclaration()) {
            // TODO figure out a better pattern for this.
            UpdateStrings()
            InitPhoneNumberService()
            Navigation(startScreen) {
                CurrentScreen()
            }
        }
    }
}

private val startScreen: Screen
    @Composable
    get() {
        val userRepository: User = rememberKoinInject()
        return remember {
            if (userRepository.isUserSignedIn) HomeScreen else SignInScreen
        }
    }
