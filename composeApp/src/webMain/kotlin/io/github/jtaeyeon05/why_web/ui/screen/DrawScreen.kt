package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import io.github.jtaeyeon05.why_web.data.Avatar
import io.github.jtaeyeon05.why_web.navigation.Screen
import io.github.jtaeyeon05.why_web.ui.foundation.LocalKeyboardEventManager
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints
import io.github.jtaeyeon05.why_web.ui.foundation.WebKey
import io.github.jtaeyeon05.why_web.ui.foundation.rememberAnimatedText
import io.github.jtaeyeon05.why_web.ui.widget.ClassicButton
import io.github.jtaeyeon05.why_web.ui.widget.MessageBox
import io.github.jtaeyeon05.why_web.ui.widget.MiniSquareButton
import io.github.jtaeyeon05.why_web.ui.widget.SelectionBox
import io.github.jtaeyeon05.why_web.viewmodel.AppViewModel


data class Pixel(
    val x: Int,
    val y: Int,
    val type: PixelType
) {
    enum class PixelType {
        DOT, STAR, EYE, FACE, FIRE
    }
}

class Drawing(
    val drawingSize: Int = 25,
    initialPixelList: List<Pixel> = listOf(),
) {
    private val _pixelList = mutableStateListOf<Pixel>().apply { addAll(initialPixelList) }
    val pixelList: List<Pixel> get() = _pixelList

    fun draw(
        x: Int,
        y: Int,
        type: Pixel.PixelType = Pixel.PixelType.DOT
    ) {
        _pixelList.add(
            Pixel(
                x = x.coerceIn(0 ..< drawingSize),
                y = y.coerceIn(0 ..< drawingSize),
                type = type,
            )
        )
    }

    fun erase(x: Int, y: Int) {
        _pixelList.removeAll {
            it.x == x && it.y == y
        }
    }

    fun clear() {
        _pixelList.clear()
    }

    fun toValue(): String {
        return "D$drawingSize" + pixelList.joinToString { pixel ->
            ",X${pixel.x}-Y${pixel.y}-T${pixel.type.name}"
        }
    }

    companion object {
        fun fromValue(value: String): Drawing {
            var drawingSize = 25
            val pixelList = mutableListOf<Pixel>()

            try {
                value.split(",")[0].let { settingStr ->
                    val params = settingStr.split("-").associate {
                        val key = it.take(1)
                        val valStr = it.drop(1)
                        key to valStr
                    }

                    if (params["D"]?.toIntOrNull() != null)
                        drawingSize = params["D"]?.toIntOrNull()!!
                }
            } catch (_: Exception) {  }
            value.split(",").drop(1).forEach { pixelStr ->
                try {
                    val params = pixelStr.split("-").associate {
                        val key = it.take(1)
                        val valStr = it.drop(1)
                        key to valStr
                    }

                    pixelList.add(
                        Pixel(
                            x = params["X"]!!.toInt(),
                            y = params["Y"]!!.toInt(),
                            type = Pixel.PixelType.valueOf(params["T"]!!)
                        )
                    )
                } catch (_: Exception) {  }
            }

            return Drawing(
                drawingSize = drawingSize,
                initialPixelList = pixelList
            )
        }
    }
}

class Painter(
    val drawingSize: Int = 25,
    initialX: Int? = null,
    initialY: Int? = null,
    initialIsPencilMode: Boolean? = null,
    initialIsEraserMode: Boolean? = null,
) {
    var x by mutableStateOf(initialX ?: 12)
        private set
    var y by mutableStateOf(initialY ?: 12)
        private set
    var isPencilMode by mutableStateOf(initialIsPencilMode ?: false)
        private set
    var isEraserMode by mutableStateOf(initialIsEraserMode ?: false)
        private set

    init {
        if (isPencilMode && isEraserMode) {
            isPencilMode = false
            isEraserMode = false
        }
    }

    fun moveTo(x: Int, y: Int) {
        this.x = x.coerceIn(0 ..< drawingSize)
        this.y = y.coerceIn(0 ..< drawingSize)
    }

    fun moveBy(dx: Int = 0, dy: Int = 0) {
        moveTo(x + dx, y + dy)
    }

    fun moveUp(amount: Int = 1) = moveBy(dy= -amount)
    fun moveDown(amount: Int = 1) = moveBy(dy = amount)
    fun moveLeft(amount: Int = 1) = moveBy(dx = -amount)
    fun moveRight(amount: Int = 1) = moveBy(dx = amount)

    fun togglePencilMode(value: Boolean = !isPencilMode) {
        if (value) isEraserMode = false
        isPencilMode = value
    }

    fun toggleEraserMode(value: Boolean = !isEraserMode) {
        if (value) isPencilMode = false
        isEraserMode = value
    }

    fun toValue(): String {
        return "D$drawingSize-X${x}-Y${y}-P${isPencilMode}-E${isEraserMode}"
    }

    companion object {
        fun fromValue(value: String): Painter {
            return try {
                val params = value.split("-").associate {
                    val key = it.take(1)
                    val valStr = it.drop(1)
                    key to valStr
                }

                Painter(
                    drawingSize = params["D"]?.toIntOrNull() ?: 25,
                    initialX = params["X"]?.toIntOrNull(),
                    initialY = params["Y"]?.toIntOrNull(),
                    initialIsPencilMode = params["P"]?.toBooleanStrictOrNull(),
                    initialIsEraserMode = params["E"]?.toBooleanStrictOrNull()
                )
            } catch (_: Exception) {
                Painter()
            }
        }
    }
}

