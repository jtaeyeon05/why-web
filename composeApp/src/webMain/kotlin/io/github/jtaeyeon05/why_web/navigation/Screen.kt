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
    data object Web: Screen

    @Serializable
    data class Draw(
        val test: String = ""
    ): Screen

    @Serializable
    data object NotToBeBorn: Screen

    @Serializable
    data object EasterEgg1: Screen

    @Serializable
    data object EasterEgg2: Screen

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
        is Web -> "web"
        is Draw -> "draw"
        is NotToBeBorn -> "notToBeBorn"
        is EasterEgg1
            -> listOf("eeeastrgg1", "gGiMZziPPokgeunBHap", "wa_gorapaduck", "._..-.._-__-_._.-.._-.__-._-._..-._..-_._-_..-__-.__-.-.__", "bozovaetteoli", "byeongmucheong").random()
        is EasterEgg2
            -> listOf("eeeastrgg2", "a-too-long-url-might-be-bad-for-the-user-experience", "gong-gi-cheong-jeong-gi-pil-teo", "noraebureulre", "ro__").random()
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
            "web" -> Web
            "draw" -> {
                // TODO
                Draw()
            }
            "notToBeBorn" -> NotToBeBorn
            "eeeastrgg1", "gGiMZziPPokgeunBHap", "wa_gorapaduck", "._..-.._-__-_._.-.._-.__-._-._..-._..-_._-_..-__-.__-.-.__", "bozovaetteoli", "byeongmucheong"
                 -> EasterEgg1
            "eeeastrgg2", "a-too-long-url-might-be-bad-for-the-user-experience", "gong-gi-cheong-jeong-gi-pil-teo", "noraebureulre", "ro__"
                -> EasterEgg2
            "notFound" -> {
                val route = params?.get("route")
                NotFound(route = route ?: "???")
            }
            else -> NotFound(identifier)
        }
    }
}
