package io.github.jtaeyeon05.why_web

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


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
