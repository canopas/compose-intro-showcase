package com.canopas.lib.showcase.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.canopas.lib.showcase.data.RevealShape
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun ShowcasePopup(
    state: IntroShowcaseState,
    dismissOnClickOutside: Boolean,
    onShowCaseCompleted: () -> Unit,
) {
    state.currentTarget?.let {
        ShowcaseWindow {
            ShowcaseContent(
                target = it,
                dismissOnClickOutside = dismissOnClickOutside
            ) {
                state.currentTargetIndex++
                if (state.currentTarget == null) {
                    onShowCaseCompleted()
                }
            }
        }
    }
}

@Composable
internal fun ShowcaseContent(
    target: IntroShowcaseTargets,
    dismissOnClickOutside: Boolean,
    onShowcaseCompleted: () -> Unit
) {

    val targetCords = target.coordinates
    val targetRect = targetCords.boundsInWindow()

    var dismissShowcaseRequest by remember(target) { mutableStateOf(false) }

    val maxDimension =
        max(targetCords.size.width.absoluteValue, targetCords.size.height.absoluteValue)
    val targetRadius = maxDimension / 2f + 40f

    val animationSpec = infiniteRepeatable<Float>(
        animation = tween(2000, easing = FastOutLinearInEasing),
        repeatMode = RepeatMode.Restart,
    )

    var outerOffset by remember(target) {
        mutableStateOf(Offset(0f, 0f))
    }

    var outerRadius by remember(target) {
        mutableFloatStateOf(0f)
    }

    val outerAnimatable = remember { Animatable(0.6f) }
    val outerAlphaAnimatable = remember(target) { Animatable(0f) }

    val animatables = remember(target) {
        listOf(
            Animatable(0f),
            Animatable(0f)
        )
    }

    LaunchedEffect(target) {
        outerAnimatable.snapTo(0.6f)

        outerAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing,
            ),
        )
    }

    LaunchedEffect(target) {
        outerAlphaAnimatable.animateTo(
            targetValue = target.style.backgroundAlpha,
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing,
            ),
        )
    }

    LaunchedEffect(dismissShowcaseRequest) {
        if (dismissShowcaseRequest) {
            launch {
                outerAlphaAnimatable.animateTo(
                    0f,
                    animationSpec = tween(
                        durationMillis = 200
                    )
                )
            }
            launch {
                outerAnimatable.animateTo(
                    targetValue = 0.6f,
                    animationSpec = tween(
                        durationMillis = 350,
                        easing = FastOutSlowInEasing,
                    )
                )
            }
            delay(350)
            onShowcaseCompleted()
        }
    }

    animatables.forEachIndexed { index, animatable ->
        LaunchedEffect(animatable) {
            delay(index * 1000L)
            animatable.animateTo(
                targetValue = 1f, animationSpec = animationSpec
            )
        }
    }

    val dys = animatables.map { it.value }

    Box(
        modifier = Modifier
            .alpha(outerAlphaAnimatable.value)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(target) {
                    detectTapGestures { tapOffset ->
                        if (targetRect.contains(tapOffset)) {
                            dismissShowcaseRequest = true
                        }
                    }
                }
                .let {
                    if (dismissOnClickOutside) {
                        it.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { dismissShowcaseRequest = true }
                    } else it
                }
                .graphicsLayer(alpha = 0.99f)
        ) {
            when (target.style.revealShape) {
                is RevealShape.Circle -> {
                    drawCircle(
                        color = target.style.backgroundColor,
                        center = outerOffset,
                        radius = outerRadius * outerAnimatable.value,
                        alpha = target.style.backgroundAlpha
                    )

                    dys.forEach { dy ->
                        drawCircle(
                            color = target.style.targetCircleColor,
                            radius = maxDimension * dy * 2f,
                            center = targetRect.center,
                            alpha = 1 - dy
                        )
                    }

                    drawCircle(
                        color = target.style.targetCircleColor,
                        radius = targetRadius,
                        center = targetRect.center,
                        blendMode = BlendMode.Xor
                    )
                }

                is RevealShape.Square -> {
                    drawRect(
                        color = target.style.backgroundColor,
                        size = Size(outerRadius * 2, outerRadius * 2),
                        topLeft = outerOffset - Offset(
                            outerRadius * outerAnimatable.value,
                            outerRadius * outerAnimatable.value
                        ),
                        alpha = target.style.backgroundAlpha
                    )

                    dys.forEach { dy ->
                        drawRect(
                            color = target.style.targetCircleColor,
                            size = Size(maxDimension * dy * 4f, maxDimension * dy * 4f),
                            topLeft = targetRect.center - Offset(
                                maxDimension * dy * 2,
                                maxDimension * dy * 2
                            ),
                            alpha = 1 - dy
                        )
                    }

                    drawRect(
                        color = target.style.targetCircleColor,
                        size = Size(targetRadius * 2, targetRadius * 2),
                        topLeft = targetRect.center - Offset(targetRadius, targetRadius),
                        blendMode = BlendMode.Xor
                    )
                }

                is RevealShape.Rounded -> {
                    val cornerRadius = target.style.revealShape.cornerRadius ?: 20f
                    drawRoundRect(
                        color = target.style.backgroundColor,
                        size = Size(outerRadius * 2, outerRadius * 2),
                        topLeft = outerOffset - Offset(
                            outerRadius * outerAnimatable.value,
                            outerRadius * outerAnimatable.value
                        ),
                        cornerRadius = CornerRadius(cornerRadius),
                        alpha = target.style.backgroundAlpha
                    )

                    dys.forEach { dy ->
                        drawRoundRect(
                            color = target.style.targetCircleColor,
                            size = Size(maxDimension * dy * 4f, maxDimension * dy * 4f),
                            topLeft = targetRect.center - Offset(
                                maxDimension * dy * 2,
                                maxDimension * dy * 2
                            ),
                            cornerRadius = CornerRadius(cornerRadius),
                            alpha = 1 - dy
                        )
                    }

                    drawRoundRect(
                        color = target.style.targetCircleColor,
                        size = Size(targetRadius * 2, targetRadius * 2),
                        cornerRadius = CornerRadius(cornerRadius),
                        topLeft = targetRect.center - Offset(targetRadius, targetRadius),
                        blendMode = BlendMode.Xor
                    )
                }
            }
        }

        ShowCaseText(target, targetRect, targetRadius) { textCoords ->
            val contentRect = textCoords.boundsInWindow()
            val outerRect = getOuterRect(contentRect, targetRect)
            outerOffset = outerRect.center
            outerRadius = getOuterRadius(outerRect) + targetRadius
        }
    }
}


