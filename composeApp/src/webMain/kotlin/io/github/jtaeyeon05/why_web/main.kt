package io.github.jtaeyeon05.why_web

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.github.jtaeyeon05.why_web.ui.Mona12
import io.github.jtaeyeon05.why_web.util.showCompose
import io.github.jtaeyeon05.why_web.util.stopLoader
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.configureWebResources


@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    configureWebResources {
        resourcePathMapping { path -> "./$path" }
    }

    ComposeViewport(viewportContainerId = "compose-root") {
        if (Mona12 != null) {
            LaunchedEffect(Unit) {
                showCompose()
                delay(10_000)
                stopLoader()
            }
        }
        App()
    }
}