@Composable
fun BoxScope.DrawScreen(
    navController: NavController,
    viewModel: AppViewModel,
    screen: Screen.Draw
) {
    LocalLayoutConstraints.current.run {
        val keyboardManager = LocalKeyboardEventManager.current

        val screen by rememberSaveable { mutableStateOf(screen) }
        val drawing by rememberSaveable { mutableStateOf(Drawing()) }
        val painter by rememberSaveable { mutableStateOf(Painter()) }

        // TODO: Hash
        /*fun refresh(newScreen: Screen.Draw) {
            screen = newScreen
            refreshHash(
                model = viewModel.model.value,
                screen = screen
            )
            navController.currentBackStackEntry?.savedStateHandle?.set("test", screen.test)
        }*/

        // Drawing Box
        LaunchedEffect(
            painter.x, painter.y,
            painter.isPencilMode, painter.isEraserMode
        ) {
            if (painter.isPencilMode) {
                drawing.draw(
                    x = painter.x,
                    y = painter.y,
                    type = Pixel.PixelType.DOT,
                )
            }
            if (painter.isEraserMode) {
                drawing.erase(
                    x = painter.x,
                    y = painter.y,
                )
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .padding(bottom = box.messageBoxHeight(2))
                .fillMaxSize(),
            contentAlignment = Alignment.TopStart,
        ) {
            // TODO: 터치, 위치 조정

            val blockWidth = maxWidth / 25
            val blockHeight = maxHeight / 25

            // Drawing
            drawing.pixelList.forEach { pixel ->
                if (pixel.type == Pixel.PixelType.DOT) {
                    Box(
                        modifier = Modifier
                            .size(box.mediumBox)
                            .offset(
                                x = pixel.x * blockWidth,
                                y = pixel.y * blockHeight,
                            )
                            .background(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                shape = RectangleShape
                            )
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .size(box.mediumBox)
                            .offset(
                                x = pixel.x * blockWidth,
                                y = pixel.y * blockHeight,
                            ),
                        text = when (pixel.type) {
                            Pixel.PixelType.STAR -> "⭐️"
                            Pixel.PixelType.EYE -> "👀"
                            Pixel.PixelType.FACE -> "😀"
                            Pixel.PixelType.FIRE -> "🔥"
                        },
                        style = LocalTextStyle.current.copy(
                            fontSize = typography.large.sp,
                            lineHeight = typography.large.sp,
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
            }

            // Pointer
            Box(
                modifier = Modifier
                    .padding(0.5f * (box.mediumBox - box.smallBox))
                    .size(box.smallBox)
                    .offset(
                        x = painter.x * blockWidth,
                        y = painter.y * blockHeight,
                    )
                    .background(
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = if (painter.isPencilMode) {
                            RectangleShape
                        } else if (painter.isEraserMode) {
                            AbsoluteCutCornerShape(percent = 100)
                        } else {
                            CircleShape
                        }
                    )
            )
        }

        // Message
        var texts by rememberSaveable { mutableStateOf(arrayOf("낙서를 하겠어", "", "...")) }
        var textsKey by rememberSaveable { mutableStateOf(0) }
        val message = rememberAnimatedText(
            texts = texts,
            key = textsKey,
        )

        MessageBox(
            modifier = Modifier.align(Alignment.BottomCenter),
            message = message,
            avatar = Avatar.user(
                avatar = viewModel.model.value.avatar,
                name = viewModel.model.value.name,
            ),
            line = 2,
            onReplay = { textsKey += 1 },
        )

        // Selection
        LaunchedEffect(Unit) {
            keyboardManager.events.collect { webKeyEvent ->
                // Move Event
                if (webKeyEvent.isUpPressed) {
                    painter.moveUp()
                } else if (webKeyEvent.isDownPressed) {
                    painter.moveDown()
                } else if (webKeyEvent.isLeftPressed) {
                    painter.moveLeft()
                } else if (webKeyEvent.isRightPressed) {
                    painter.moveRight()
                }

                // Drawing Point Event
                else if (webKeyEvent.isPressed(WebKey.QUOTE)) {
                    drawing.draw(
                        x = painter.x,
                        y = painter.y,
                        type = Pixel.PixelType.DOT,
                    )
                } else if (webKeyEvent.isPressed(WebKey.ENTER)) {
                    painter.togglePencilMode()
                }

                // Erasing Event
                else if (webKeyEvent.isPressed(WebKey.SLASH)) {
                    drawing.erase(
                        x = painter.x,
                        y = painter.y,
                    )
                } else if (webKeyEvent.isPressed(WebKey.SHIFT_RIGHT)) {
                    painter.toggleEraserMode()
                }

                // Emoji Event
                else if (webKeyEvent.isPressed(WebKey.DIGIT_1)) {
                    drawing.draw(
                        x = painter.x,
                        y = painter.y,
                        type = Pixel.PixelType.STAR,
                    )
                } else if (webKeyEvent.isPressed(WebKey.DIGIT_2)) {
                    drawing.draw(
                        x = painter.x,
                        y = painter.y,
                        type = Pixel.PixelType.EYE,
                    )
                } else if (webKeyEvent.isPressed(WebKey.DIGIT_3)) {
                    drawing.draw(
                        x = painter.x,
                        y = painter.y,
                        type = Pixel.PixelType.FACE,
                    )
                } else if (webKeyEvent.isPressed(WebKey.DIGIT_4)) {
                    drawing.draw(
                        x = painter.x,
                        y = painter.y,
                        type = Pixel.PixelType.FIRE,
                    )
                }
            }
        }

        SelectionBox(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = box.messageBoxHeight(2) + padding.medium)
                .pointerInput(Unit) {},
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding.small),
                text = "←↑→↓ 혹은 터치로 포인터를 조절하고\n" +
                        "다음 버튼을 눌러 그림을 그릴 수 있습니다.",
                fontSize = typography.small.sp,
                textAlign = TextAlign.Start,
                lineHeight = typography.small.lineSp,
            )
            ClassicButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    drawing.draw(
                        x = painter.x,
                        y = painter.y,
                        type = Pixel.PixelType.DOT
                    )
                },
            ) {
                Text("점 찍기 (')")
            }
            ClassicButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    drawing.erase(
                        x = painter.x,
                        y = painter.y,
                    )
                },
            ) {
                Text("지우기 (/)")
            }
            ClassicButton(
                modifier = Modifier.fillMaxWidth(),
                focused = painter.isPencilMode,
                onClick = { painter.togglePencilMode() },
            ) {
                Text("연속 점 찍기 (Enter)")
            }
            ClassicButton(
                modifier = Modifier.fillMaxWidth(),
                focused = painter.isEraserMode,
                onClick = { painter.toggleEraserMode() },
            ) {
                Text("연속 지우기 (R.Shift)")
            }
            ClassicButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    drawing.draw(
                        x = painter.x,
                        y = painter.y,
                        type = Pixel.PixelType.STAR
                    )
                },
            ) {
                Text("⭐️ 찍기 (1)")
            }
            ClassicButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    drawing.draw(
                        x = painter.x,
                        y = painter.y,
                        type = Pixel.PixelType.EYE
                    )
                },
            ) {
                Text("👀 찍기 (2)")
            }
            ClassicButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    drawing.draw(
                        x = painter.x,
                        y = painter.y,
                        type = Pixel.PixelType.FACE
                    )
                },
            ) {
                Text("😀 찍기 (3)")
            }
            ClassicButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    drawing.draw(
                        x = painter.x,
                        y = painter.y,
                        type = Pixel.PixelType.FIRE
                    )
                },
            ) {
                Text("🔥 찍기 (4)")
            }
        }

        // Test Text
        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(MaterialTheme.colorScheme.background)
                .padding(padding.small),
            text = "X: ${painter.x}, Y: ${painter.y}, Pencil: ${painter.isPencilMode}, Eraser: ${painter.isEraserMode}",
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
    }
}
