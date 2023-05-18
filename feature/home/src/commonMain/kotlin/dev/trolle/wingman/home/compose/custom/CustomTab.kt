package dev.trolle.wingman.home.compose.custom

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

interface CustomTab : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(0u, "", null)
}