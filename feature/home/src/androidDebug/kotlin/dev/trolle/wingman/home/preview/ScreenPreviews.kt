package dev.trolle.wingman.home.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.trolle.wingman.home.HomeState
import dev.trolle.wingman.home.compose.Home
import dev.trolle.wingman.ui.compose.PreviewDefaults
import dev.trolle.wingman.user.model.Match

@Preview
@Composable
fun HomePreview() = PreviewDefaults {
    val testState = HomeState(
        listOf(
            Match(
                "jon",
                "29",
                "https://qodebrisbane.com/wp-content/uploads/2019/07/This-is-not-a-person-10-1.jpeg",
                "Hi there! I came across your profile and I have to say, I was intrigued. Your [insert something interesting you noticed about their profile] really caught my attention. I would love to chat and get to know you better. What are some of your favorite hobbies or interests?",
            ),
            Match(
                "jon",
                "29",
                "https://qodebrisbane.com/wp-content/uploads/2019/07/This-is-not-a-person-10-1.jpeg",
                "Hi there! I came across your profile and I have to say, I was intrigued. Your [insert something interesting you noticed about their profile] really caught my attention. I would love to chat and get to know you better. What are some of your favorite hobbies or interests?",
            ),
            Match(
                "jon",
                "29",
                "https://qodebrisbane.com/wp-content/uploads/2019/07/This-is-not-a-person-10-1.jpeg",
                "Hi there! I came across your profile and I have to say, I was intrigued. Your [insert something interesting you noticed about their profile] really caught my attention. I would love to chat and get to know you better. What are some of your favorite hobbies or interests?",
            ),
        ),
    )
    Home(state = testState)
}
