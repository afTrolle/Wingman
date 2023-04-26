package dev.trolle.wingman.ui

import cafe.adriel.voyager.core.screen.Screen

interface Navigation {
    suspend fun open(screen: Screen)
    suspend fun pop()
    suspend fun replaceAll(screen: Screen)

    // Feature Entrypoint
    suspend fun openHomeScreen()
}
