package io.github.jtaeyeon05.why_web

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform