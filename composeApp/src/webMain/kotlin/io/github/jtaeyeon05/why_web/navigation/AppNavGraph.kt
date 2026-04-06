package io.github.jtaeyeon05.why_web.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.github.jtaeyeon05.why_web.ui.screen.BornScreen
import io.github.jtaeyeon05.why_web.ui.screen.EarthScreen
import io.github.jtaeyeon05.why_web.ui.screen.EasterEgg1Screen
import io.github.jtaeyeon05.why_web.ui.screen.EasterEgg2Screen
import io.github.jtaeyeon05.why_web.ui.screen.NotFoundScreen
import io.github.jtaeyeon05.why_web.ui.screen.NotToBeBornScreen
import io.github.jtaeyeon05.why_web.ui.screen.ReadyScreen
import io.github.jtaeyeon05.why_web.ui.screen.StartScreen
import io.github.jtaeyeon05.why_web.ui.screen.ToBeBorn1Screen
import io.github.jtaeyeon05.why_web.ui.screen.ToBeBorn2Screen
import io.github.jtaeyeon05.why_web.ui.screen.WebScreen
import io.github.jtaeyeon05.why_web.viewmodel.AppViewModel


fun NavGraphBuilder.appNavGraph(
    navController: NavController,
    viewModel: AppViewModel,
) {
    composable<Screen.Start> {
        Box(modifier = Modifier.fillMaxSize()) {
            StartScreen(
                navController = navController,
            )
        }
    }
    composable<Screen.Ready> {
        Box(modifier = Modifier.fillMaxSize()) {
            ReadyScreen(
                navController = navController,
            )
        }
    }
    composable<Screen.ToBeBorn1> {
        Box(modifier = Modifier.fillMaxSize()) {
            ToBeBorn1Screen(
                navController = navController,
                viewModel = viewModel,
            )
        }
    }
    composable<Screen.ToBeBorn2> {
        Box(modifier = Modifier.fillMaxSize()) {
            ToBeBorn2Screen(
                navController = navController,
                viewModel = viewModel,
            )
        }
    }
    composable<Screen.Earth> { backStackEntry ->
        Box(modifier = Modifier.fillMaxSize()) {
            EarthScreen(
                navController = navController,
                screen = backStackEntry.toRoute<Screen.Earth>(),
            )
        }
    }
    composable<Screen.Born> { backStackEntry ->
        Box(modifier = Modifier.fillMaxSize()) {
            BornScreen(
                navController = navController,
                viewModel = viewModel,
                screen = backStackEntry.toRoute<Screen.Born>(),
            )
        }
    }
    composable<Screen.Web> {
        Box(modifier = Modifier.fillMaxSize()) {
            WebScreen(
                viewModel = viewModel,
            )
        }
    }
    composable<Screen.NotToBeBorn> {
        Box(modifier = Modifier.fillMaxSize()) {
            NotToBeBornScreen(
                navController = navController,
            )
        }
    }
    composable<Screen.EasterEgg1> {
        Box(modifier = Modifier.fillMaxSize()) {
            EasterEgg1Screen(
                navController = navController,
            )
        }
    }
    composable<Screen.EasterEgg2> {
        Box(modifier = Modifier.fillMaxSize()) {
            EasterEgg2Screen(
                navController = navController,
            )
        }
    }
    composable<Screen.NotFound> { backStackEntry ->
        Box(modifier = Modifier.fillMaxSize()) {
            NotFoundScreen(
                navController = navController,
                screen = backStackEntry.toRoute<Screen.NotFound>(),
            )
        }
    }
}
