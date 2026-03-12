package io.github.jtaeyeon05.why_web.ui.foundation

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


class KeyboardEventManager {
    private val _events = MutableSharedFlow<WebKeyEvent>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    fun dispatch(event: WebKeyEvent) {
        _events.tryEmit(event)
    }
}

val LocalKeyboardEventManager = staticCompositionLocalOf { KeyboardEventManager() }
