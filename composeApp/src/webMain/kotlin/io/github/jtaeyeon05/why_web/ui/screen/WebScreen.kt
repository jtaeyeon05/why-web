package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.parkwoocheol.composewebview.ComposeWebView
import com.parkwoocheol.composewebview.DarkMode
import com.parkwoocheol.composewebview.WebViewSettings
import com.parkwoocheol.composewebview.rememberWebViewController
import com.parkwoocheol.composewebview.rememberWebViewState
import io.github.jtaeyeon05.why_web.data.Avatar
import io.github.jtaeyeon05.why_web.ui.foundation.LocalKeyboardEventManager
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.ui.foundation.rememberAnimatedText
import io.github.jtaeyeon05.why_web.ui.widget.MessageBox
import io.github.jtaeyeon05.why_web.ui.widget.MiniSquareButton
import io.github.jtaeyeon05.why_web.util.BrowserWindow
import io.github.jtaeyeon05.why_web.util.focusIframes
import io.github.jtaeyeon05.why_web.viewmodel.AppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


@Composable
fun BoxScope.WebScreen(
    navController: NavController,
    viewModel: AppViewModel,
) {
    LocalLayoutConstraints.current.run {
        val keyboardManager = LocalKeyboardEventManager.current
        val scope = rememberCoroutineScope()

        var isWebShowing by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            keyboardManager.events.collect { webKeyEvent ->
                if (webKeyEvent.isConfirmPressed) {
                    keyboardManager.disable()
                    isWebShowing = true
                }
            }
        }
        DisposableEffect(Unit) {
            val job = scope.launch {
                delay(2_500)
                if (!isWebShowing) {
                    keyboardManager.disable()
                    isWebShowing = true
                }
            }
            onDispose {
                job.cancel()
                keyboardManager.enable()
            }
        }

        // Message
        var texts by rememberSaveable { mutableStateOf(arrayOf("웹 서핑을 할 것이다!", "", "... ...")) }
        val message = rememberAnimatedText(texts = texts)

        MessageBox(
            modifier = Modifier.align(Alignment.BottomCenter),
            message = message,
            avatar = Avatar.user(
                avatar = viewModel.model.value.avatar,
                name = viewModel.model.value.name,
            ),
            line = 2f,
        )

        // WebView
        val webWidth = screen.base - 2 * padding.large
        val webHeight = screen.height - 2 * box.messageBoxHeight(2f) - 2 * padding.large

        val webState = rememberWebViewState(url = BrowserWindow.location.origin + "/?iframe_seed=${Random.nextInt()}")
        val webController = rememberWebViewController()

        AnimatedVisibility(
            visible = isWebShowing,
            modifier = Modifier
                .width(webWidth)
                .height(webHeight)
                .align(Alignment.Center),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = inset.borderWidth,
                        color = LocalContentColor.current,
                    )
                    .background(
                        color = LocalContentColor.current
                            .copy(alpha = 0.25f)
                            .compositeOver(MaterialTheme.colorScheme.background),
                    )
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(padding.large),
                    text = "WebView",
                    fontSize = typography.large.sp,
                    lineHeight = typography.large.lineSp,
                    fontWeight = FontWeight.Bold,
                )
                ComposeWebView(
                    modifier = Modifier
                        .padding(inset.borderWidth)
                        .fillMaxSize(),
                    state = webState,
                    controller = webController,
                    settings = WebViewSettings(
                        javaScriptEnabled = true,
                        domStorageEnabled = true,
                        darkMode = DarkMode.DARK,
                    ),
                    onCreated = {
                        focusIframes()
                    },
                )
            }
        }

        // Close
        val canBack by derivedStateOf { navController.previousBackStackEntry != null }

        LaunchedEffect(canBack) {
            if (canBack) {
                keyboardManager.globalEvents.collect { webKeyEvent ->
                    if (webKeyEvent.isCancelPressed) {
                        keyboardManager.enable()
                        navController.popBackStack()
                    }
                }
            }
        }

        if (canBack) {
            MiniSquareButton(
                modifier = Modifier.align(Alignment.TopStart),
                text = "X",
                onClick = { navController.popBackStack() },
            )
        }
    }
}
