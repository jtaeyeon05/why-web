package io.github.jtaeyeon05.why_web.ui.foundation

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


class KeyboardEventManager {
    private val _events = MutableSharedFlow<WebKeyEvent>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    private var isKilled = false

    fun dispatch(event: WebKeyEvent) {
        if (isKilled) return
        _events.tryEmit(event)
    }

    fun kill() {
        isKilled = true
    }

    fun revive() {
        isKilled = false
    }
}

val LocalKeyboardEventManager = staticCompositionLocalOf { KeyboardEventManager() }
