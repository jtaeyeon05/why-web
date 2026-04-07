package io.github.jtaeyeon05.why_web.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import io.github.jtaeyeon05.why_web.data.Avatar
import io.github.jtaeyeon05.why_web.ui.foundation.LocalKeyboardEventManager
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.ui.foundation.WebKey
import kotlin.math.ceil


@Composable
fun MessageBox(
    modifier: Modifier = Modifier,
    message: String,
    avatar: Avatar? = null,
    line: Float = LocalLayoutConstraints.current.box.defaultMessageLine,
    onReplay: (() -> Unit)? = null,
) {
    val keyboardManager = LocalKeyboardEventManager.current
    LaunchedEffect(onReplay) {
        if (onReplay == null) return@LaunchedEffect
        keyboardManager.events.collect { webKeyEvent ->
            if (webKeyEvent.isPressed(WebKey.KEY_L)) {
                onReplay()
            }
        }
    }

    LocalLayoutConstraints.current.run {
        Column(
            modifier = modifier
                .width(box.messageBoxWidth)
                .height(box.messageBoxHeight(line))
                .background(MaterialTheme.colorScheme.background)
                .border(
                    width = inset.borderWidth,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RectangleShape
                )
                .padding(inset.borderWidth + padding.medium)
        ) {
            if (avatar != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(typography.medium.lineDp),
                    horizontalArrangement = Arrangement.spacedBy(padding.medium),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("[")
                            }
                            if (avatar.avatar != null) {
                                append(" ")
                                append(avatar.avatar)
                            }
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(" ${avatar.name} ]")
                            }
                        },
                        fontSize = typography.medium.sp,
                        lineHeight = typography.medium.lineSp,
                        maxLines = 1,
                    )
                    if (onReplay != null) {
                        Text(
                            modifier = Modifier
                                .fillMaxHeight()
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = { onReplay() }
                                    )
                                },
                            text = "↺ L",
                            color = LocalContentColor.current.copy(alpha = 0.25f),
                            fontSize = typography.small.sp,
                            lineHeight = typography.small.lineSp,
                            maxLines = 1,
                        )
                    }
                }
            }
            val messageLine = if (avatar == null) line else line - 1f
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(typography.medium.lineDp * messageLine),
                text = message,
                fontSize = typography.medium.sp,
                lineHeight = typography.medium.lineSp,
                maxLines = ceil(messageLine).toInt(),
            )
        }
    }
}
