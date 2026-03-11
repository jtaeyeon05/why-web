package io.github.jtaeyeon05.why_web.navigation

import kotlinx.serialization.Serializable


object Screen {
    @Serializable
    data object Start

    @Serializable
    data class NotFound(
        val route: String,
    )
}
