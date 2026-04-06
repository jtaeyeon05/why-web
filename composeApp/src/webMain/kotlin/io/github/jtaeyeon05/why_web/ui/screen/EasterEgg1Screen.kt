package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import io.github.jtaeyeon05.why_web.ui.foundation.LocalKeyboardEventManager
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.ui.widget.MiniSquareButton
import kotlin.math.ceil


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoxScope.EasterEgg1Screen(
    navController: NavController,
) {
    LocalLayoutConstraints.current.run {
        val columns = ceil(screen.width / box.smallBox).toInt().let { it / 2 * 2 + 2 }
        val rows = ceil(screen.height / box.smallBox).toInt().let { it / 2 * 2 + 2 }

        val boxMap = remember(columns, rows) {
            SnapshotStateList(columns) {
                SnapshotStateList(rows) {
                    0f
                }
            }
        }
        val boundsMap = remember(columns, rows) {
            Array(columns) {
                Array(rows) {
                    Rect.Zero
                }
            }
        }

        var isBrushed by rememberSaveable { mutableStateOf(true) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(columns, rows) {
                    var lastOverIndex: Pair<Int, Int>? = null
                    detectDragGestures(
                        onDrag = { change, _ ->
                            val touchPoint = change.position
                            boundsMap.forEachIndexed { xIndex, boundsColumn ->
                                boundsColumn.forEachIndexed { yIndex, bounds ->
                                    if (bounds.contains(touchPoint)) {
                                        if (lastOverIndex != xIndex to yIndex) {
                                            if (isBrushed) {
                                                if (xIndex - 1 in 0 ..< columns) boxMap[xIndex - 1][yIndex] = (boxMap[xIndex - 1][yIndex] + 0.25f).coerceAtMost(1f)
                                                if (xIndex + 1 in 0 ..< columns) boxMap[xIndex + 1][yIndex] = (boxMap[xIndex + 1][yIndex] + 0.25f).coerceAtMost(1f)
                                                if (yIndex - 1 in 0 ..< rows) boxMap[xIndex][yIndex - 1] = (boxMap[xIndex][yIndex - 1] + 0.25f).coerceAtMost(1f)
                                                if (yIndex + 1 in 0 ..< rows) boxMap[xIndex][yIndex + 1] = (boxMap[xIndex][yIndex + 1] + 0.25f).coerceAtMost(1f)
                                                boxMap[xIndex][yIndex] = (boxMap[xIndex][yIndex] + 0.5f).coerceAtMost(1f)
                                            } else {
                                                boxMap[xIndex][yIndex] = (boxMap[xIndex][yIndex] + 0.5f).coerceAtMost(1f)
                                            }
                                            lastOverIndex = xIndex to yIndex
                                        }
                                    }
                                }
                            }
                        },
                        onDragCancel = { lastOverIndex = null },
                        onDragEnd = { lastOverIndex = null },
                    )
                },
        ) {
            val keyboardManager = LocalKeyboardEventManager.current

            // Blocks
            for (x in -columns / 2 ..< columns / 2) {
                for (y in -rows / 2 ..< rows / 2) {
                    val xIndex = x + columns / 2
                    val yIndex = y + rows / 2
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(
                                x = (x + 0.5f) * box.smallBox,
                                y = (y + 0.5f) * box.smallBox,
                            )
                            .size(box.smallBox)
                            .onGloballyPositioned { layoutCoordinates ->
                                boundsMap[xIndex][yIndex] = layoutCoordinates.boundsInParent()
                            }
                            .background(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = boxMap[xIndex][yIndex]).compositeOver(MaterialTheme.colorScheme.background),
                            )
                            .border(
                                width = inset.thinBorderWidth,
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = RectangleShape
                            )
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        boxMap[xIndex][yIndex] = (boxMap[xIndex][yIndex] + 0.25f).coerceAtMost(1f)
                                    },
                                )
                            }
                    )
                }
            }

            // Guide
            Box(
                modifier = Modifier
                    .size(box.smallBox * 20 + inset.borderWidth)
                    .align(Alignment.Center)
                    .border(
                        width = inset.borderWidth,
                        color = MaterialTheme.colorScheme.secondary,
                    )
            )

            // TestText
            Text(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding.small),
                text = "columns: $columns, rows: $rows",
                style = LocalTextStyle.current.copy(
                    fontSize = typography.small.sp,
                    textAlign = TextAlign.End,
                    lineHeight = typography.small.lineSp,
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None
                    ),
                ),
            )

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

            // Toolbar
            Column(
                modifier = Modifier
                    .width(box.buttonHeight)
                    .align(Alignment.TopEnd),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                MiniSquareButton(
                    text = if (isBrushed) "✑" else "✎",
                    onClick = { isBrushed = !isBrushed },
                )
                MiniSquareButton(
                    text = "⟲",
                    onClick = {
                        boxMap.forEachIndexed { xIndex, boxColumn ->
                            boxColumn.forEachIndexed { yIndex, _ ->
                                boxMap[xIndex][yIndex] = 0f
                            }
                        }
                    },
                )
            }
        }
    }
}
