@file:OptIn(ExperimentalWasmJsInterop::class)

package io.github.jtaeyeon05.why_web.util

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny
import kotlin.js.JsName
import kotlin.js.definedExternally


@JsName("window")
external object BrowserWindow {
    val location: WindowLocation
    val history: History
    val parent: Parent
    fun addEventListener(type: String, listener: (WebKeyboardEvent) -> Unit)
    fun removeEventListener(type: String, listener: (WebKeyboardEvent) -> Unit)
    fun addEventListener(type: String, listener: (WebMessageEvent) -> Unit)
    fun removeEventListener(type: String, listener: (WebMessageEvent) -> Unit)
}

@JsName("JSON")
external object WebJSON {
    fun parse(text: String): JsAny
    fun stringify(obj: JsAny?): String
}

external interface WindowLocation {
    val origin: String
    val hash: String
}

external interface History {
    // fun pushState(data: JsAny?, title: String, url: String? = definedExternally)
    fun replaceState(data: JsAny?, title: String, url: String? = definedExternally)
    // fun back()
    // fun forward()
    // fun go(delta: Int)
    // val length: Int
    // val state: JsAny?
}

external interface Parent {
    fun postMessage(message: JsAny?, targetOrigin: String)
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

external interface WebMessageEvent {
    val data: JsAny?
    // val origin: String
    // val source: JsAny?
    // val type: String
}

@JsName("URLSearchParams")
external class URLSearchParams(init: String) {
    @JsName("get")
    fun getEncoded(name: String): String?
    fun has(name: String): Boolean
}

external fun encodeURIComponent(str: String): String

external fun decodeURIComponent(str: String): String

// KeyboardEventManager GlobalEvent를 위한 인터페이스
external interface IframeKeyMessage : JsAny {
    val type: String?
    val keyCode: String?
    val status: String?
}
