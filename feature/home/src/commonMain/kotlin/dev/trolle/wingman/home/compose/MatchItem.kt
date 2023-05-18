package dev.trolle.wingman.home.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality.Companion.Medium
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.seiko.imageloader.rememberAsyncImagePainter
import dev.trolle.wingman.home.compose.screen.home.MatchItem
import dev.trolle.wingman.ui.MaterialThemeWingman

@Composable
fun MatchItem(
    item: MatchItem? = null,
    modifier: Modifier = Modifier,
) {
    val isVisible = item != null
    val highlight = PlaceholderHighlight.shimmer()
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val realImage = item?.image?.let { rememberAsyncImagePainter(it, filterQuality = Medium) }
        val placeholder = rememberVectorPainter(image = Icons.Default.Person)

        Image(
            painter = realImage ?: placeholder,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .placeholder(!isVisible, highlight = highlight),
        )
        Column(
            modifier = Modifier.padding(start = 8.dp)
                .weight(1f)
                .defaultMinSize(minHeight = 56.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(if (isVisible) 1f else 0.5f)
                    .placeholder(!isVisible, highlight = highlight),
                text = item?.name ?: "",
                style = MaterialThemeWingman.typography.h6,
                maxLines = 1,
            )
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(if (isVisible) 1f else 0.2f)
                        .placeholder(!isVisible, highlight = highlight),
                    text = item?.latestMessage ?: "",
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialThemeWingman.typography.subtitle2,
                    maxLines = 1,
                )
            }
        }

        if (item?.latestMessageReceived == true) {
            Box(Modifier.padding(start = 8.dp)) {
                val painter = rememberVectorPainter(Icons.Default.Mail)
                Icon(
                    modifier = Modifier
                        .size(22.dp)
                        .background(
                            MaterialThemeWingman.colors.secondary,
                            RoundedCornerShape(16.dp),
                        )
                        .padding(3.dp),
                    painter = painter,
                    contentDescription = null,
                    tint = contentColorFor(backgroundColor = MaterialThemeWingman.colors.secondary),
                )
            }
        }
    }
}