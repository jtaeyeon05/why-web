package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.jtaeyeon05.why_web.buildinfo.BuildInfo
import io.github.jtaeyeon05.why_web.ui.ClassicButton
import io.github.jtaeyeon05.why_web.ui.foundation.LocalKeyboardEventManager
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.util.openTabInNewTab
import kotlinx.coroutines.delay


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BoxScope.StartScreen(navController: NavController) {
    var nowMessage by rememberSaveable { mutableStateOf("") }
    val targetMessage = "태어나시겠습니까?"

    LaunchedEffect(Unit) {
        delay(1_000)
        while (nowMessage.length < targetMessage.length) {
            nowMessage += targetMessage[nowMessage.length]
            delay(100)
        }
    }

    LocalLayoutConstraints.current.run {
        // Message
        Column(
            modifier = Modifier
                .width(box.messageBoxWidth)
                .height(box.messageBoxHeight(box.defaultMessageLine))
                .align(Alignment.BottomCenter)
                .border(
                    width = inset.borderWidth,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RectangleShape
                )
                .padding(inset.borderWidth + padding.medium)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(typography.mediumLineHeight.dp),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("[ ")
                    }
                    append("🧞")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(" ?? ]")
                    }
                },
                fontSize = typography.mediumFontSize.sp,
                lineHeight = typography.lineHeight.em,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(typography.mediumLineHeight.dp * 3),
                text = nowMessage,
                fontSize = typography.mediumFontSize.sp,
                lineHeight = typography.lineHeight.em,
            )
        }

        // Selection
        val keyboardManager = LocalKeyboardEventManager.current
        val selectionScrollState = rememberScrollState()

        var selection by rememberSaveable { mutableStateOf(0) }

        LaunchedEffect(Unit) {
            keyboardManager.events.collect { webKeyEvent ->
                if (webKeyEvent.isUpPressed && selection - 1 >= 0) {
                    selection -= 1
                    if (selection == 0) selectionScrollState.scrollTo(0)
                } else if (webKeyEvent.isDownPressed && selection + 1 <= 2) {
                    selection += 1
                    if (selection == 2) selectionScrollState.scrollTo(selectionScrollState.maxValue)
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(bottom = box.messageBoxHeight(box.defaultMessageLine) + padding.medium)
                .width(IntrinsicSize.Max)
                .sizeIn(
                    minWidth = box.selectionBoxWidth.min,
                    maxWidth = box.selectionBoxWidth.max,
                    minHeight = box.selectionBoxHeight(box.defaultSelectionLine),
                    maxHeight = box.selectionBoxHeight(box.defaultSelectionLine),
                )
                .align(Alignment.BottomCenter)
                .border(
                    width = inset.borderWidth,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RectangleShape
                )
                .padding(inset.borderWidth + padding.medium)
                .verticalScroll(selectionScrollState)
        ) {
            ClassicButton(
                focused = selection == 0,
                onClick = { openTabInNewTab("https://xodus.lol/") },
                onFocused = { selection = 0 },
            ) {
                Text("예")
            }
            ClassicButton(
                focused = selection == 1,
                onClick = { openTabInNewTab("https://xodus.lol/") },
                onFocused = { selection = 1 },
            ) {
                Text("아니요")
            }
            ClassicButton(
                focused = selection == 2,
                onClick = { openTabInNewTab("https://xodus.lol/") },
                onFocused = { selection = 2 },
            ) {
                Text("!!!")
            }
        }

        // IdentifierBox
        var isIdentifierBoxTapped by rememberSaveable { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(box.buttonHeight)
                .pointerInput(Unit) {
                    detectTapGestures {
                        isIdentifierBoxTapped = !isIdentifierBoxTapped
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            if (isIdentifierBoxTapped) {
                BasicText(
                    text = "${BuildInfo.VERSION}\n${BuildInfo.BUILD_NUMBER}",
                    style = LocalTextStyle.current.copy(
                        color = LocalContentColor.current,
                        textAlign = TextAlign.Center,
                        lineHeight = typography.lineHeight.em,
                    ),
                    autoSize = TextAutoSize.StepBased(
                        minFontSize = 0.1f.sp,
                        maxFontSize = 100.0f.sp,
                    )
                )
            }
        }
    }
}
