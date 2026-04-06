package io.github.jtaeyeon05.why_web.ui.foundation

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow


class KeyboardEventManager {
    // 일반적인 키보드 이벤트
    private val _events = MutableSharedFlow<WebKeyEvent>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    // 비활성화되었거나, iframe에서 포커스되었을 때도 발생하는 키보드 이벤트
    private val _globalEvents = MutableSharedFlow<WebKeyEvent>(extraBufferCapacity = 64)
    val globalEvents = _globalEvents.asSharedFlow()

    private val _isEnabled = MutableStateFlow(true)
    val isEnabled = _isEnabled.asStateFlow()

    fun dispatch(event: WebKeyEvent) {
        _globalEvents.tryEmit(event)
        if (_isEnabled.value) _events.tryEmit(event)
    }

    fun iframeDispatch(event: WebKeyEvent) {
        _globalEvents.tryEmit(event)
    }

    fun enable() {
        _isEnabled.value = true
    }

    fun disable() {
        _isEnabled.value = false
    }
}

val LocalKeyboardEventManager = staticCompositionLocalOf { KeyboardEventManager() }
