package io.github.jtaeyeon05.why_web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit


data class LayoutConstraints(
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
        val selectionBoxMinWidth: Dp,
        val selectionBoxMaxWidth: Dp,
        val selectionBoxHeight: Dp,
        val messageBoxWidth: Dp,
        val messageBoxHeight: Dp,
    )

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
    }
}

@Composable
fun rememberLayoutConstraints(
    width: Dp,
    height: Dp,
): LayoutConstraints {
    val density = LocalDensity.current
    return remember(width, height, density) {
        val base = if (width <= height) width else height * 1.25f

        val screen = LayoutConstraints.ScreenSize(
            base = base,
            width = width,
            height = height,
            isVertical = width <= height,
            isHorizontal = width >= height,
        )
        val inset = LayoutConstraints.InsetSize(
            borderWidth = base * 0.01f,
            smallRound = base * 0.01f,
            mediumRound = base * 0.02f,
            largeRound = base * 0.04f,
        )
        val padding = LayoutConstraints.PaddingSize(
            small = base * 0.01f,
            medium = base * 0.02f,
            large = base * 0.04f,
        )
        val typography = run {
            val lineHeight = 1.2f

            val small = LayoutConstraints.fontSize(base * 0.03f, density)
            val medium = LayoutConstraints.fontSize(base * 0.04f, density)
            val large = LayoutConstraints.fontSize(base * 0.06f, density)

            LayoutConstraints.TypographySize(
                lineHeight = lineHeight,
                smallFontSize = small,
                smallLineHeight = small * lineHeight,
                mediumFontSize = medium,
                mediumLineHeight = medium * lineHeight,
                largeFontSize = large,
                largeLineHeight = large * lineHeight,
            )
        }

        val box = run {
            val buttonHeight = typography.mediumLineHeight.dp + padding.small * 2

            LayoutConstraints.BoxSize(
                buttonHeight = buttonHeight,
                selectionBoxMinWidth = base * 0.33f,
                selectionBoxMaxWidth = base - padding.large * 2,
                selectionBoxHeight = buttonHeight * 2 + padding.medium * 2 + inset.borderWidth * 2,
                messageBoxWidth = base,
                messageBoxHeight = typography.mediumLineHeight.dp * 4 + padding.medium * 2 + inset.borderWidth * 2,
            )
        }

        LayoutConstraints(
            screen = screen,
            box = box,
            inset = inset,
            padding = padding,
            typography = typography,
        )
    }
}
