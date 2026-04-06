package io.github.jtaeyeon05.why_web.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints


@Composable
fun MiniSquareButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    LocalLayoutConstraints.current.run {
        Box(
            modifier = modifier
                .size(box.mediumBox)
                .background(MaterialTheme.colorScheme.background)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onClick() },
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = LocalTextStyle.current.copy(
                    fontSize = typography.medium.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = typography.medium.lineSp,
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None
                    ),
                ),
            )
        }
    }
}
