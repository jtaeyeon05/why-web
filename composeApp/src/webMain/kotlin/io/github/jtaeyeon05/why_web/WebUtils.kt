package io.github.jtaeyeon05.why_web

import kotlin.js.js


fun stopLoader() {
    js("if (window.stopLoader) window.stopLoader();")
}
