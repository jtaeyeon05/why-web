package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import io.github.jtaeyeon05.why_web.data.Avatar
import io.github.jtaeyeon05.why_web.navigation.Screen
import io.github.jtaeyeon05.why_web.ui.foundation.LocalKeyboardEventManager
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.ui.foundation.rememberAnimatedText
import io.github.jtaeyeon05.why_web.ui.widget.ClassicButton
import io.github.jtaeyeon05.why_web.ui.widget.MessageBox
import io.github.jtaeyeon05.why_web.ui.widget.SelectionBox
import io.github.jtaeyeon05.why_web.viewmodel.AppViewModel


@Composable
fun BoxScope.ToBeBorn2Screen(
    navController: NavController,
    viewModel: AppViewModel,
) {
    LocalLayoutConstraints.current.run {
        // Message
        val message = rememberAnimatedText("어떤 이름으로 태어나시겠습니까?")
        MessageBox(
            modifier = Modifier.align(Alignment.BottomCenter),
            message = message,
            avatar = Avatar.God,
        )

        // Selection
        val keyboardManager = LocalKeyboardEventManager.current
        var selectionCol by rememberSaveable { mutableStateOf((viewModel.model.value.name ?: 0) / 4) }
        var selectionRow by rememberSaveable { mutableStateOf((viewModel.model.value.name ?: 0) % 4) }
        LaunchedEffect(Unit) {
            keyboardManager.events.collect { webKeyEvent ->
                if (webKeyEvent.isUpPressed) {
                    selectionCol = (selectionCol - 1).coerceIn(0..< 6)
                } else if (webKeyEvent.isDownPressed) {
                    selectionCol = (selectionCol + 1).coerceIn(0..< 6)
                } else if (webKeyEvent.isLeftPressed) {
                    selectionRow = (selectionRow - 1).coerceIn(0..< 2)
                } else if (webKeyEvent.isRightPressed) {
                    selectionRow = (selectionRow + 1).coerceIn(0..< 2)
                }
            }
        }

        SelectionBox(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = box.messageBoxHeight(box.defaultMessageLine) + padding.medium),
        ) {
            Column {
                for (col in 0 until 6) {
                    Row {
                        for (row in 0 until 2) {
                            ClassicButton(
                                modifier = Modifier.weight(1f),
                                focused = selectionCol == col && selectionRow == row,
                                onClick = {
                                    viewModel.updateName(col * 2 + row)
                                    navController.navigate(
                                        route = Screen.Earth(
                                            destination = Screen.Born.identifier(),
                                        )
                                    )
                                },
                                onFocused = {
                                    selectionCol = col
                                    selectionRow = row
                                },
                            ) {
                                Text(Avatar.Names[col * 2 + row])
                            }
                        }
                    }
                }
            }
        }
    }
}
