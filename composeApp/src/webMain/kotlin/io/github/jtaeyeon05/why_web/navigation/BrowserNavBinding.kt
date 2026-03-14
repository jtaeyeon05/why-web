package io.github.jtaeyeon05.why_web.navigation

import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.bindToBrowserNavigation
import androidx.navigation.toRoute
import io.github.jtaeyeon05.why_web.util.BrowserWindow
import io.github.jtaeyeon05.why_web.util.URLSearchParams
import io.github.jtaeyeon05.why_web.util.buildQuery
import io.github.jtaeyeon05.why_web.util.get


fun NavController.navigationFromInitHash() {
    val initHash = BrowserWindow.location.hash.removePrefix("#")
    val params = URLSearchParams(initHash.substringAfter("?", ""))

    if (initHash.isBlank()) return
    when (initHash.substringBefore("?")) {
        "", "start" -> navigate(Screen.Start)
        "ready" -> navigate(Screen.Ready)
        "toBeBorn1" -> navigate(Screen.ToBeBorn1)
        "toBeBorn2" -> navigate(Screen.ToBeBorn2)
        "notToBeBorn" -> navigate(Screen.NotToBeBorn)

        "notFound" -> {
            val route = params["notFound"]
            navigate(Screen.NotFound(route = route ?: "???"))
        }
        else -> navigate(Screen.NotFound(route = initHash))
    }
}

@OptIn(ExperimentalBrowserHistoryApi::class)
suspend fun NavController.bindBrowserHash() {
    bindToBrowserNavigation(
        getBackStackEntryRoute = { entry ->
            val destination = entry.destination
            when {
                destination.hasRoute<Screen.Start>() -> "#start"
                destination.hasRoute<Screen.Ready>() -> "#ready"
                destination.hasRoute<Screen.NotToBeBorn>() -> "#notToBeBorn"
                destination.hasRoute<Screen.ToBeBorn1>() -> "#toBeBorn1"
                destination.hasRoute<Screen.ToBeBorn2>() -> "#toBeBorn2"

                destination.hasRoute<Screen.NotFound>() -> {
                    val args = entry.toRoute<Screen.NotFound>()
                    "#notFound" + buildQuery(queryMap = mapOf("route" to args.route))
                }
                else -> ""
            }
        }
    )
}
