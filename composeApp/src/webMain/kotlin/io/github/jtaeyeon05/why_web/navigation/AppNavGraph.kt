package io.github.jtaeyeon05.why_web.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.github.jtaeyeon05.why_web.ui.screen.NotFoundScreen
import io.github.jtaeyeon05.why_web.ui.screen.NotToBeBornScreen
import io.github.jtaeyeon05.why_web.ui.screen.StartScreen


fun NavGraphBuilder.appNavGraph(navController: NavController) {
    composable<Screen.Start> {
        Box(modifier = Modifier.fillMaxSize()) {
            StartScreen(navController = navController)
        }
    }
    composable<Screen.NotToBeBorn> {
        Box(modifier = Modifier.fillMaxSize()) {
            NotToBeBornScreen(navController = navController)
        }
    }
    composable<Screen.NotFound> { backStackEntry ->
        Box(modifier = Modifier.fillMaxSize()) {
            NotFoundScreen(
                screen = backStackEntry.toRoute<Screen.NotFound>(),
                navController = navController,
            )
        }
    }
}
