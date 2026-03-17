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
    key: Any? = Unit,
): String {
    var animatedText by rememberSaveable(text) { mutableStateOf("") }
    LaunchedEffect(text, startDelay, intervalDelay, key) {
        animatedText = ""
        delay(startDelay)
        for (i in text.indices) {
            animatedText = text.take(i + 1)
            delay(intervalDelay)
        }
    }
    return animatedText
}

@Composable
fun rememberAnimatedText(
    vararg texts: String,
    startDelay: Long = 1_000,
    intervalDelay: Long = 100,
    key: Any? = Unit,
): String {
    val textsKey = texts.contentDeepHashCode()
    var animatedText by rememberSaveable(textsKey) { mutableStateOf("") }
    LaunchedEffect(textsKey, startDelay, intervalDelay, key) {
        animatedText = ""
        for (text in texts) {
            delay(startDelay)
            for (i in text.indices) {
                animatedText = text.take(i + 1)
                delay(intervalDelay)
            }
        }
    }
    return animatedText
}
