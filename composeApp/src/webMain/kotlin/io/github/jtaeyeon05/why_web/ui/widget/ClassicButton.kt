package io.github.jtaeyeon05.why_web.ui.widget

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import io.github.jtaeyeon05.why_web.ui.foundation.LocalKeyboardEventManager
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ClassicButton(
    focused: Boolean,
    onClick: () -> Unit,
    onFocused: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Selection Color Transition")
    val selectionColor by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.primaryContainer,
        targetValue = MaterialTheme.colorScheme.background,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500,
                easing = EaseInOutCubic
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Selection Color"
    )

    val keyboardManager = LocalKeyboardEventManager.current
    LaunchedEffect(Unit) {
        keyboardManager.events.collect { webKeyEvent ->
            if (focused && webKeyEvent.isConfirmPressed) {
                onClick()
            }
        }
    }

    LocalLayoutConstraints.current.run {
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
