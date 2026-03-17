package io.github.jtaeyeon05.why_web.navigation

import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.bindToBrowserNavigation
import androidx.navigation.toRoute
import io.github.jtaeyeon05.why_web.util.BrowserWindow
import io.github.jtaeyeon05.why_web.util.URLSearchParams
import io.github.jtaeyeon05.why_web.util.buildQuery
import io.github.jtaeyeon05.why_web.viewmodel.AppModel


fun parseInitHash(): Pair<AppModel, Screen> {
    val initHash = BrowserWindow.location.hash.removePrefix("#")
    val params = URLSearchParams(initHash.substringAfter("?", ""))

    val initialModel = AppModel.fromQuery(params)
    val screen = Screen.fromIdentifier(
        identifier = initHash.substringBefore("?"),
        params = params
    )

    return Pair(initialModel, screen)
}

@OptIn(ExperimentalBrowserHistoryApi::class)
suspend fun NavController.bindBrowserHash(
    modelProvider: () -> AppModel
) {
    bindToBrowserNavigation(
        getBackStackEntryRoute = { entry ->
            val identifier: String
            val modelQueryMap = modelProvider().toQueryMap()
            val screenQueryMap = mutableMapOf<String, String>()

            val destination = entry.destination
            identifier = when {
                destination.hasRoute<Screen.Start>() -> Screen.Start.identifier()
                destination.hasRoute<Screen.Ready>() -> Screen.Ready.identifier()
                destination.hasRoute<Screen.ToBeBorn1>() -> Screen.ToBeBorn1.identifier()
                destination.hasRoute<Screen.ToBeBorn2>() -> Screen.ToBeBorn2.identifier()
                destination.hasRoute<Screen.Earth>() -> {
                    val screen = entry.toRoute<Screen.Earth>()
                    screen.destination?.let { screenQueryMap["destination"] = screen.destination }
                    screen.identifier()
                }
                destination.hasRoute<Screen.Born>() -> Screen.Born.identifier()
                destination.hasRoute<Screen.NotToBeBorn>() -> Screen.NotToBeBorn.identifier()
                destination.hasRoute<Screen.NotFound>() -> {
                    val screen = entry.toRoute<Screen.NotFound>()
                    screenQueryMap["route"] = screen.route
                    screen.identifier()
                }
                else -> ""
            }

            "#" + identifier + buildQuery(modelQueryMap + screenQueryMap)
        }
    )
}
