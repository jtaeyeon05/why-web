package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import io.github.jtaeyeon05.why_web.navigation.Screen
import io.github.jtaeyeon05.why_web.ui.foundation.LocalKeyboardEventManager
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.ui.foundation.rememberAnimatedText
import io.github.jtaeyeon05.why_web.ui.widget.Avatar
import io.github.jtaeyeon05.why_web.ui.widget.ClassicButton
import io.github.jtaeyeon05.why_web.ui.widget.MessageBox
import io.github.jtaeyeon05.why_web.ui.widget.SelectionBox


@Composable
fun BoxScope.ReadyScreen(navController: NavController) {
    LocalLayoutConstraints.current.run {
        // Message
        val message = rememberAnimatedText(
            """
                꼭 태어나셔야겠습니까?
                다시 한 번 생각하십시오.
            """.trimIndent()
        )
        MessageBox(
            modifier = Modifier.align(Alignment.BottomCenter),
            message = message,
            avatar = Avatar.God,
        )

        // Selection
        val keyboardManager = LocalKeyboardEventManager.current
        val selectionLine = 2
        var selection by rememberSaveable { mutableStateOf(0) }
        LaunchedEffect(Unit) {
            keyboardManager.events.collect { webKeyEvent ->
                if (webKeyEvent.isUpPressed) {
                    selection = (selection - 1).coerceAtLeast(0)
                } else if (webKeyEvent.isDownPressed) {
                    selection = (selection + 1).coerceAtMost(selectionLine - 1)
                }
            }
        }

        SelectionBox(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = box.messageBoxHeight(box.defaultMessageLine) + padding.medium),
        ) {
            ClassicButton(
                focused = selection == 0,
                onClick = { navController.navigate(Screen.ToBeBorn1) },
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
        }
    }
}
