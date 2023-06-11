package dev.trolle.wingman.home.compose.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import dev.trolle.wingman.ui.LocalWindowSizeClass
import dev.trolle.wingman.ui.MaterialThemeWingman
import dev.trolle.wingman.ui.compose.Pane
import dev.trolle.wingman.ui.ext.statusBarsPadding
import dev.trolle.wingman.ui.ext.systemBarsPadding
import dev.trolle.wingman.ui.string.Strings

object HomeScreen : Screen {

    override val key: ScreenKey = "MainHomeScreen"

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        TabNavigator(HomeTab) {
            Scaffold(
                containerColor = MaterialThemeWingman.colorScheme.surface,
                contentWindowInsets = WindowInsets(0.dp),
                content = { paddingValues ->
                    val size = LocalWindowSizeClass.current.widthSizeClass
                    Row {
                        if (size != WindowWidthSizeClass.Compact) {
                            NavigationRail() {
                                RailNavigationItem(
                                    true,
                                    HomeTab,
                                    Strings.home_tab,
                                    Icons.Default.Home,
                                )
                                RailNavigationItem(
                                    false,
                                    BioTab,
                                    Strings.bio_tab,
                                    Icons.Default.Description,
                                )
                                RailNavigationItem(
                                    true,
                                    ProfileTab,
                                    Strings.profile_tab,
                                    Icons.Default.Person,
                                )
                            }
                        }
                        Pane(
                            modifier = Modifier.statusBarsPadding(),
                            cutout = true,
                            innerModifier = Modifier,
                            shape =  MaterialThemeWingman.shapes.extraLarge.copy( bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp), topEnd =  CornerSize(0.dp)),
                        ) {
                            CompositionLocalProvider(LocalTabPaddingValues provides paddingValues) {
                                CurrentTab()
                            }
                        }
                    }
                },
                bottomBar = {
                    val size = LocalWindowSizeClass.current
                    if (size.widthSizeClass == WindowWidthSizeClass.Compact)
                        BottomAppBar(
                            tonalElevation = 3.dp,
                        ) {
                            TabNavigationItem(true, HomeTab, Strings.home_tab, Icons.Default.Home)
                            TabNavigationItem(
                                false,
                                BioTab,
                                Strings.bio_tab,
                                Icons.Default.Description,
                            )
                            TabNavigationItem(
                                true,
                                ProfileTab,
                                Strings.profile_tab,
                                Icons.Default.Person,
                            )
                        }
                },
            )
        }
    }
}

val LocalTabPaddingValues = compositionLocalOf { PaddingValues() }

@Composable
private fun RowScope.TabNavigationItem(
    enabled: Boolean,
    tab: Tab,
    name: String,
    icon: ImageVector,
) {
    val tabNavigator = LocalTabNavigator.current
    val isCurrentTab by remember(tabNavigator) {
        derivedStateOf { tabNavigator.current == tab }
    }
    val painter = rememberVectorPainter(icon)
    val unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant.let { color ->
        if (enabled) color else color.copy(DisabledAlpha)
    }

    NavigationBarItem(
        enabled = enabled,
        selected = isCurrentTab,
        onClick = { tabNavigator.current = tab },
        icon = { Icon(painter = painter, contentDescription = name) },
        alwaysShowLabel = false,
        label = { Text(name) },
        colors = NavigationBarItemDefaults.colors(
            unselectedIconColor = unselectedContentColor,
            unselectedTextColor = unselectedContentColor,
        ),
    )
}


@Composable
private fun RailNavigationItem(
    enabled: Boolean,
    tab: Tab,
    name: String,
    icon: ImageVector,
) {
    val tabNavigator = LocalTabNavigator.current
    val isCurrentTab by remember(tabNavigator) {
        derivedStateOf { tabNavigator.current == tab }
    }
    val painter = rememberVectorPainter(icon)
    val unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant.let { color ->
        if (enabled) color else color.copy(DisabledAlpha)
    }

    NavigationRailItem(
        enabled = enabled,
        selected = isCurrentTab,
        onClick = { tabNavigator.current = tab },
        icon = { Icon(painter = painter, contentDescription = name) },
        alwaysShowLabel = false,
        label = { Text(name) },
        colors = NavigationRailItemDefaults.colors(
            unselectedIconColor = unselectedContentColor,
            unselectedTextColor = unselectedContentColor,
        ),
    )
}

internal const val DisabledAlpha = 0.38f
