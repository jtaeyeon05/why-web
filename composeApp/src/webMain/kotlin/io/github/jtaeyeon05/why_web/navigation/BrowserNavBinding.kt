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
import kotlin.collections.plus
import kotlin.js.ExperimentalWasmJsInterop


fun parseHash(): Pair<AppModel, Screen> {
    val initHash = BrowserWindow.location.hash.removePrefix("#").substringBefore("?")
    val params = URLSearchParams(BrowserWindow.location.hash.substringAfter("?", ""))

    val initialModel = AppModel.fromQuery(params)
    val screen = Screen.fromIdentifier(
        identifier = initHash,
        params = params
    )

    return Pair(initialModel, screen)
}

fun serializeHash(
    model: AppModel,
    screen: Screen,
): String {
    val identifier: String = screen.identifier()
    val modelMapQuery = model.toQueryMap()
    val screenMapQuery = mutableMapOf<String, String>()
    val screenListQuery = mutableListOf<String>()

    when (screen) {
        is Screen.Earth -> {
            if (screen.destination != null)
                screenMapQuery["destination"] = screen.destination
        }
        is Screen.NotFound -> {
            screenMapQuery["route"] = screen.route
        }
        else -> {}
    }

    return "#$identifier" + buildQuery(
        mapQuery = modelMapQuery + screenMapQuery,
        listQuery = screenListQuery
    )
}

@OptIn(ExperimentalWasmJsInterop::class)
fun refreshHash(
    model: AppModel,
    screen: Screen,
) {
    val newHash = serializeHash(
        model = model,
        screen = screen,
    )
    BrowserWindow.history.replaceState(
        data = null,
        title = "",
        url = newHash
    )
}

@OptIn(ExperimentalBrowserHistoryApi::class)
suspend fun NavController.bindBrowserHash(
    modelProvider: () -> AppModel,
) {
    bindToBrowserNavigation(
        getBackStackEntryRoute = { entry ->
            val destination = entry.destination

            val screen: Screen = when {
                destination.hasRoute<Screen.Start>() -> Screen.Start
                destination.hasRoute<Screen.Ready>() -> Screen.Ready
                destination.hasRoute<Screen.ToBeBorn1>() -> Screen.ToBeBorn1
                destination.hasRoute<Screen.ToBeBorn2>() -> Screen.ToBeBorn2
                destination.hasRoute<Screen.Earth>() -> entry.toRoute<Screen.Earth>()
                destination.hasRoute<Screen.Born>() -> Screen.Born
                destination.hasRoute<Screen.Web>() -> Screen.Web
                destination.hasRoute<Screen.Draw>() -> entry.toRoute<Screen.Draw>()
                destination.hasRoute<Screen.NotToBeBorn>() -> Screen.NotToBeBorn
                destination.hasRoute<Screen.EasterEgg1>() -> Screen.EasterEgg1
                destination.hasRoute<Screen.EasterEgg2>() -> Screen.EasterEgg2
                destination.hasRoute<Screen.NotFound>() -> entry.toRoute<Screen.NotFound>()
                else -> Screen.NotFound(route = destination.route ?: "???")
            }

            serializeHash(
                model = modelProvider(),
                screen = screen,
            )
        }
    )
}
