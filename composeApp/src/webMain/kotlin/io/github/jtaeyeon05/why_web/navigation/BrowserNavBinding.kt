package io.github.jtaeyeon05.why_web.navigation

import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.NavController
import androidx.navigation.bindToBrowserNavigation
import androidx.navigation.toRoute
import io.github.jtaeyeon05.why_web.util.BrowserWindow
import io.github.jtaeyeon05.why_web.util.URLSearchParams
import io.github.jtaeyeon05.why_web.util.buildQuery
import io.github.jtaeyeon05.why_web.util.decodeURIComponent


fun NavController.navigationFromInitHash() {
    val initHash = BrowserWindow.location.hash.removePrefix("#")
    val params = URLSearchParams(initHash.substringAfter("?", ""))

    if (initHash.isBlank()) return
    when (initHash.substringBefore("?")) {
        "", "start" -> {
            navigate(Screen.Start)
        }
        "notToBeBorn" -> {
            navigate(Screen.NotToBeBorn)
        }
        "notFound" -> {
            val route = params.get("notFound")?.let { decodeURIComponent(it) }
            navigate(
                Screen.NotFound(route = route ?: "???")
            )
        }
        else -> {
            navigate(
                Screen.NotFound(route = initHash)
            )
        }
    }
}

@OptIn(ExperimentalBrowserHistoryApi::class)
suspend fun NavController.bindBrowserHash() {
    bindToBrowserNavigation(
        getBackStackEntryRoute = { entry ->
            val route = entry.destination.route.orEmpty()
            when {
                route.startsWith(Screen.Start.serializer().descriptor.serialName) -> {
                    "#start"
                }
                route.startsWith(Screen.NotToBeBorn.serializer().descriptor.serialName) -> {
                    "#notToBeBorn"
                }
                route.startsWith(Screen.NotFound.serializer().descriptor.serialName) -> {
                    val args = entry.toRoute<Screen.NotFound>()
                    "#notFound" + buildQuery(mapQuery = mapOf("route" to args.route))
                }
                else -> ""
            }
        }
    )
}
