package dev.trolle.af.wingman

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import dev.trolle.af.wingman.compose.WingManTheme
import dev.trolle.af.wingman.koin.buildKoinAppDeclaration
import dev.trolle.af.wingman.koin.KoinApplication
import dev.trolle.af.wingman.koin.rememberKoinInject
import dev.trolle.af.wingman.repository.UserRepository
import dev.trolle.af.wingman.screen.HomeScreen
import dev.trolle.af.wingman.screen.SignInScreen
import dev.trolle.af.wingman.service.NavigationService
import dev.trolle.af.wingman.service.PhoneNumberService
import dev.trolle.af.wingman.service.Register
import kotlinx.coroutines.runBlocking

@Composable
fun App() {
    KoinApplication(buildKoinAppDeclaration()) {
        WingManTheme {
            val userRepository: UserRepository = rememberKoinInject()
            val phoneNumberService: PhoneNumberService = rememberKoinInject()
            phoneNumberService.Register()

            val startScreen =
                remember {
                    runBlocking {
                        if (userRepository.isUserSignedIn()) HomeScreen else SignInScreen
                    }
                }
            Navigator(startScreen) { navigator ->
                // Store  navigator in navigation Service so screenModels can change screen
                val navigationService: NavigationService = rememberKoinInject()
                navigationService.Register(navigator)
                // draw current Screen
                CurrentScreen()
            }
        }
    }
}