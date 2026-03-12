package io.github.jtaeyeon05.why_web.ui.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay


@Composable
fun rememberAnimatedText(
    text: String,
    startDelay: Long = 1_000,
    intervalDelay: Long = 100,
): String {
    var animatedText by rememberSaveable(text) { mutableStateOf("") }

    LaunchedEffect(text, startDelay, intervalDelay) {
        animatedText = ""
        delay(startDelay)

        for (i in text.indices) {//TODO
            animatedText = text.take(i + 1)
            delay(intervalDelay)
        }
    }

    return animatedText
}
