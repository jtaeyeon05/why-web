package io.github.jtaeyeon05.why_web

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.jtaeyeon05.why_web.navigation.appNavGraph
import io.github.jtaeyeon05.why_web.navigation.bindBrowserHash
import io.github.jtaeyeon05.why_web.navigation.parseHash
import io.github.jtaeyeon05.why_web.ui.AppTheme
import io.github.jtaeyeon05.why_web.ui.foundation.KeyboardEventManager
import io.github.jtaeyeon05.why_web.ui.foundation.LocalKeyboardEventManager
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.ui.foundation.WebKeyEvent
import io.github.jtaeyeon05.why_web.ui.foundation.WebKeyType
import io.github.jtaeyeon05.why_web.ui.foundation.rememberLayoutConstraints
import io.github.jtaeyeon05.why_web.util.BrowserWindow
import io.github.jtaeyeon05.why_web.util.IframeKeyMessage
import io.github.jtaeyeon05.why_web.util.WebJSON
import io.github.jtaeyeon05.why_web.util.WebKeyboardEvent
import io.github.jtaeyeon05.why_web.util.WebMessageEvent
import io.github.jtaeyeon05.why_web.viewmodel.AppViewModel
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.unsafeCast


@OptIn(ExperimentalWasmJsInterop::class)
@Composable
fun App() {
    val navController = rememberNavController()
    val (initialModel, startScreen) = remember { parseHash() }
    val viewModel = remember(initialModel) { AppViewModel(initialModel) }

    LaunchedEffect(navController) {
        navController.bindBrowserHash { viewModel.model.value }
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
                        BrowserWindow.parent.postMessage(
                            message = WebJSON.parse(
                                """
                                    {
                                        "type": "IFRAME_KEY",
                                        "keyCode": "${event.keyCode}",
                                        "status": "${WebKeyType.KEY_DOWN.value}"
                                    }
                                """.trimIndent()
                            ),
                            targetOrigin = "*",
                        )
                    }

                    BrowserWindow.addEventListener(type = WebKeyType.KEY_DOWN.value, listener = listener)
                    onDispose {
                        BrowserWindow.removeEventListener(type = WebKeyType.KEY_DOWN.value, listener = listener)
                    }
                }
                DisposableEffect(keyboardManager) {
                    val listener = { event: WebMessageEvent ->
                        val message = event.data?.unsafeCast<IframeKeyMessage>()
                        if (message?.type == "IFRAME_KEY" && message.keyCode != null && message.status != null) {
                            keyboardManager.iframeDispatch(
                                WebKeyEvent(
                                    keyCode = message.keyCode!!,
                                    status = WebKeyType.fromValue(message.status!!),
                                )
                            )
                        }
                    }

                    BrowserWindow.addEventListener(type = "message", listener = listener)
                    onDispose {
                        BrowserWindow.removeEventListener(type = "message", listener = listener)
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = startScreen,
                ) {
                    appNavGraph(
                        navController = navController,
                        viewModel = viewModel,
                    )
                }
            }
        }
    }
}
