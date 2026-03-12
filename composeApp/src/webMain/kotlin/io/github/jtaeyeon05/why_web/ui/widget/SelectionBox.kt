package io.github.jtaeyeon05.why_web.ui.widget

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints


// 스크롤 불가능한 SelectionBox
@Composable
fun SelectionBox(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    LocalLayoutConstraints.current.run {
        Column(
            modifier = modifier
                .width(IntrinsicSize.Max)
                .sizeIn(
                    minWidth = box.selectionBoxWidth.min,
                    maxWidth = box.selectionBoxWidth.max,
                )
                .border(
                    width = inset.borderWidth,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RectangleShape
                )
                .padding(inset.borderWidth + padding.medium),
            content = content
        )
    }
}

// 스크롤 가능한 SelectionBox
@Composable
fun SelectionBox(
    modifier: Modifier = Modifier,
    scrollTo: Int = 0,  // 변경시 해당 버튼으로 스크롤 이동
    line: Int = LocalLayoutConstraints.current.box.defaultSelectionLine,  // 한 번에 보이는 버튼 개수
    content: @Composable ColumnScope.() -> Unit,
) {
    LocalLayoutConstraints.current.run {
        val scrollState = rememberScrollState()
        val itemHeightPx = with(LocalDensity.current) { box.buttonHeight.toPx() }
        LaunchedEffect(scrollTo) {
            scrollState.scrollTo((itemHeightPx * (scrollTo - 1).coerceAtLeast(0)).toInt())
        }

        Column(
            modifier = modifier
                .width(IntrinsicSize.Max)
                .sizeIn(
                    minWidth = box.selectionBoxWidth.min,
                    maxWidth = box.selectionBoxWidth.max,
                    minHeight = box.selectionBoxHeight(line),
                    maxHeight = box.selectionBoxHeight(line),
                )
                .border(
                    width = inset.borderWidth,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RectangleShape
                )
                .padding(inset.borderWidth + padding.medium)
                .verticalScroll(scrollState),
            content = content
        )
    }
}
