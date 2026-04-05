package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import kotlin.math.ceil


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoxScope.EasterEgg1Screen(
    navController: NavController,
) {
    LocalLayoutConstraints.current.run {
        val width = ceil(screen.width / box.smallBox).toInt()
        val height = ceil(screen.height / box.smallBox).toInt()

        val boxMap = remember(width, height) {
            SnapshotStateList(width / 2 * 2 + 1) {
                SnapshotStateList(height / 2 * 2 + 1) {
                    false
                }
            }
        }
        val boundsMap = remember(width, height) {
            Array(width / 2 * 2 + 1) {
                Array(height / 2 * 2 + 1) {
                    Rect.Zero
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(width, height) {
                    var lastOverIndex: Pair<Int, Int>? = null
                    detectDragGestures(
                        onDrag = { change, _ ->
                            val touchPoint = change.position
                            boundsMap.forEachIndexed { xIndex, boundsColumn ->
                                boundsColumn.forEachIndexed { yIndex, bounds ->
                                    if (bounds.contains(touchPoint)) {
                                        if (lastOverIndex != xIndex to yIndex) {
                                            boxMap[xIndex][yIndex] = !boxMap[xIndex][yIndex]
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
            for (x in -width / 2..width / 2) {
                for (y in -height / 2..height / 2) {
                    val xIndex = x + width / 2
                    val yIndex = y + height / 2
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(
                                x = x * box.smallBox,
                                y = y * box.smallBox,
                            )
                            .size(box.smallBox)
                            .onGloballyPositioned { layoutCoordinates ->
                                boundsMap[xIndex][yIndex] = layoutCoordinates.boundsInParent()
                            }
                            .background(
                                color = if (boxMap[xIndex][yIndex]) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f).compositeOver(MaterialTheme.colorScheme.background) else MaterialTheme.colorScheme.background,
                            )
                            .border(
                                width = inset.thinBorderWidth,
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = RectangleShape
                            )
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        boxMap[xIndex][yIndex] = !boxMap[xIndex][yIndex]
                                    },
                                )
                            }
                    )
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
                Box(
                    modifier = Modifier
                        .size(box.buttonHeight)
                        .align(Alignment.TopStart)
                        .background(MaterialTheme.colorScheme.background)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    navController.popBackStack()
                                },
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "X",
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
    }
}
