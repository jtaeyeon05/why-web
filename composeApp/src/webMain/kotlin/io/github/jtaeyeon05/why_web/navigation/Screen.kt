package io.github.jtaeyeon05.why_web.navigation

import io.github.jtaeyeon05.why_web.util.URLSearchParams
import io.github.jtaeyeon05.why_web.util.get
import kotlinx.serialization.Serializable


sealed interface Screen {
    @Serializable
    data object Start: Screen

    @Serializable
    data object Ready: Screen

    @Serializable
    data object ToBeBorn1: Screen

    @Serializable
    data object ToBeBorn2: Screen

    @Serializable
    data class Earth(
        val destination: String? = null,
    ): Screen

    @Serializable
    data object Born: Screen

    @Serializable
    data object NotToBeBorn: Screen

    @Serializable
    data class NotFound(
        val route: String = "???",
    ): Screen

    fun identifier(): String = when (this) {
        is Start -> "start"
        is Ready -> "ready"
        is ToBeBorn1 -> "toBeBorn1"
        is ToBeBorn2 -> "toBeBorn2"
        is Earth -> "earth"
        is Born -> "born"
        is NotToBeBorn -> "notToBeBorn"
        is NotFound -> "notFound"
    }

    companion object {
        fun fromIdentifier(
            identifier: String,
            params: URLSearchParams? = null
        ): Screen = when (identifier) {
            "", "start" -> Start
            "ready" -> Ready
            "toBeBorn1" -> ToBeBorn1
            "toBeBorn2" -> ToBeBorn2
            "earth" -> {
                val destination = params?.get("destination")
                Earth(
                    destination = destination,
                )
            }
            "born" -> Born
            "notToBeBorn" -> NotToBeBorn
            "notFound" -> {
                val route = params?.get("route")
                NotFound(route = route ?: "???")
            }
            else -> NotFound(identifier)
        }
    }
}
