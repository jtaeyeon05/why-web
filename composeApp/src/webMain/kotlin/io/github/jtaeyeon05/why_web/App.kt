package io.github.jtaeyeon05.why_web

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.jtaeyeon05.why_web.navigation.Screen
import io.github.jtaeyeon05.why_web.navigation.appNavGraph
import io.github.jtaeyeon05.why_web.ui.AppTheme
import io.github.jtaeyeon05.why_web.ui.foundation.KeyboardEventManager
import io.github.jtaeyeon05.why_web.ui.foundation.LocalKeyboardEventManager
import io.github.jtaeyeon05.why_web.ui.foundation.WebKeyEvent
import io.github.jtaeyeon05.why_web.ui.foundation.WebKeyType
import io.github.jtaeyeon05.why_web.ui.rememberLayoutConstraints
import io.github.jtaeyeon05.why_web.util.BrowserWindow


@Composable
fun App(
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    val navController = rememberNavController()
    val keyboardManager = remember { KeyboardEventManager() }
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
            CompositionLocalProvider(LocalKeyboardEventManager provides keyboardManager) {
                LaunchedEffect(Unit) {
                    BrowserWindow.addEventListener(
                        type = WebKeyType.KEY_DOWN.value,
                        listener = { event ->
                            keyboardManager.dispatch(
                                WebKeyEvent(
                                    keyCode = event.keyCode,
                                    status = WebKeyType.KEY_DOWN,
                                )
                            )
                        }
                    )
                }

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
}
