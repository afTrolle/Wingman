@file:OptIn(ExperimentalLayoutApi::class)

package dev.trolle.wingman.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.trolle.wingman.ui.LocalWindowSizeClass
import dev.trolle.wingman.ui.MaterialThemeWingman
import dev.trolle.wingman.ui.ext.imePadding
import dev.trolle.wingman.ui.ext.painterResource
import dev.trolle.wingman.ui.string.Strings

@Composable
fun Logo(
    modifier: Modifier = Modifier.size(175.dp),
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource("drawable/logo.png"),
            contentDescription = Strings.logo_desc,
            modifier = modifier,
        )
        Text(
            text = Strings.app_name,
            style = MaterialThemeWingman.typography.displayMedium,
            color = MaterialThemeWingman.colorScheme.tertiary,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignIn(
    text: String = "",
    isError: Boolean? = null,
    captureFocus: Boolean = true,
    requestFocus: Boolean = false,
    isSignInEnabled: Boolean = true,
    onChange: (String) -> Unit = {},
    onSignIn: () -> Unit = {},
    onFocusCaptured: () -> Unit = { },
) = Scaffold(
    containerColor = MaterialThemeWingman.colorScheme.secondaryContainer,
    contentColor = MaterialThemeWingman.colorScheme.onSurface,
) {
    val requestFocusModifier = Modifier.requestFocus(requestFocus)
    LayoutOnDifferentHeight(
        upTo = {
            Pane(Modifier.padding(it).padding(horizontal = 16.dp, vertical = 16.dp)) {
                SignInLimitedHeight(
                    text = text,
                    isError = isError,
                    captureFocus = captureFocus,
                    isSignInEnabled = isSignInEnabled,
                    onChange = onChange,
                    onSignIn = onSignIn,
                    onFocusCaptured = onFocusCaptured,
                    requestFocusModifier = requestFocusModifier,
                )
            }
        },
        over = {
            Pane(Modifier.padding(it).padding(horizontal = 16.dp, vertical = 8.dp)) {
                SignInNotLimitedHeight(
                    text = text,
                    isError = isError,
                    captureFocus = captureFocus,
                    isSignInEnabled = isSignInEnabled,
                    onChange = onChange,
                    onSignIn = onSignIn,
                    onFocusCaptured = onFocusCaptured,
                    requestFocusModifier = requestFocusModifier,
                )
            }
        },
    )
}

private val paneModifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
private val innerPaneModifier = Modifier.fillMaxSize().padding(16.dp)

@Composable
fun Pane(
    modifier: Modifier = paneModifier,
    innerModifier: Modifier = innerPaneModifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        shape = MaterialThemeWingman.shapes.extraLarge,
        color = MaterialThemeWingman.colorScheme.surface,
        modifier = modifier,
        content = {
            Box(
                innerModifier,
                contentAlignment = Alignment.Center,
            ) {
                content()
            }
        },
    )
}

@Composable
fun SignInLimitedHeight(
    text: String = "",
    isError: Boolean? = null,
    captureFocus: Boolean = true,
    isSignInEnabled: Boolean = true,
    onChange: (String) -> Unit = {},
    onSignIn: () -> Unit = {},
    onFocusCaptured: () -> Unit = { },
    requestFocusModifier: Modifier,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    Row(
        Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Logo(Modifier.size(150.dp))
        }
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Login(
                modifier = Modifier.widthIn(max = 500.dp).imePadding(),
                text = text,
                isError = isError,
                isSignInEnabled = isSignInEnabled,
                captureFocus = captureFocus,
                onChange = onChange,
                onSignIn = onSignIn,
                onFocusCaptured = onFocusCaptured,
                requestFocusModifier = requestFocusModifier,
            )
        }
    }
    Disclaimer()
}

@Composable
fun SignInNotLimitedHeight(
    text: String = "",
    isError: Boolean? = null,
    captureFocus: Boolean = true,
    isSignInEnabled: Boolean = true,
    onChange: (String) -> Unit = {},
    onSignIn: () -> Unit = {},
    onFocusCaptured: () -> Unit = { },
    requestFocusModifier: Modifier,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceAround,
) {
    val spacerModifier = Modifier.weight(1f)
    Spacer(modifier = spacerModifier)
    Logo()
    Spacer(modifier = spacerModifier)
    Login(
        modifier = Modifier.widthIn(max = 350.dp),
        text = text,
        isError = isError,
        isSignInEnabled = isSignInEnabled,
        captureFocus = captureFocus,
        onChange = onChange,
        onSignIn = onSignIn,
        onFocusCaptured = onFocusCaptured,
        requestFocusModifier = requestFocusModifier,
    )
    Spacer(modifier = spacerModifier)
    Disclaimer(Modifier.padding(bottom = 16.dp))
}

@Composable
fun LayoutOnDifferentHeight(
    upTo: @Composable () -> Unit,
    over: @Composable () -> Unit,
    heightSizeClass: WindowHeightSizeClass = WindowHeightSizeClass.Compact,
) {
    val currentHeightClass = LocalWindowSizeClass.current.heightSizeClass
    if (currentHeightClass <= heightSizeClass) {
        upTo()
    } else {
        over()
    }
}

@Composable
fun Disclaimer(
    modifier: Modifier = Modifier,
) = Text(
    modifier = modifier,
    text = Strings.disclaimer,
    style = MaterialThemeWingman.typography.labelMedium,
    textAlign = TextAlign.Center,
    color = MaterialThemeWingman.colorScheme.tertiary,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    modifier: Modifier = Modifier,
    text: String = "",
    isError: Boolean? = null,
    isSignInEnabled: Boolean = true,
    captureFocus: Boolean = true,
    onChange: (String) -> Unit = {},
    onSignIn: () -> Unit = {},
    onFocusCaptured: () -> Unit = { },
    requestFocusModifier: Modifier,
) = Column(
    modifier,
    verticalArrangement = Arrangement.spacedBy(12.dp),
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth().captureFocus(captureFocus, onFocusCaptured)
            .then(requestFocusModifier),
        value = text,
        onValueChange = onChange,
        isError = isError ?: false,
        label = { Text(Strings.phone_number_label) },
        placeholder = { Text(Strings.phone_number_placeholder) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Phone,
        ),
        shape = MaterialThemeWingman.shapes.large,
    )
    FilledTonalButton(
        modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 52.dp),
        onClick = onSignIn,
        enabled = isSignInEnabled,
        shape = MaterialThemeWingman.shapes.large,
    ) {
        Text(text = Strings.button_sign_in)
    }
}

