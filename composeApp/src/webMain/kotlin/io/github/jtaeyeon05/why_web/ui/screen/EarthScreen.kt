package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import io.github.jtaeyeon05.why_web.navigation.Screen
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.ui.foundation.TextSize
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.random.Random


@Composable
fun BoxScope.EarthScreen(
    navController: NavController,
    screen: Screen.Earth,
) {
    LocalLayoutConstraints.current.run {
        val density = LocalDensity.current
        val earthSize = TextSize(
            size = this.screen.base * 0.32f,
            lineHeight = 1f,
            density = density
        )
        val moonSize = TextSize(
            size = this.screen.base * 0.12f,
            lineHeight = 1f,
            density = density
        )
        val starSize = TextSize(
            size = this.screen.base * 0.02f,
            lineHeight = 1f,
            density = density
        )

        val earth = listOf("🌍", "🌎", "🌏")
        val moon = listOf("🌑", "🌒", "🌓", "🌔", "🌕", "🌖", "🌗", "🌘")

        var earthIndex by remember { mutableStateOf(0) }
        var moonIndex by remember { mutableStateOf(Random.nextInt(moon.size)) }
        val starPosition by remember(this) {
            mutableStateOf(
                List(20) {
                    this.screen.run {
                        Pair(
                            cos(Random.nextFloat() * PI) * 0.45f * width,
                            cos(Random.nextFloat() * PI) * 0.45f * height
                        )
                    }
                }
            )
        }
        val starSettings = remember {
            List(20) {
                this.screen.run {
                    val duration = Random.nextInt(500, 2000)
                    val offset = Random.nextInt(0, duration)
                    Pair(duration, offset)
                }
            }
        }
        val starTransition = rememberInfiniteTransition()
        val starAlpha = starSettings.map { (duration, offset) ->
            starTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = duration,
                        easing = EaseInOutCubic
                    ),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(offset)
                ),
            )
        }

        LaunchedEffect(Unit) {
            var count = 0
            while (true) {
                count++
                if (count % 5 == 0) {
                    earthIndex++
                    if (earthIndex == earth.size) earthIndex = 0
                }
                if (count % 20 == 0) {
                    moonIndex++
                    if (moonIndex == moon.size) moonIndex = 0
                }
                if (count == 50) {
                    screen.destination?.let {
                        navController.navigate(
                            route = Screen.fromIdentifier(screen.destination)
                        )
                    }
                }
                delay(100)
            }
        }

        for (i in 0 ..< 20) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(
                        x = starPosition[i].first,
                        y = starPosition[i].second
                    )
                    .alpha(starAlpha[i].value),
                text = if (i % 5 != 0) "⭐️" else "🌟",
                fontSize = starSize.sp,
                lineHeight = starSize.lineSp,
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(
                    x = earthSize.dp * 0.5f + this.screen.base * 0.08f,
                    y = -(earthSize.dp * 0.5f + this.screen.base * 0.08f)
                ),
            text = moon[moonIndex],
            fontSize = moonSize.sp,
            lineHeight = moonSize.lineSp,
        )
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { /* TODO */ }
                    )
                },
            text = earth[earthIndex],
            fontSize = earthSize.sp,
            lineHeight = earthSize.lineSp,
        )
    }
}
