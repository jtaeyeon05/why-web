package io.github.jtaeyeon05.why_web.util

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js


@OptIn(ExperimentalWasmJsInterop::class)
fun showCompose() {
    js(
        """
            document.getElementById('compose-root').style.opacity = 1;
            document.getElementById('compose-root').style.zIndex = 2;
        """
    )
}

@OptIn(ExperimentalWasmJsInterop::class)
fun stopLoader() {
    js("if (window.stopLoader) window.stopLoader();")
}
