package io.github.jtaeyeon05.why_web.util

import kotlin.js.js


external fun openTabInNewTab(url: String)

fun showCompose() {
    js(
        """
            document.getElementById('compose-root').style.opacity = 1;
            document.getElementById('compose-root').style.zIndex = 2;
        """
    )
}

fun stopLoader() {
    js("if (window.stopLoader) window.stopLoader();")
}
