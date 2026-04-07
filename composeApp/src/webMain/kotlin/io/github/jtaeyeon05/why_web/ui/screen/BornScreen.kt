package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
fun BoxScope.BornScreen(
    navController: NavController,
    viewModel: AppViewModel,
) {
    LocalLayoutConstraints.current.run {
        // Message
        var texts by rememberSaveable { mutableStateOf(arrayOf("태어났다!", "어떤 행동을 할까?")) }
        var textsKey by rememberSaveable { mutableStateOf(0) }
        val message = rememberAnimatedText(
            texts = texts,
            key = textsKey,
        )

        MessageBox(
            modifier = Modifier.align(Alignment.BottomCenter),
            message = message,
            avatar = Avatar.user(
                avatar = viewModel.model.value.avatar,
                name = viewModel.model.value.name,
            ),
            onReplay = { textsKey += 1 },
        )

        // Selection
        val keyboardManager = LocalKeyboardEventManager.current
        var selection by rememberSaveable { mutableStateOf(0) }
        LaunchedEffect(Unit) {
            keyboardManager.events.collect { webKeyEvent ->
                if (webKeyEvent.isUpPressed) {
                    selection = (selection - 1).coerceIn(0 ..< 3)
                } else if (webKeyEvent.isDownPressed) {
                    selection = (selection + 1).coerceIn(0 ..< 3)
                }
            }
        }

        SelectionBox(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = box.messageBoxHeight(box.defaultMessageLine) + padding.medium),
        ) {
            ClassicButton(
                modifier = Modifier.fillMaxWidth(),
                focused = selection == 0,
                onClick = { navController.navigate(Screen.Web) },
                onFocused = { selection = 0 },
            ) {
                Text("웹 서핑 하기")
            }
            ClassicButton(
                modifier = Modifier.fillMaxWidth(),
                focused = selection == 1,
                onClick = { navController.navigate(Screen.Draw()) },
                onFocused = { selection = 1 },
            ) {
                Text("벽에 낙서하기")
            }
            ClassicButton(
                modifier = Modifier.fillMaxWidth(),
                focused = selection == 2,
                onClick = {
                    texts = arrayOf(
                        "이딴 프로젝트에 더 시간 쓰기 아깝다고 한다!",
                        "이 프로젝트에만 벌써 30개가 넘는 코드 파일에 3,000줄이 넘는 코드를 작성했다!",
                        "빨리 버리고 실용적인 프로젝트로 넘어가야겠다.",
                        "어떤 행동을 할까?"
                    )
                    textsKey += 1
                },
                onFocused = { selection = 2 },
            ) {
                Text("귀찮아서 개발하지 않았다!")
            }
        }
    }
}
