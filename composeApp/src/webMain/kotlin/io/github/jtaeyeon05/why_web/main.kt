package io.github.jtaeyeon05.why_web

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.coroutines.delay


@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        LaunchedEffect(Unit) {
            delay(10_000)
            stopLoader()
        }
        App()
    }
}
