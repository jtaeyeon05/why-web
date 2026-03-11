package io.github.jtaeyeon05.why_web.util

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsName


@JsName("URLSearchParams")
external class URLSearchParams(init: String) {
    fun get(name: String): String?
    fun has(name: String): Boolean
}

@JsName("window")
external object BrowserWindow {
    @JsName("location")
    object Location {
        val hash: String
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("str => encodeURIComponent(str)")
external fun encodeURIComponent(str: String): String

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("str => decodeURIComponent(str)")
external fun decodeURIComponent(str: String): String

external fun openTabInNewTab(url: String)
