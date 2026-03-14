package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.ui.foundation.rememberAnimatedText
import io.github.jtaeyeon05.why_web.ui.widget.Avatar
import io.github.jtaeyeon05.why_web.ui.widget.MessageBox


@Composable
fun BoxScope.ToBeBorn1Screen(navController: NavController) {
    LocalLayoutConstraints.current.run {
        // Message
        val message = rememberAnimatedText("그러면 뭐로 태어나시겠습니까?")
        MessageBox(
            modifier = Modifier.align(Alignment.BottomCenter),
            message = message,
            avatar = Avatar.God,
        )
    }
}
