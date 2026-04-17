package io.github.jtaeyeon05.why_web.ui.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.times


class LayoutConstraints private constructor(
    val screen: ScreenSize,
    val box: BoxSize,
    val inset: InsetSize,
    val padding: PaddingSize,
    val typography: TypographySize,
) {
    data class ScreenSize(
        val base: Dp,
        val width: Dp,
        val height: Dp,
        val isVertical: Boolean,
        val isHorizontal: Boolean,
    )

    data class BoxSize(
        val smallBox: Dp,
        val mediumBox: Dp,
        val largeBox: Dp,
        val buttonHeight: Dp,
        val defaultSelectionLine: Float,
        val selectionBoxWidth: WidthRange,
        val selectionBoxHeight: (Float?) -> Dp,
        val defaultMessageLine: Float,
        val messageBoxWidth: Dp,
        val messageBoxHeight: (Float?) -> Dp,
    )

    data class InsetSize(
        val borderWidth: Dp,
        val thinBorderWidth: Dp,
    )

    data class PaddingSize(
        val small: Dp,
        val medium: Dp,
        val large: Dp,
    )

    data class TypographySize(
        val small: TextSize,
        val medium: TextSize,
        val large: TextSize,
        val lineHeight: Float,
    )

    companion object {
        fun from(
            width: Dp,
            height: Dp,
            density: Density,
        ): LayoutConstraints {
            val base = if (width <= height) width else min(width, height * 1.25f)

            val screen = ScreenSize(
                base = base,
                width = width,
                height = height,
                isVertical = width <= height,
                isHorizontal = width >= height,
            )
            val inset = InsetSize(
                borderWidth = base * 0.01f,
                thinBorderWidth = base * 0.001f,
            )
            val padding = PaddingSize(
                small = base * 0.01f,
                medium = base * 0.02f,
                large = base * 0.04f,
            )
            val typography = run {
                val lineHeight = 1.2f

                val small = TextSize(
                    size = base * 0.02f,
                    lineHeight = lineHeight,
                    density = density,
                )
                val medium = TextSize(
                    size = base * 0.04f,
                    lineHeight = lineHeight,
                    density = density,
                )
                val large = TextSize(
                    size = base * 0.08f,
                    lineHeight = lineHeight,
                    density = density,
                )

                TypographySize(
                    lineHeight = lineHeight,
                    small = small,
                    medium = medium,
                    large = large,
                )
            }

            val box = run {
                val buttonHeight = typography.medium.lineDp + padding.small * 2
                val defaultSelectionLine = 2f
                val defaultMessageLine = 4f

                BoxSize(
                    smallBox = base * 0.04f,
                    mediumBox = base * 0.08f,
                    largeBox = base * 0.24f,
                    buttonHeight = buttonHeight,
                    defaultSelectionLine = defaultSelectionLine,
                    selectionBoxWidth = WidthRange(
                        min = base * 0.33f,
                        max = base - padding.large * 2,
                    ),
                    selectionBoxHeight = { line ->
                        val line = if (line == null || line !in 1f .. 10f) defaultSelectionLine else line
                        buttonHeight * line + padding.medium * 2 + inset.borderWidth * 2
                    },
                    defaultMessageLine = defaultMessageLine,
                    messageBoxWidth = base,
                    messageBoxHeight = { line ->
                        val line = if (line == null || line !in 1f .. 10f) defaultMessageLine else line
                        typography.medium.lineDp * line + padding.medium * 2 + inset.borderWidth * 2
                    },
                )
            }

            return LayoutConstraints(
                screen = screen,
                box = box,
                inset = inset,
                padding = padding,
                typography = typography,
            )
        }
    }
}

data class WidthRange(
    val min: Dp,
    val max: Dp
) : ClosedRange<Dp> {
    override val start: Dp get() = min
    override val endInclusive: Dp get() = max
}

data class TextSize(
    val size: Dp,
    val lineHeight: Float,
    private val density: Density,
) {
    val dp: Dp get() = size
    val sp: TextUnit get() = with(density) { dp.toSp() }

    val lineDp: Dp get() = size * lineHeight
    val lineSp: TextUnit get() = with(density) { lineDp.toSp() }

    operator fun unaryPlus() = this
    operator fun unaryMinus() = this * -1f

    operator fun plus(other: TextSize) = TextSize(
        size = this.size + other.size,
        lineHeight = 0.5f * (this.lineHeight + other.lineHeight),
        density = this.density,
    )

    operator fun minus(other: TextSize) = TextSize(
        size = this.size - other.size,
        lineHeight = 0.5f * (this.lineHeight + other.lineHeight),
        density = this.density,
    )

    operator fun times(scale: Number) = TextSize(
        size = this.size * scale.toFloat(),
        lineHeight = this.lineHeight,
        density = this.density,
    )

    operator fun div(scale: Number) = TextSize(
        size = this.size / scale.toFloat(),
        lineHeight = this.lineHeight,
        density = this.density,
    )

    companion object {
        operator fun Number.times(textSize: TextSize) = TextSize(
            size = this.toFloat() * textSize.size,
            lineHeight = textSize.lineHeight,
            density = textSize.density,
        )
    }
}

@Composable
fun rememberLayoutConstraints(
    width: Dp,
    height: Dp,
): LayoutConstraints {
    val density = LocalDensity.current
    return remember(width, height, density) {
        LayoutConstraints.from(
            width = width,
            height = height,
            density = density
        )
    }
}

val LocalLayoutConstraints = staticCompositionLocalOf {
    LayoutConstraints.from(
        width = 800.dp,
        height = 1200.dp,
        density = Density(density = 3.0f)
    )
}
