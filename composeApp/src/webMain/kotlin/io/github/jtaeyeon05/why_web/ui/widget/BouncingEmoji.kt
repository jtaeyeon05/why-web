package io.github.jtaeyeon05.why_web.ui.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoxScope.BouncingEmoji(
    emoji: String,
    emojiSize: Dp = LocalLayoutConstraints.current.typography.large.dp,
    alignment: Alignment = Alignment.Center,
    initialDirection: Float = remember { 2f * PI.toFloat() * Random.nextFloat() },
    initialMagnitude: Dp = with(LocalLayoutConstraints.current) { remember { screen.base * (0.015f + 0.015f * Random.nextFloat()) } },
    accelDirection: Float = remember { 2f * PI.toFloat() * Random.nextFloat() },
    accelMagnitude: Dp = with(LocalLayoutConstraints.current) { remember { screen.base * (0.00015f + 0.00015f * Random.nextFloat()) } },
    ticksPerSecond: Long = 60L,
) {
    LocalLayoutConstraints.current.run {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            var velocityX by remember(maxWidth, maxHeight, screen.base, emojiSize, alignment, initialDirection, initialMagnitude) { mutableStateOf(cos(initialDirection) * initialMagnitude) }
            var velocityY by remember(maxWidth, maxHeight, screen.base, emojiSize, alignment, initialDirection, initialMagnitude) { mutableStateOf(sin(initialDirection) * initialMagnitude) }

            var offsetX by remember(maxWidth, maxHeight, screen.base, emojiSize, alignment, initialDirection, initialMagnitude) { mutableStateOf(0.dp) }
            var offsetY by remember(maxWidth, maxHeight, screen.base, emojiSize, alignment, initialDirection, initialMagnitude) { mutableStateOf(0.dp) }

            var isDragging by remember { mutableStateOf(false) }

            val isAlive by derivedStateOf {
                val safeArea = 0.50f * screen.base
                var result = true

                // 상하 검사
                when (alignment) {
                    Alignment.TopStart, Alignment.TopCenter, Alignment.TopEnd -> {
                        if (offsetY < -emojiSize - safeArea || offsetY > maxHeight + emojiSize + safeArea) {
                            result = false
                        }
                    }
                    Alignment.CenterStart, Alignment.Center, Alignment.CenterEnd -> {
                        if (offsetY < -0.5f * maxHeight - emojiSize - safeArea || offsetY > 0.5f * maxHeight + emojiSize + safeArea) {
                            result = false
                        }
                    }
                    Alignment.BottomStart, Alignment.BottomCenter, Alignment.BottomEnd -> {
                        if (offsetY < -maxHeight - emojiSize - safeArea || offsetY > emojiSize + safeArea) {
                            result = false
                        }
                    }
                }

                // 좌우 검사
                when (alignment) {
                    Alignment.TopStart, Alignment.CenterStart, Alignment.BottomStart -> {
                        if (offsetX < -emojiSize - safeArea || offsetX > maxWidth + emojiSize + safeArea) {
                            result = false
                        }
                    }
                    Alignment.TopCenter, Alignment.Center, Alignment.BottomCenter -> {
                        if (offsetX < -0.5f * maxWidth - emojiSize - safeArea || offsetX > 0.5f * maxWidth + emojiSize + safeArea) {
                            result = false
                        }
                    }
                    Alignment.TopEnd, Alignment.CenterEnd, Alignment.BottomEnd -> {
                        if (offsetX < -maxWidth - emojiSize - safeArea || offsetX > emojiSize + safeArea) {
                            result = false
                        }
                    }
                }

                result
            }

            LaunchedEffect(isAlive, isDragging) {
                while (isAlive && !isDragging) {
                    velocityX += cos(accelDirection) * accelMagnitude
                    velocityY += sin(accelDirection) * accelMagnitude
                    offsetX += velocityX
                    offsetY -= velocityY
                    delay(1000L / ticksPerSecond)
                }
            }

            if (isAlive) {
                val density = LocalDensity.current

                Text(
                    modifier = Modifier
                        .size(emojiSize)
                        .align(alignment)
                        .offset(x = offsetX, y = offsetY)
                        .onDrag(
                            onDragStart = {
                                velocityX = 0.dp
                                velocityY = 0.dp
                                isDragging = true
                            },
                            onDragCancel = { isDragging = false },
                            onDragEnd = { isDragging = false },
                            onDrag = {
                                with(density) {
                                    offsetX += it.x.toDp()
                                    offsetY += it.y.toDp()
                                }
                            },
                        ),
                    text = emoji,
                    fontSize = with(density) { emojiSize.toSp() },
                    lineHeight = with(density) { emojiSize.toSp() },
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    minLines = 1,
                )
            }
        }
    }
}
