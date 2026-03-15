package io.github.jtaeyeon05.why_web.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.em
import io.github.jtaeyeon05.why_web.data.Avatar
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints


@Composable
fun MessageBox(
    modifier: Modifier = Modifier,
    message: String,
    avatar: Avatar? = null,
    line: Int = LocalLayoutConstraints.current.box.defaultMessageLine,
) {
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
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(typography.medium.lineDp),
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
            }
            val messageLine = if (avatar == null) line else line - 1
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(typography.medium.lineDp * messageLine),
                text = message,
                fontSize = typography.medium.sp,
                lineHeight = typography.medium.lineSp,
                maxLines = messageLine,
            )
        }
    }
}
