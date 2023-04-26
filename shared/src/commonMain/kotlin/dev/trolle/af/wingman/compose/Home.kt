package dev.trolle.af.wingman.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
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
import dev.trolle.af.wingman.screen.HomeState
import dev.trolle.wingman.user.model.Match

@Composable
fun Home(
    state: HomeState,
) = Scaffold {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 32.dp), // TODO add systembar padding
    ) {
        items(
            count = state.matches.size,
            itemContent = { index ->
                val match = state.matches[index]
                Match(match)
            },
        )
    }
}

@Composable
fun Match(match: Match) {
    Column(
        Modifier.padding(vertical = 8.dp),
    ) {
        Row(
            Modifier.height(96.dp)
                .padding(horizontal = 8.dp),
        ) {
            Image(
                painter = rememberAsyncImagePainter(match.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
            )

            Column(Modifier.align(Alignment.Top)) {
                Text(
                    text = match.name,
                    style = typography.h4,
                    textAlign = TextAlign.Start,
                )
                Text(
                    text = match.age,
                    style = typography.h6,
                    textAlign = TextAlign.Start,
                )
            }
        }

        Box(Modifier.padding(horizontal = 8.dp)) {
            BasicTextField(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                value = match.opener,
                readOnly = true,
                onValueChange = { value: String -> },
                textStyle = typography.body2,
            )
        }
        Divider()
    }
}
