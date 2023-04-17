package dev.trolle.af.wingman.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberAsyncImagePainter
import dev.trolle.af.wingman.compose.local.systemBarsPadding
import dev.trolle.af.wingman.screen.HomeState
import dev.trolle.af.wingman.screen.Match

@Composable
fun Home(
    state: HomeState,
) = Scaffold {
    LazyColumn(
        modifier = Modifier.systemBarsPadding(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(
            count = state.matches.size,
            itemContent = { index ->
                val match = state.matches[index]
                Match(match)
            })

    }
}

@Composable
fun Match(match: Match) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        elevation = 2.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Row(
                Modifier.height(96.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(match.imageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(8.dp),
                    text = match.name,
                    style = typography.h4,
                    textAlign = TextAlign.Start
                )
            }

            Box(
                modifier = Modifier
            ) {
                Text(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                    text = match.opener,
                    style = typography.body2
                )
            }
        }
    }

}
