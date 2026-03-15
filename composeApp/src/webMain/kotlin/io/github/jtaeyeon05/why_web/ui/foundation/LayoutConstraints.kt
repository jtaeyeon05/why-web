package io.github.jtaeyeon05.why_web.ui.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp


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
        val buttonHeight: Dp,
        val defaultSelectionLine: Int,
        val selectionBoxWidth: WidthRange,
        val selectionBoxHeight: (Int?) -> Dp,
        val defaultMessageLine: Int,
        val messageBoxWidth: Dp,
        val messageBoxHeight: (Int?) -> Dp,
    ) {
        data class WidthRange(
            val min: Dp,
            val max: Dp
        ) : ClosedRange<Dp> {
            override val start: Dp get() = min
            override val endInclusive: Dp get() = max
        }
    }

    data class InsetSize(
        val borderWidth: Dp,
        val smallRound: Dp,
        val mediumRound: Dp,
        val largeRound: Dp,
    )

    data class PaddingSize(
        val small: Dp,
        val medium: Dp,
        val large: Dp,
    )

    data class TypographySize(
        val lineHeight: Float,
        val smallFontSize: FontSize,
        val smallLineHeight: FontSize,
        val mediumFontSize: FontSize,
        val mediumLineHeight: FontSize,
        val largeFontSize: FontSize,
        val largeLineHeight: FontSize,
        val superLargeFontSize: FontSize,
        val superLargeLineHeight: FontSize,
    ) {
        data class FontSize(
            val sp: TextUnit,
            val dp: Dp,
        ) {
            operator fun times(other: Float): FontSize {
                return FontSize(
                    sp = sp * other,
                    dp = dp * other,
                )
            }
        }
    }

    companion object {
        fun fontSize(dp: Dp, density: Density): TypographySize.FontSize {
            return with (density) {
                TypographySize.FontSize(
                    sp = dp.toSp(),
                    dp = dp
                )
            }
        }

        fun from(
            width: Dp,
            height: Dp,
            density: Density,
        ): LayoutConstraints {
            val base = if (width <= height) width else height * 1.25f

            val screen = ScreenSize(
                base = base,
                width = width,
                height = height,
                isVertical = width <= height,
                isHorizontal = width >= height,
            )
            val inset = InsetSize(
                borderWidth = base * 0.01f,
                smallRound = base * 0.01f,
                mediumRound = base * 0.02f,
                largeRound = base * 0.04f,
            )
            val padding = PaddingSize(
                small = base * 0.01f,
                medium = base * 0.02f,
                large = base * 0.04f,
            )
            val typography = run {
                val lineHeight = 1.2f

                val small = LayoutConstraints.fontSize(base * 0.02f, density)
                val medium = LayoutConstraints.fontSize(base * 0.04f, density)
                val large = LayoutConstraints.fontSize(base * 0.08f, density)
                val superLarge = LayoutConstraints.fontSize(base * 0.32f, density)

                TypographySize(
                    lineHeight = lineHeight,
                    smallFontSize = small,
                    smallLineHeight = small * lineHeight,
                    mediumFontSize = medium,
                    mediumLineHeight = medium * lineHeight,
                    largeFontSize = large,
                    largeLineHeight = large * lineHeight,
                    superLargeFontSize = superLarge,
                    superLargeLineHeight = superLarge * lineHeight,
                )
            }

            val box = run {
                val buttonHeight = typography.mediumLineHeight.dp + padding.small * 2
                val defaultSelectionLine = 2
                val defaultMessageLine = 4

                BoxSize(
                    buttonHeight = buttonHeight,
                    defaultSelectionLine = defaultSelectionLine,
                    selectionBoxWidth = BoxSize.WidthRange(
                        min = base * 0.33f,
                        max = base - padding.large * 2,
                    ),
                    selectionBoxHeight = { line ->
                        val line = if (line == null || line !in 1 .. 10) defaultSelectionLine else line
                        buttonHeight * line + padding.medium * 2 + inset.borderWidth * 2
                    },
                    defaultMessageLine = defaultMessageLine,
                    messageBoxWidth = base,
                    messageBoxHeight = { line ->
                        val line = if (line == null || line !in 0 .. 10) defaultMessageLine else line
                        typography.mediumLineHeight.dp * line + padding.medium * 2 + inset.borderWidth * 2
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
