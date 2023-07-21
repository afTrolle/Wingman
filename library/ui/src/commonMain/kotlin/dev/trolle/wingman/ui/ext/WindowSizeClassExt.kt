package dev.trolle.wingman.ui.ext

import dev.trolle.wingman.ui.WindowHeightSizeClass
import dev.trolle.wingman.ui.WindowSizeClass
import dev.trolle.wingman.ui.WindowWidthSizeClass

val WindowSizeClass.isWidthCompact get() = widthSizeClass == WindowWidthSizeClass.Compact
val WindowSizeClass.isWidthMedium get() = widthSizeClass == WindowWidthSizeClass.Medium
val WindowSizeClass.isWidthExpanded get() = widthSizeClass == WindowWidthSizeClass.Expanded
val WindowSizeClass.isHeightCompact get() = heightSizeClass == WindowHeightSizeClass.Compact
val WindowSizeClass.isHeightMedium get() = heightSizeClass == WindowHeightSizeClass.Medium
val WindowSizeClass.isHeightExpanded get() = heightSizeClass == WindowHeightSizeClass.Expanded
