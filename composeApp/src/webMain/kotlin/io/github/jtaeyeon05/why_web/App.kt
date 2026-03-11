package io.github.jtaeyeon05.why_web

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.jtaeyeon05.why_web.navigation.Screen
import io.github.jtaeyeon05.why_web.navigation.appNavGraph
import io.github.jtaeyeon05.why_web.ui.AppTheme
import io.github.jtaeyeon05.why_web.ui.rememberLayoutConstraints


@Composable
fun App(
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    val navController = rememberNavController()
    var isGraphReady by remember { mutableStateOf(false) }

    LaunchedEffect(isGraphReady) {
        if (isGraphReady) {
            onNavHostReady(navController)
        }
    }

    AppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
                val layoutConstraints = rememberLayoutConstraints(maxWidth, maxHeight)

                NavHost(
                    navController = navController,
                    startDestination = Screen.Start
                ) {
                    appNavGraph(
                        navController = navController,
                        layoutConstraints = layoutConstraints
                    )
                    isGraphReady = true
                }
            }
        }
    }
}
