package dev.trolle.wingman.home.preview


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.trolle.wingman.home.compose.screen.prompt.PromptEntry
import dev.trolle.wingman.home.compose.screen.prompt.PromptState
import dev.trolle.wingman.ui.compose.PreviewDefaults

private const val randomPersonIcon = "https://cdnstorage.sendbig.com/unreal/female.webp"
private val mockData = PromptState(
    entries = listOf(
        PromptEntry.FromMatch(message = "hej", icon = randomPersonIcon),
        PromptEntry.ToMatch(message = "Hej"),
        PromptEntry.FromMatch(message = "Hur är läget?!", icon = randomPersonIcon),
    ),
    promptField = "nice and cool prompt.",
)

@Preview
@Composable
private fun PreviewPrompt() = PreviewDefaults {

}

@Preview
@Composable
private fun PreviewInputField() = PreviewDefaults {
    PromptField("partial input")

}

@Preview(backgroundColor = 0)
@Composable
private fun PreviewInputFieldLongInput() = PreviewDefaults {

    PromptField("partial inputpartial inputpartial inputpartial inputpartial inputpartial inputpartial inputpartial input")

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Prompt(
    state: PromptState,
    onPromptChange: (String) -> Unit = {},
    onPromptSend: () -> Unit = {},
) = Scaffold { padding ->
    Column(
        Modifier.padding(padding),
    ) {
        ChatHistory(state.entries)
        PromptField(
            state.promptField,
            onPromptChange,
            onPromptSend,
        )
    }
}

@Composable
fun ChatHistory(
    entries: List<PromptEntry>,
    onEditAiEntry: (PromptEntry.ToAi) -> Unit = { },
) {
    LazyColumn {
        items(entries) {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromptField(
    text: String = "",
    onValueChange: (String) -> Unit = {},
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    Card(modifier) {
        OutlinedTextField(
            modifier = Modifier
                .padding(8.dp)
            ,
            value = text,
            shape = RoundedCornerShape(8.dp),
            onValueChange = onValueChange,
            trailingIcon = {
                TextButton(onClick) { Text("generate") }
            },
        )
    }
}
