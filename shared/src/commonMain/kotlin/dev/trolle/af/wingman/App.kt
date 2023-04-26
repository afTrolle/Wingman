package dev.trolle.af.wingman

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import dev.trolle.wingman.common.koin.KoinApplication
import dev.trolle.af.wingman.koin.buildKoinAppDeclaration
import dev.trolle.wingman.common.koin.rememberKoinInject
import dev.trolle.af.wingman.screen.HomeScreen
import dev.trolle.af.wingman.screen.SignInScreen
import dev.trolle.af.wingman.service.Navigation
import dev.trolle.af.wingman.service.PhoneNumberService
import dev.trolle.wingman.ui.UiBase
import dev.trolle.wingman.ui.di.UpdateStrings
import dev.trolle.wingman.user.User
import kotlinx.coroutines.runBlocking

@Composable
internal fun App() {
    AppPlatform {
        UiBase {
            KoinApplication(buildKoinAppDeclaration()) {
                UpdateStrings()
                // TODO figure out a better pattern for this.
                val phoneNumberService: PhoneNumberService = rememberKoinInject()
                phoneNumberService.Register()
                val startScreen = getStartScreen()
                Navigation(startScreen) {
                    CurrentScreen()
                }
            }
        }
    }
}

// Register or Provide platform specific services
@Composable
internal expect fun AppPlatform(content: @Composable () -> Unit)

@Composable
private fun getStartScreen(): Screen {
    val userRepository: User = rememberKoinInject()
    val startScreen =
        remember {
            runBlocking {
                if (userRepository.isUserSignedIn()) HomeScreen else SignInScreen
            }
        }
    return startScreen
}

