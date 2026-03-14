package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.jtaeyeon05.why_web.buildinfo.BuildInfo
import io.github.jtaeyeon05.why_web.navigation.Screen
import io.github.jtaeyeon05.why_web.ui.foundation.LocalKeyboardEventManager
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.ui.foundation.rememberAnimatedText
import io.github.jtaeyeon05.why_web.ui.widget.Avatar
import io.github.jtaeyeon05.why_web.ui.widget.ClassicButton
import io.github.jtaeyeon05.why_web.ui.widget.MessageBox
import io.github.jtaeyeon05.why_web.ui.widget.SelectionBox


@Composable
fun BoxScope.StartScreen(
    navController: NavController,
) {
    LocalLayoutConstraints.current.run {
        // Message
        val message = rememberAnimatedText("태어나시겠습니까?")
        MessageBox(
            modifier = Modifier.align(Alignment.BottomCenter),
            message = message,
            avatar = Avatar.God,
        )

        // Selection
        val keyboardManager = LocalKeyboardEventManager.current
        val selectionLine = 3
        var selection by rememberSaveable { mutableStateOf(0) }
        var selectionScrollTo by rememberSaveable { mutableStateOf(0) }
        LaunchedEffect(Unit) {
            keyboardManager.events.collect { webKeyEvent ->
                if (webKeyEvent.isUpPressed) {
                    selection = (selection - 1).coerceAtLeast(0)
                    selectionScrollTo = selection
                } else if (webKeyEvent.isDownPressed) {
                    selection = (selection + 1).coerceAtMost(selectionLine - 1)
                    selectionScrollTo = selection
                }
            }
        }

        SelectionBox(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = box.messageBoxHeight(box.defaultMessageLine) + padding.medium),
            scrollTo = selectionScrollTo,
            line = 2,
        ) {
            ClassicButton(
                focused = selection == 0,
                onClick = { navController.navigate(Screen.Ready) },
                onFocused = { selection = 0 },
            ) {
                Text("예")
            }
            ClassicButton(
                focused = selection == 1,
                onClick = { navController.navigate(Screen.NotToBeBorn) },
                onFocused = { selection = 1 },
            ) {
                Text("아니요")
            }
            ClassicButton(
                focused = selection == 2,
                onClick = { /* TODO */ },
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
