package dev.trolle.wingman.home.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha.disabled
import androidx.compose.material.ContentAlpha.medium
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import dev.trolle.wingman.home.compose.custom.CustomBottomNavigation
import dev.trolle.wingman.ui.ext.navigationBarsPadding
import dev.trolle.wingman.ui.string.Strings

val LocalTabPaddingValues = compositionLocalOf { PaddingValues() }

@Composable
fun HomeBase() {
    TabNavigator(HomeTab) {
        Scaffold(
            content = { paddingValues ->
                CompositionLocalProvider(LocalTabPaddingValues provides paddingValues) {
                    CurrentTab()
                }
            },
            bottomBar = {
                CustomBottomNavigation(
                    rowModifier = Modifier.navigationBarsPadding(),
                ) {
                    TabNavigationItem(true, HomeTab, Strings.home_tab, Icons.Default.Home)
                    TabNavigationItem(false, BioTab, Strings.bio_tab, Icons.Default.Description)
                    TabNavigationItem(true, ProfileTab, Strings.profile_tab, Icons.Default.Person)
                }
            },
        )
    }
}

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
    val selectedContentColor = LocalContentColor.current
    val unselectedContentColor =
        selectedContentColor.copy(alpha = if (enabled) medium else disabled)
    BottomNavigationItem(
        enabled = enabled,
        selected = isCurrentTab,
        onClick = { tabNavigator.current = tab },
        icon = { Icon(painter = painter, contentDescription = name) },
        alwaysShowLabel = false,
        label = { Text(name) },
        selectedContentColor = selectedContentColor,
        unselectedContentColor = unselectedContentColor,
    )
}