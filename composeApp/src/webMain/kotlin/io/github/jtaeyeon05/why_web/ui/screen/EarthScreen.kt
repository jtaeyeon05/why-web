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
import androidx.navigation.NavController
import io.github.jtaeyeon05.why_web.navigation.Screen
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import kotlinx.coroutines.delay


@Composable
fun BoxScope.EarthScreen(
    navController: NavController,
    screen: Screen.Earth,
) {
    LocalLayoutConstraints.current.run {
        // 아직 다 안 만듦

        val earth = listOf("🌍", "🌎", "🌏")
        var earthIndex by remember { mutableStateOf(0) }

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
            fontSize = typography.smallFontSize.sp,
            lineHeight = typography.smallLineHeight.sp,
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = earth[earthIndex],
            fontSize = typography.superLargeFontSize.sp,
            lineHeight = typography.superLargeLineHeight.sp,
        )
    }
}
