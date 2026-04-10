package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavController
import io.github.jtaeyeon05.why_web.ui.foundation.LocalKeyboardEventManager
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.ui.widget.BouncingEmoji
import io.github.jtaeyeon05.why_web.ui.widget.MiniSquareButton
import io.github.jtaeyeon05.why_web.util.emojiGroups
import io.github.jtaeyeon05.why_web.util.nextGaussian
import io.github.vinceglb.confettikit.compose.ConfettiKit
import io.github.vinceglb.confettikit.core.Angle
import io.github.vinceglb.confettikit.core.Party
import io.github.vinceglb.confettikit.core.Position
import io.github.vinceglb.confettikit.core.Rotation
import io.github.vinceglb.confettikit.core.Spread
import io.github.vinceglb.confettikit.core.emitter.Emitter
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.random.Random
import kotlin.time.Duration


@Composable
fun BoxScope.EasterEgg1Screen(
    navController: NavController,
) {
    LocalLayoutConstraints.current.run {
        val keyboardManager = LocalKeyboardEventManager.current

        // Confetti
        val party = Party(
            speed = 50f,
            maxSpeed = 100f,
            damping = 0.93f,
            spread = Spread.WIDE,
            colors = List(10) { index ->
                Color.hsl(
                    hue = 36f * (index % 10),
                    saturation = 0.9f,
                    lightness = 0.8f
                ).toArgb()
            },
            emitter = Emitter(duration = Duration.INFINITE).perSecond(100),
        )

        ConfettiKit(
            modifier = Modifier.fillMaxSize(),
            parties = listOf(
                party.copy(
                    angle = Angle.BOTTOM,
                    position = Position.Between(
                        Position.Relative(0.0, 0.0),
                        Position.Relative(1.0, 0.0),
                    ),
                    rotation = Rotation.disabled(),
                ), // 위 -> 아래
                party.copy(
                    angle = Angle.TOP,
                    position = Position.Between(
                        Position.Relative(0.0, 1.0),
                        Position.Relative(1.0, 1.0),
                    ),
                    rotation = Rotation.enabled(),
                ), // 아래 -> 위
            ),
        )

        // BouncingEmoji
        var bouncingEmojiKey by rememberSaveable { mutableStateOf(0) }
        LaunchedEffect(Unit) {
            while (true) {
                delay(2500)
                bouncingEmojiKey++
            }
        }

        key(bouncingEmojiKey) {
            val emojiGroup = remember { emojiGroups.random() }
            repeat(50) {
                val bottomAlignment = remember { listOf(Alignment.BottomStart, Alignment.BottomEnd).random() }
                val bottomInitialDirection = remember {
                    if (bottomAlignment == Alignment.BottomStart) {
                        Random.nextGaussian(
                            mean = 0.35 * PI,
                            stdDev = 0.75,
                            min = 0.2 * PI,
                            max = 0.5 * PI
                        ).toFloat()
                    } else {
                        Random.nextGaussian(
                            mean = 0.65 * PI,
                            stdDev = 0.75,
                            min = 0.5 * PI,
                            max = 0.8 * PI
                        ).toFloat()
                    }
                }
                BouncingEmoji(
                    modifier = Modifier.fillMaxSize(),
                    emoji = remember { emojiGroup.random() },
                    alignment = bottomAlignment,
                    initialDirection = bottomInitialDirection,
                    initialMagnitude = remember { screen.base * (0.005f + 0.025f * Random.nextFloat()) },
                    accelDirection = 1.5f * PI.toFloat(),
                    accelMagnitude = screen.base * 0.0003f,
                    isDragEnabled = false,
                ) // 위 -> 아래
                val topAlignment = remember { listOf(Alignment.TopStart, Alignment.TopEnd).random() }
                val topInitialDirection = remember {
                    if (topAlignment == Alignment.TopStart) {
                        Random.nextGaussian(
                            mean = 1.65 * PI,
                            stdDev = 0.75,
                            min = 1.5 * PI,
                            max = 1.8 * PI
                        ).toFloat()
                    } else {
                        Random.nextGaussian(
                            mean = 1.35 * PI,
                            stdDev = 0.75,
                            min = 1.2 * PI,
                            max = 1.5 * PI
                        ).toFloat()
                    }
                }
                BouncingEmoji(
                    modifier = Modifier.fillMaxSize(),
                    emoji = remember { emojiGroup.random() },
                    alignment = topAlignment,
                    initialDirection = topInitialDirection,
                    initialMagnitude = remember { screen.base * (0.005f + 0.025f * Random.nextFloat()) },
                    accelDirection = 0.5f * PI.toFloat(),
                    accelMagnitude = screen.base * 0.0003f,
                    isDragEnabled = false,
                ) // 아래 -> 위
            }
        }

        // Close
        val canBack by derivedStateOf { navController.previousBackStackEntry != null }

        LaunchedEffect(canBack) {
            if (canBack) {
                keyboardManager.events.collect { webKeyEvent ->
                    if (webKeyEvent.isCancelPressed) {
                        navController.popBackStack()
                    }
                }
            }
        }

        if (canBack) {
            MiniSquareButton(
                modifier = Modifier.align(Alignment.TopStart),
                text = "X",
                onClick = { navController.popBackStack() },
            )
        }
    }
}
