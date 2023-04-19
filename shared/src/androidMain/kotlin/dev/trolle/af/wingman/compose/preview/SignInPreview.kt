package dev.trolle.af.wingman.compose.local.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import dev.trolle.af.wingman.compose.Home
import dev.trolle.af.wingman.compose.OneTimePassword
import dev.trolle.af.wingman.compose.SignIn
import dev.trolle.af.wingman.screen.HomeState
import dev.trolle.af.wingman.screen.Match
import dev.trolle.af.wingman.screen.OneTimePasswordState
import dev.trolle.af.wingman.screen.SignInState

@Preview
@Composable
fun SignInPreview() = PreviewDefaults {
    val (text, setText) = remember { mutableStateOf("") }
    SignIn(
        state = SignInState(
            phoneNumber = text,
            filterInteraction = false,
        ),
        onPhoneNumberChange = setText,
    )
}

@Preview
@Composable
fun OneTimePasswordPreview() = PreviewDefaults {
    val (oneTimePassword, setOneTimePassword) = remember {
        mutableStateOf("")
    }
    val state = OneTimePasswordState(
        phoneNumber = "+46720208",
        oneTimePassword = oneTimePassword,
    )
    OneTimePassword(
        oneTimePasswordState = state,
        onOneTimePasswordChanged = setOneTimePassword
    )
}

@Preview
@Composable
fun HomePreview() = PreviewDefaults {
    val testState = HomeState(
        listOf(
            Match(
                "jon",
                "29",
                "https://qodebrisbane.com/wp-content/uploads/2019/07/This-is-not-a-person-10-1.jpeg",
                "Hi there! I came across your profile and I have to say, I was intrigued. Your [insert something interesting you noticed about their profile] really caught my attention. I would love to chat and get to know you better. What are some of your favorite hobbies or interests?"
            ),
            Match(
                "jon",
                "29",
                "https://qodebrisbane.com/wp-content/uploads/2019/07/This-is-not-a-person-10-1.jpeg",
                "Hi there! I came across your profile and I have to say, I was intrigued. Your [insert something interesting you noticed about their profile] really caught my attention. I would love to chat and get to know you better. What are some of your favorite hobbies or interests?"
            ),
            Match(
                "jon",
                "29",
                "https://qodebrisbane.com/wp-content/uploads/2019/07/This-is-not-a-person-10-1.jpeg",
                "Hi there! I came across your profile and I have to say, I was intrigued. Your [insert something interesting you noticed about their profile] really caught my attention. I would love to chat and get to know you better. What are some of your favorite hobbies or interests?"
            )
        )
    )
    Home(state = testState)
}