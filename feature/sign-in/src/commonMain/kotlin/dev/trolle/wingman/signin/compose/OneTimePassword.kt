@file:OptIn(ExperimentalLayoutApi::class)

package dev.trolle.wingman.signin.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.trolle.wingman.signin.OneTimePasswordState
import dev.trolle.wingman.ui.compose.Pane
import dev.trolle.wingman.ui.MaterialThemeWingman
import dev.trolle.wingman.ui.compose.BigButton
import dev.trolle.wingman.ui.ext.imePadding
import dev.trolle.wingman.ui.string.Strings
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun OneTimePassword(
    oneTimePasswordState: OneTimePasswordState,
    onOneTimePasswordChanged: (String) -> Unit = {},
    onSignIn: () -> Unit = {},
) = Scaffold(
    containerColor = MaterialThemeWingman.colorScheme.secondaryContainer,
    contentColor = MaterialThemeWingman.colorScheme.onSurface,
) { paddingValues ->
    Pane(
        Modifier.padding(paddingValues)
    ) {
        val listState = rememberLazyListState()
        val density = LocalDensity.current
        var insets by remember { mutableStateOf(0.dp) }
        val scope = rememberCoroutineScope()
        val hasFocus = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.fillMaxHeight()
                .onGloballyPositioned { coordinates ->
                    val root = coordinates.findRootCoordinates()
                    val rootBounds = coordinates.boundsInRoot()
                    insets = with(density) {
                        (root.size.height - coordinates.positionInRoot().y - rootBounds.height).toDp()
                    }
                    if (hasFocus.value) {
                        scope.launch {
                            listState.animateScrollToItem(2)
                        }
                    }
                }.consumeWindowInsets(PaddingValues(bottom = insets))
                .imePadding()
                .widthIn(max = 360.dp),
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                state = listState,
            ) {
                item {
                    Text(
                        text = Strings.enterOneTimePassword,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge,
                    )
                    Spacer(Modifier.height(32.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = Strings.one_time_password_help,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(Modifier.height(32.dp))
                    OneTimePasswordInput(
                        modifier = Modifier.onFocusChanged {
                            hasFocus.value = it.isFocused
                        },
                        length = oneTimePasswordState.oneTimePasswordLength,
                        error = oneTimePasswordState.isOneTimePasswordError,
                        text = oneTimePasswordState.oneTimePassword,
                        onTextChanged = { onOneTimePasswordChanged(it.text) },
                    )
                    Text(
                        text = Strings.phoneNumberSentTo(oneTimePasswordState.phoneNumber),
                        modifier = Modifier.padding(top = 12.dp, bottom = 16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                    )
                }
                item {
                    Spacer(Modifier.height(10.dp))
                }
            }
            BigButton(
                Strings.button_sign_in,
                onSignIn,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