@Composable
private fun ShowCaseText(
    currentTarget: IntroShowcaseTargets,
    boundsInParent: Rect,
    targetRadius: Float,
    updateContentCoordinates: (LayoutCoordinates) -> Unit
) {

    var contentOffsetY by remember(currentTarget) { mutableFloatStateOf(0f) }

    Box(
        content = currentTarget.content,
        modifier = Modifier
            .offset(y = with(LocalDensity.current) {
                contentOffsetY.toDp()
            })
            .onGloballyPositioned {
                updateContentCoordinates(it)
                val contentHeight = it.size.height

                val possibleTop =
                    boundsInParent.center.y - targetRadius - contentHeight

                contentOffsetY = if (possibleTop > 0) {
                    possibleTop
                } else {
                    boundsInParent.center.y + targetRadius
                }
            }
            .padding(16.dp)
    )

}

private fun getOuterRect(contentRect: Rect, targetRect: Rect): Rect {

    val topLeftX = min(contentRect.topLeft.x, targetRect.topLeft.x)
    val topLeftY = min(contentRect.topLeft.y, targetRect.topLeft.y)
    val bottomRightX = max(contentRect.bottomRight.x, targetRect.bottomRight.x)
    val bottomRightY = max(contentRect.bottomRight.y, targetRect.bottomRight.y)

    return Rect(topLeftX, topLeftY, bottomRightX, bottomRightY)
}

private fun getOuterRadius(outerRect: Rect): Float {
    val d = sqrt(
        outerRect.height.toDouble().pow(2.0)
                + outerRect.width.toDouble().pow(2.0)
    ).toFloat()

    return (d / 2f)
}

data class IntroShowcaseTargets(
    val index: Int,
    val coordinates: LayoutCoordinates,
    val style: ShowcaseStyle = ShowcaseStyle.Default,
    val content: @Composable BoxScope.() -> Unit
)

class ShowcaseStyle(
    val backgroundColor: Color = Color.Black,
    /*@FloatRange(from = 0.0, to = 1.0)*/
    val backgroundAlpha: Float = DEFAULT_BACKGROUND_RADIUS,
    val targetCircleColor: Color = Color.White,
    val revealShape: RevealShape = RevealShape.Circle
) {

    fun copy(
        backgroundColor: Color = this.backgroundColor,
        /*@FloatRange(from = 0.0, to = 1.0)*/
        backgroundAlpha: Float = this.backgroundAlpha,
        targetCircleColor: Color = this.targetCircleColor,
        revealShape: RevealShape = this.revealShape
    ): ShowcaseStyle {

        return ShowcaseStyle(
            backgroundColor = backgroundColor,
            backgroundAlpha = backgroundAlpha,
            targetCircleColor = targetCircleColor,
            revealShape = revealShape
        )
    }

    companion object {
        private const val DEFAULT_BACKGROUND_RADIUS = 0.9f

        /**
         * Constant for default text style.
         */
        @Stable
        val Default = ShowcaseStyle()
    }
}