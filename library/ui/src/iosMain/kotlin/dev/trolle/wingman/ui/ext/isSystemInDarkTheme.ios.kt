package dev.trolle.wingman.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.interop.LocalUIViewController
import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.readValue
import kotlinx.coroutines.delay
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UITraitCollection
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.currentTraitCollection

// From https://github.com/JetBrains/compose-multiplatform/issues/3213
@Composable
actual fun isSystemInDarkTheme(): Boolean {
    var style: UIUserInterfaceStyle by remember {
        mutableStateOf(UITraitCollection.currentTraitCollection.userInterfaceStyle)
    }

    val viewController: UIViewController = LocalUIViewController.current

    DisposableEffect(Unit) {
        val view: UIView = viewController.view
        val traitView = TraitView { trait : UIUserInterfaceStyle ->
            style = trait
        }
        view.addSubview(traitView)

        onDispose {
            traitView.removeFromSuperview()
        }
    }

    return style == UIUserInterfaceStyle.UIUserInterfaceStyleDark
}

@ExportObjCClass
private class TraitView(
    private val onTraitChanged: (UIUserInterfaceStyle) -> Unit,
) : UIView(frame = CGRectZero.readValue()) {
    override fun traitCollectionDidChange(previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)
        onTraitChanged(traitCollection.userInterfaceStyle)
    }
}