package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavController
import io.github.jtaeyeon05.why_web.data.Avatar
import io.github.jtaeyeon05.why_web.navigation.Screen
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.ui.foundation.rememberAnimatedText
import io.github.jtaeyeon05.why_web.ui.widget.ClassicButton
import io.github.jtaeyeon05.why_web.ui.widget.MessageBox
import io.github.jtaeyeon05.why_web.ui.widget.SelectionBox
import io.github.vinceglb.confettikit.compose.ConfettiKit
import io.github.vinceglb.confettikit.core.Angle
import io.github.vinceglb.confettikit.core.Party
import io.github.vinceglb.confettikit.core.Position
import io.github.vinceglb.confettikit.core.Spread
import io.github.vinceglb.confettikit.core.emitter.Emitter
import io.github.vinceglb.confettikit.core.models.Shape
import kotlinx.coroutines.delay
import kotlin.time.Duration


@Composable
fun BoxScope.NotToBeBornScreen(
    navController: NavController,
) {
    LocalLayoutConstraints.current.run {
        // Confetti
        var showConfetti by rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            delay(2_500)
            showConfetti = true
        }
        if (showConfetti) {
            ConfettiKit(
                modifier = Modifier.fillMaxSize(),
                parties = listOf(
                    Party(
                        speed = 50f,
                        maxSpeed = 100f,
                        damping = 0.93f,
                        angle = Angle.TOP,
                        spread = Spread.WIDE,
                        colors = List(10) { index ->
                            Color.hsl(
                                hue = 36f * (index % 10),
                                saturation = 0.9f,
                                lightness = 0.8f
                            ).toArgb()
                        },
                        shapes = listOf(Shape.Circle),
                        position = Position.Relative(0.5, 1.0),
                        emitter = Emitter(duration = Duration.INFINITE).perSecond(500),
                    )
                ),
            )
        }

        // Message
        var textKey by rememberSaveable { mutableStateOf(0) }
        val message = rememberAnimatedText(
            text = "태어나지 않기로 했다!",
            key = textKey,
        )
        MessageBox(
            modifier = Modifier.align(Alignment.BottomCenter),
            message = message,
            avatar = Avatar.System,
            onReplay = { textKey += 1 },
        )

        // Selection
        var showSelection by rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            delay(2_500)
            showSelection = true
        }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = box.messageBoxHeight(box.defaultMessageLine) + padding.medium),
            visible = showSelection,
            enter = fadeIn(tween(1_000)),
            exit = fadeOut(tween(1_000))
        ) {
            SelectionBox {
                ClassicButton(
                    modifier = Modifier.fillMaxWidth(),
                    focused = true,
                    onClick = { navController.navigate(Screen.Start) },
                ) {
                    Text("다시하기")
                }
            }
        }
    }
}
