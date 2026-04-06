package io.github.jtaeyeon05.why_web.util

import kotlin.js.JsName


@JsName("window")
external object BrowserWindow {
    @JsName("location")
    val location: WindowLocation
    fun addEventListener(type: String, listener: (WebKeyboardEvent) -> Unit)
    fun removeEventListener(type: String, listener: (WebKeyboardEvent) -> Unit)
}

@JsName("URLSearchParams")
external class URLSearchParams(init: String) {
    @JsName("get")
    fun getEncoded(name: String): String?
    fun has(name: String): Boolean
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
    val origin: String
    val hash: String
}

external fun encodeURIComponent(str: String): String

external fun decodeURIComponent(str: String): String
