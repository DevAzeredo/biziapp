package dev.azeredo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform