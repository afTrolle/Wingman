package dev.trolle.wingman.ui.ext

import cafe.adriel.voyager.navigator.Navigator

val Navigator.parentOrThrow get() = parent ?: error("Navigator isn't nested")
val Navigator.parentScreenOrThrow get() = parentOrThrow.lastItem
