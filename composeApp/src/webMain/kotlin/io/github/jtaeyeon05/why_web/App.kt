package io.github.jtaeyeon05.why_web

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
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
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.ui.foundation.WebKeyEvent
import io.github.jtaeyeon05.why_web.ui.foundation.WebKeyType
import io.github.jtaeyeon05.why_web.ui.foundation.rememberLayoutConstraints
import io.github.jtaeyeon05.why_web.util.BrowserWindow
import io.github.jtaeyeon05.why_web.util.WebKeyboardEvent


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
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            val layoutConstraints = rememberLayoutConstraints(maxWidth, maxHeight)
            val keyboardManager = remember { KeyboardEventManager() }

            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onBackground,
                LocalLayoutConstraints provides layoutConstraints,
                LocalKeyboardEventManager provides keyboardManager
            ) {
                DisposableEffect(keyboardManager) {
                    val listener = { event: WebKeyboardEvent ->
                        keyboardManager.dispatch(
                            WebKeyEvent(
                                keyCode = event.keyCode,
                                status = WebKeyType.KEY_DOWN,
                            )
                        )
                    }

                    BrowserWindow.addEventListener(type = WebKeyType.KEY_DOWN.value, listener = listener)
                    onDispose {
                        BrowserWindow.removeEventListener(type = WebKeyType.KEY_DOWN.value, listener = listener)
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = Screen.Start
                ) {
                    appNavGraph(navController = navController)
                    isGraphReady = true
                }
            }
        }
    }
}
