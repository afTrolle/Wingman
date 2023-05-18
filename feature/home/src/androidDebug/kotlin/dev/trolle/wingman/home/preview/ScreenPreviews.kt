package dev.trolle.wingman.home.preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.trolle.wingman.home.compose.MatchItem
import dev.trolle.wingman.home.compose.screen.home.MatchItem
import dev.trolle.wingman.ui.compose.PreviewDefaults

private val item = MatchItem(
    id = "1",
    name = "jon",
    age = 69,
    latestMessage = "hello",
    image = "https://cdnstorage.sendbig.com/unreal/female.webp",
    latestMessageReceived = true,
)

@Preview(
    showBackground = true,
)
@Composable
fun MatchItemPreview() = PreviewDefaults {
    Surface {
        MatchItem(item, Modifier.padding(16.dp))
    }
}

@Preview
@Composable
fun MatchItemLoadingPreview() = PreviewDefaults {
    Surface {
        MatchItem(null, Modifier.padding(16.dp))
    }
}
