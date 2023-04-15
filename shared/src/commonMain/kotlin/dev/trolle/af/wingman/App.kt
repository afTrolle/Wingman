package dev.trolle.af.wingman

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import dev.trolle.af.wingman.compose.WingManTheme
import dev.trolle.af.wingman.compose.local.SetLocalPhoneNumberProvider
import dev.trolle.af.wingman.koin.KoinApplication
import dev.trolle.af.wingman.koin.appModule
import dev.trolle.af.wingman.koin.rememberKoinInject
import dev.trolle.af.wingman.repository.UserRepository
import dev.trolle.af.wingman.screen.HomeScreen
import dev.trolle.af.wingman.screen.SignInScreen

@Composable
fun App() {
    KoinApplication(::appModule) {
        WingManTheme {
            SetLocalPhoneNumberProvider {
                val userRepository: UserRepository = rememberKoinInject()
                val startScreen =
                    remember { if (userRepository.userIsSignedIn) HomeScreen else SignInScreen }
                Navigator(startScreen) { navigator ->
                    // Store  navigator in navigation Service so screenModels can change screen
                    val navigationService: NavigationService = rememberKoinInject()
                    LaunchedEffect(navigator) {
                        navigationService.setNavigator(navigator)
                    }
                    // draw current Screen
                    CurrentScreen()
                }
            }
        }
    }
}