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
import io.github.jtaeyeon05.why_web.viewmodel.AppModel


fun parseInitHash(): Pair<AppModel, Any> {
    val initHash = BrowserWindow.location.hash.removePrefix("#")
    val params = URLSearchParams(initHash.substringAfter("?", ""))

    val initialModel = AppModel.fromQuery(params)
    val screen = when (initHash.substringBefore("?")) {
        "", "start" -> Screen.Start
        "ready" -> Screen.Ready
        "toBeBorn1" -> Screen.ToBeBorn1
        "toBeBorn2" -> Screen.ToBeBorn2
        "notToBeBorn" -> Screen.NotToBeBorn

        "notFound" -> {
            val route = params["route"]
            Screen.NotFound(route = route ?: "???")
        }
        else -> Screen.NotFound(route = initHash)
    }

    return Pair(initialModel, screen)
}

@OptIn(ExperimentalBrowserHistoryApi::class)
suspend fun NavController.bindBrowserHash(
    modelProvider: () -> AppModel
) {
    bindToBrowserNavigation(
        getBackStackEntryRoute = { entry ->
            val route: String
            val modelQueryMap = modelProvider().toQueryMap()
            val screenQueryMap = mutableMapOf<String, String>()

            val destination = entry.destination
            route = when {
                destination.hasRoute<Screen.Start>() -> "#start"
                destination.hasRoute<Screen.Ready>() -> "#ready"
                destination.hasRoute<Screen.NotToBeBorn>() -> "#notToBeBorn"
                destination.hasRoute<Screen.ToBeBorn1>() -> "#toBeBorn1"
                destination.hasRoute<Screen.ToBeBorn2>() -> "#toBeBorn2"
                destination.hasRoute<Screen.NotFound>() -> {
                    val screen = entry.toRoute<Screen.NotFound>()
                    screenQueryMap["route"] = screen.route
                    "#notFound"
                }
                else -> ""
            }

            route + buildQuery(modelQueryMap + screenQueryMap)
        }
    )
}
