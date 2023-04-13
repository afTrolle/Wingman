package dev.trolle.af.wingman

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform