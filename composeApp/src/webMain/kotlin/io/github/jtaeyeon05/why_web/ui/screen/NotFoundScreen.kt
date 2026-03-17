package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import io.github.jtaeyeon05.why_web.data.Avatar
import io.github.jtaeyeon05.why_web.navigation.Screen
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.ui.foundation.rememberAnimatedText
import io.github.jtaeyeon05.why_web.ui.widget.ClassicButton
import io.github.jtaeyeon05.why_web.ui.widget.MessageBox
import io.github.jtaeyeon05.why_web.ui.widget.SelectionBox


@Composable
fun BoxScope.NotFoundScreen(
    navController: NavController,
    screen: Screen.NotFound,
) {
    LocalLayoutConstraints.current.run {
        // Message
        var textKey by rememberSaveable { mutableStateOf(0) }
        val message = rememberAnimatedText(
            text = "\"${screen.route}\"라는 주소는 존재하지 않는데?",
            key = textKey,
        )
        MessageBox(
            modifier = Modifier.align(Alignment.BottomCenter),
            message = message,
            avatar = Avatar.System,
            onReplay = { textKey += 1 },
        )

        // Selection
        SelectionBox(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = box.messageBoxHeight(box.defaultMessageLine) + padding.medium),
        ) {
            ClassicButton(
                modifier = Modifier.fillMaxWidth(),
                focused = true,
                onClick = { navController.navigate(Screen.Start) },
            ) {
                Text("다시하기")
            }
        }
    }
}
