package io.github.jtaeyeon05.why_web.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ClassicButton(
    focused: Boolean,
    onClick: () -> Unit,
    onFocused: () -> Unit,
    layoutConstraints: LayoutConstraints,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Selection Color Transition")
    val selectionColor by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.primaryContainer,
        targetValue = MaterialTheme.colorScheme.background,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Selection Color"
    )

    layoutConstraints.run {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .height(box.buttonHeight)
                .onPointerEvent(PointerEventType.Enter) { onFocused() }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { onFocused() },
                        onTap = { onClick() }
                    )
                },
            color = if (focused) selectionColor else MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding.small),
                contentAlignment = Alignment.CenterStart
            ) {
                val textStyle = LocalTextStyle.current.copy(
                    color = LocalContentColor.current,
                    fontSize = typography.mediumFontSize.sp,
                    textAlign = TextAlign.Start,
                    lineHeight = typography.mediumLineHeight.sp,
                )

                CompositionLocalProvider(
                    LocalTextStyle provides textStyle,
                    content = content
                )
            }
        }
    }
}
