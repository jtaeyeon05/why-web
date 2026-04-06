@file:OptIn(ExperimentalWasmJsInterop::class)

package io.github.jtaeyeon05.why_web.util

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js


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

fun enableIframesToAutoplay() {
    js(
        """
            const iframes = document.querySelectorAll("iframe");
            iframes.forEach(iframe => {
                iframe.setAttribute("allow", "autoplay");
                console.log("YouTube iframe allow attribute injected!");
            });
        """
    )
}

fun focusIframes() {
    js(
        """
            const iframes = document.querySelectorAll("iframe");
            iframes.forEach(iframe => {
                iframe.focus();
                if (iframe.contentWindow) {
                    iframe.contentWindow.focus();
                }
                console.log("Iframe focused via JS!");
            });
        """
    )
}
