package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.parkwoocheol.composewebview.ComposeWebView
import com.parkwoocheol.composewebview.DarkMode
import com.parkwoocheol.composewebview.WebViewSettings
import com.parkwoocheol.composewebview.rememberWebViewController
import com.parkwoocheol.composewebview.rememberWebViewState
import io.github.jtaeyeon05.why_web.ui.foundation.LocalKeyboardEventManager
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.ui.foundation.WebKey
import io.github.jtaeyeon05.why_web.ui.widget.BouncingEmoji
import io.github.jtaeyeon05.why_web.ui.widget.ClassicButton
import io.github.jtaeyeon05.why_web.ui.widget.MiniSquareButton
import io.github.jtaeyeon05.why_web.ui.widget.SelectionBox
import io.github.jtaeyeon05.why_web.util.emojiGroups
import io.github.jtaeyeon05.why_web.util.enableIframesToAutoplay
import io.github.jtaeyeon05.why_web.util.nextGaussian
import io.github.jtaeyeon05.why_web.util.specialString
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.run


@Composable
fun BoxScope.EasterEgg2Screen(
    navController: NavController,
) {
    LocalLayoutConstraints.current.run {
        var backgroundColorKey by rememberSaveable { mutableStateOf(-1) }
        val backgroundColor by animateColorAsState(
            targetValue = when (backgroundColorKey) {
                0 -> Color.Black
                1 -> Color.Red
                2 -> Color.Yellow
                3 -> Color.Green
                4 -> Color.Cyan
                5 -> Color.Blue
                6 -> Color.Magenta
                else -> Color.Black
            }.copy(alpha = 0.25f),
            label = "backgroundColor"
        )
        LaunchedEffect(backgroundColorKey) {
            if (backgroundColorKey != 0) {
                while (backgroundColorKey + 1 in 1 .. 6) {
                    delay(200)
                    backgroundColorKey += 1
                }
                delay(500)
                backgroundColorKey = 0
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            val keyboardManager = LocalKeyboardEventManager.current

            // BouncingEmoji
            var bouncingEmojiKey by rememberSaveable { mutableStateOf(0) }
            key(bouncingEmojiKey) {
                val emojiGroup = remember { emojiGroups.random() }
                repeat(50) {
                    BouncingEmoji(
                        emoji = remember { emojiGroup.random() },
                        initialDirection = remember {
                            Random.nextGaussian(
                                mean = 0.5 * PI,
                                stdDev = 0.75,
                                min = -0.5 * PI,
                                max = 1.5 * PI
                            ).toFloat()
                        },
                        initialMagnitude = remember {
                            screen.base * (0.015f + 0.015f * Random.nextFloat())
                        },
                        accelDirection = 1.5f * PI.toFloat(),
                        accelMagnitude = screen.base * 0.0003f,
                    )
                }
            }

            // WebView
            val (webWidth, webHeight) = run {
                val webHeightMax = screen.height - 2 * box.selectionBoxHeight(2) - 2 * padding.large

                var webWidth = screen.base - 2 * padding.large
                var webHeight = webWidth * 3.0f / 4.0f
                if (webHeight > webHeightMax) {
                    webWidth = webHeightMax * 4.0f / 3.0f
                    webHeight = webHeightMax
                }

                Pair(webWidth, webHeight)
            }

            val webState = rememberWebViewState(url = "https://www.youtube.com/embed/u2erFUojbAA?autoplay=1&controls=0")
            val webController = rememberWebViewController()

            Box(
                modifier = Modifier
                    .width(webWidth)
                    .height(webHeight)
                    .align(Alignment.Center)
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
                        enableIframesToAutoplay()
                    },
                )
            }

            // Close
            val canBack by derivedStateOf { navController.previousBackStackEntry != null }

            LaunchedEffect(canBack) {
                if (canBack) {
                    keyboardManager.events.collect { webKeyEvent ->
                        if (webKeyEvent.isCancelPressed) {
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

            // Selection
            var selection by rememberSaveable { mutableStateOf(1) }
            var selectionScrollTo by rememberSaveable { mutableStateOf(1.5f) }
            LaunchedEffect(Unit) {
                keyboardManager.events.collect { webKeyEvent ->
                    if (webKeyEvent.isUpPressed) {
                        selection = (selection - 1).coerceIn(0..<3)
                        selectionScrollTo = selection.toFloat()
                    } else if (webKeyEvent.isDownPressed) {
                        selection = (selection + 1).coerceIn(0..<3)
                        selectionScrollTo = selection.toFloat()
                    }
                    if (webKeyEvent.isAnyPressed(WebKey.SHIFT_LEFT, WebKey.SHIFT_RIGHT)) {
                        bouncingEmojiKey += 1
                    }
                }
            }

            SelectionBox(
                modifier = Modifier.align(Alignment.BottomCenter),
                scrollTo = selectionScrollTo,
                line = 2,
            ) {
                ClassicButton(
                    modifier = Modifier.fillMaxWidth(),
                    focused = selection == 0,
                    onClick = { backgroundColorKey = 1 },
                    onFocused = { selection = 0 },
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = rememberSaveable { specialString(Random.nextInt(3..6)) },
                    )
                }
                ClassicButton(
                    modifier = Modifier.fillMaxWidth(),
                    focused = selection == 1,
                    onClick = {
                        val urlList = listOf(
                            "https://www.youtube.com/embed/eMGRt0A9Yns?autoplay=1&controls=0",
                            "https://www.youtube.com/embed/vR21RMEV0fg?autoplay=1&controls=0",
                            "https://www.youtube.com/embed/6zAkiKy76xs?autoplay=1&controls=0",
                            "https://www.youtube.com/embed/5mGuCdlCcNM?autoplay=1&controls=0",
                            "https://www.youtube.com/embed/3e6motL4QMc?autoplay=1&controls=0",
                            "https://www.youtube.com/embed/tcHaMWktCYE?autoplay=1&controls=0",
                        )
                        webController.loadUrl(urlList.random())
                        webController.reload()
                    },
                    onFocused = { selection = 1 },
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = rememberSaveable { specialString(Random.nextInt(3..6)) },
                    )
                }
                ClassicButton(
                    modifier = Modifier.fillMaxWidth(),
                    focused = selection == 2,
                    onClick = { bouncingEmojiKey += 1 },
                    onFocused = { selection = 2 },
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = rememberSaveable { specialString(Random.nextInt(3..6)) },
                    )
                }
            }
        }
    }
}
