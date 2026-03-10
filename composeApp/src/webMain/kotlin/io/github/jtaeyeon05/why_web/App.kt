package io.github.jtaeyeon05.why_web

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.em
import kotlinx.coroutines.delay


@Composable
fun App() {
    AppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
                val layoutConstraints = rememberLayoutConstraints(maxWidth, maxHeight)

                var nowMessage by rememberSaveable { mutableStateOf("") }
                val targetMessage = "태어나시겠습니까?"

                LaunchedEffect(Unit) {
                    delay(1_000)
                    while (nowMessage.length < targetMessage.length) {
                        nowMessage += targetMessage[nowMessage.length]
                        delay(100)
                    }
                }

                layoutConstraints.run {
                    // Message
                    Column(
                        modifier = Modifier
                            .width(box.messageBoxWidth)
                            .height(box.messageBoxHeight)
                            .align(Alignment.BottomCenter)
                            .border(
                                width = inset.borderWidth,
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = RectangleShape
                            )
                            .padding(inset.borderWidth + padding.medium)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(typography.mediumLineHeight.dp),
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("[ ")
                                }
                                append("👩‍🚀")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(" ?? ]")
                                }
                            },
                            fontSize = typography.mediumFontSize.sp,
                            lineHeight = typography.lineHeight.em,
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(typography.mediumLineHeight.dp * 3),
                            text = nowMessage,
                            fontSize = typography.mediumFontSize.sp,
                            lineHeight = typography.lineHeight.em,
                        )
                    }

                    // Selection
                    val infiniteTransition = rememberInfiniteTransition(label = "Selection Color Transition")
                    val selectionColor by infiniteTransition.animateColor(
                        initialValue = MaterialTheme.colorScheme.primaryContainer,
                        targetValue = MaterialTheme.colorScheme.background,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 1000,
                                easing = LinearEasing
                            ),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "Selection Color"
                    )
                    var selection by rememberSaveable { mutableStateOf(0) }
                    Column(
                        modifier = Modifier
                            .padding(bottom = box.messageBoxHeight + padding.medium)
                            .width(IntrinsicSize.Max)
                            .sizeIn(
                                minWidth = box.selectionBoxMinWidth,
                                maxWidth = box.selectionBoxMaxWidth,
                                minHeight = box.selectionBoxHeight,
                                maxHeight = box.selectionBoxHeight,
                            )
                            .align(Alignment.BottomCenter)
                            .border(
                                width = inset.borderWidth,
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = RectangleShape
                            )
                            .padding(inset.borderWidth + padding.medium)
                            .verticalScroll(rememberScrollState())
                    ) {
                        val interactionSource0 = remember { MutableInteractionSource() }
                        LaunchedEffect(interactionSource0) {
                            interactionSource0.interactions.collect { interaction ->
                                if (interaction is HoverInteraction.Enter) {
                                    selection = 0
                                }
                            }
                        }
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(box.buttonHeight)
                                .background(if (selection == 0) selectionColor else MaterialTheme.colorScheme.background)
                                .clickable(interactionSource = interactionSource0) {
                                    openTabInNewTab("https://xodus.lol/") // TODO
                                }
                                .padding(padding.small),
                            text = "예",
                            fontSize = typography.mediumFontSize.sp,
                            lineHeight = typography.lineHeight.em,
                        )

                        val interactionSource1 = remember { MutableInteractionSource() }
                        LaunchedEffect(interactionSource1) {
                            interactionSource1.interactions.collect { interaction ->
                                if (interaction is HoverInteraction.Enter) {
                                    selection = 1
                                }
                            }
                        }
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(box.buttonHeight)
                                .background(if (selection == 1) selectionColor else MaterialTheme.colorScheme.background)
                                .clickable(interactionSource = interactionSource1) {
                                    openTabInNewTab("https://xodus.lol/") // TODO
                                }
                                .padding(padding.small),
                            text = "아니요",
                            fontSize = typography.mediumFontSize.sp,
                            lineHeight = typography.lineHeight.em,
                        )

                        val interactionSource2 = remember { MutableInteractionSource() }
                        LaunchedEffect(interactionSource2) {
                            interactionSource2.interactions.collect { interaction ->
                                if (interaction is HoverInteraction.Enter) {
                                    selection = 2
                                }
                            }
                        }
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(box.buttonHeight)
                                .background(if (selection == 2) selectionColor else MaterialTheme.colorScheme.background)
                                .clickable(interactionSource = interactionSource2) {
                                    openTabInNewTab("https://xodus.lol/") // TODO
                                }
                                .padding(padding.small),
                            text = "!!!",
                            fontSize = typography.mediumFontSize.sp,
                            lineHeight = typography.lineHeight.em,
                        )
                    }
                }
            }
        }
    }
}
