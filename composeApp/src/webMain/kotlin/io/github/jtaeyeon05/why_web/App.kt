package io.github.jtaeyeon05.why_web

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun App() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "TEST",
                color = Color.White,
                style = MaterialTheme.typography.displayLarge
            )
        }
    }
}
