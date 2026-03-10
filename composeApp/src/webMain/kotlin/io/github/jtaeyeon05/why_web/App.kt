package io.github.jtaeyeon05.why_web

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.FontLoadResult
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.configureWebResources
import org.jetbrains.compose.resources.preloadFont

import why_web.composeapp.generated.resources.Mona12
import why_web.composeapp.generated.resources.Mona12_Bold
import why_web.composeapp.generated.resources.Res


@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    AppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .safeContentPadding(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TEST",
                    style = MaterialTheme.typography.displayLarge
                )
            }
        }
    }
}
