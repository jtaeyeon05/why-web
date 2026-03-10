package io.github.jtaeyeon05.why_web

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.ComposeViewport
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.configureWebResources
import org.jetbrains.compose.resources.preloadFont
import why_web.composeapp.generated.resources.Mona12
import why_web.composeapp.generated.resources.Mona12_Bold
import why_web.composeapp.generated.resources.Res
import kotlin.js.js


@OptIn(ExperimentalComposeUiApi::class, ExperimentalResourceApi::class)
fun main() {
    configureWebResources {
        resourcePathMapping { path -> "./$path" }
    }

    ComposeViewport(viewportContainerId = "compose-root") {
        /*LaunchedEffect(Unit) {
            delay(10_000)
            //stopLoader()
        }*/
        /*App()*/
        val fontA by preloadFont(Res.font.Mona12)
        val fontB by preloadFont(Res.font.Mona12_Bold, FontWeight.Bold)
        if (fontA != null && fontB != null) {
            LaunchedEffect(Unit) {
                sho()
                delay(10_000)
                stopLoader()
            }
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text("폰트다폰트1", fontFamily = FontFamily(fontA!!),
                    modifier = Modifier.align(Alignment.BottomEnd),
                    fontSize = 200.sp)
                Text("폰트다폰트2", fontFamily = FontFamily(fontA!!),
                    modifier = Modifier.align(Alignment.TopStart),
                    fontSize = 200.sp)
            }
        } else {
            Text("asda")
        }

        /*val fontFamilyResolver = LocalFontFamilyResolver.current
        LaunchedEffect(fontFamilyResolver, fontA) {
            if (fontA != null) {
                fontFamilyResolver.preload(FontFamily(fontA!!))
            }
        }*/
    }
}

fun sho() {
    js(
        """
            document.getElementById('compose-root').style.opacity = 1;
            document.getElementById('compose-root').style.zIndex = 2;
        """
    )
}
