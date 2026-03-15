package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.NavController
import io.github.jtaeyeon05.why_web.navigation.Screen
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.ui.foundation.TextSize
import kotlinx.coroutines.delay


@Composable
fun BoxScope.EarthScreen(
    navController: NavController,
    screen: Screen.Earth,
) {
    LocalLayoutConstraints.current.run {
        val earth = listOf("🌍", "🌎", "🌏")
        var earthIndex by remember { mutableStateOf(0) }

        val density = LocalDensity.current
        val earthSize = TextSize(
            size = this.screen.base * 0.32f,
            lineHeight = 1f,
            density = density
        )

        LaunchedEffect(Unit) {
            var count = 0
            while (true) {
                count++

                if (count % 5 == 0) {
                    earthIndex++
                    if (earthIndex == 3) earthIndex = 0
                }
                if (count == 100) {
                    screen.destination?.let {
                        navController.navigate(
                            route = Screen.fromIdentifier(screen.destination)
                        )
                    }
                }

                delay(100)
            }
        }

        Text(
            modifier = Modifier.align(Alignment.BottomCenter),
            text = "destination: ${screen.destination}",
            fontSize = typography.small.sp,
            lineHeight = typography.small.lineSp,
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = earth[earthIndex],
            fontSize = earthSize.sp,
            lineHeight = earthSize.lineSp,
        )
    }
}
