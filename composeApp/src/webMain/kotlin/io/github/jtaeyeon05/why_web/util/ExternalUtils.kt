package io.github.jtaeyeon05.why_web.util

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsName


@JsName("window")
external object BrowserWindow {
    @JsName("location")
    val location: WindowLocation
    fun addEventListener(type: String, listener: (WebKeyboardEvent) -> Unit)
    // fun removeEventListener(type: String, listener: (WebKeyboardEvent) -> Unit)
}

@JsName("URLSearchParams")
external class URLSearchParams(init: String) {
    fun get(name: String): String?
    // fun has(name: String): Boolean
}

external interface WebKeyboardEvent {
    // @JsName("type") val type: String
    @JsName("code") val keyCode: String
    // @JsName("key") val keyValue: String
    // @JsName("repeat") val isRepeated: Boolean
    // @JsName("ctrlKey") val isControlKey: Boolean
    // @JsName("shiftKey") val isShiftKey: Boolean
    // @JsName("altKey") val isAltKey: Boolean
    // @JsName("metaKey") val isMetaKey: Boolean
}

external interface WindowLocation {
    val hash: String
}

@OptIn(ExperimentalWasmJsInterop::class)
// @JsFun("str => encodeURIComponent(str)")
external fun encodeURIComponent(str: String): String

@OptIn(ExperimentalWasmJsInterop::class)
// @JsFun("str => decodeURIComponent(str)")
external fun decodeURIComponent(str: String): String

external fun openTabInNewTab(url: String)
