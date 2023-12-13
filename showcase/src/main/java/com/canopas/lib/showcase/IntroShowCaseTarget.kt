package com.canopas.lib.showcase

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun IntroShowCaseTarget(
    state: IntroShowCaseState,
    dismissOnClickOutside: Boolean,
    onShowCaseCompleted: () -> Unit,
) {
    state.currentTarget?.let {
        TargetContent(target = it, dismissOnClickOutside = dismissOnClickOutside) {
            state.currentTargetIndex++
            if (state.currentTarget == null) {
                onShowCaseCompleted()
            }
        }
    }
}

@Composable
private fun TargetContent(
    target: IntroShowcaseTargets,
    dismissOnClickOutside: Boolean,
    onShowcaseCompleted: () -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val targetCords = target.coordinates
    val gutterArea = 88.dp
    val targetRect = targetCords.boundsInRoot()

    val yOffset = with(LocalDensity.current) {
        targetCords.positionInRoot().y.toDp()
    }

    var dismissShowcaseRequest by remember(target) { mutableStateOf(false) }

    val isTargetInGutter = gutterArea > yOffset || yOffset > screenHeight.dp.minus(gutterArea)

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
        mutableStateOf(0f)
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
    Box(modifier = Modifier.alpha(outerAlphaAnimatable.value)) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(target) {
                    detectTapGestures { tapOffeset ->
                        if (targetRect.contains(tapOffeset)) {
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

        ShowCaseText(target, targetRect, targetRadius) { textCoords ->
            val contentRect = textCoords.boundsInParent()
            val outerRect = getOuterRect(contentRect, targetRect)
            val possibleOffset = getOuterCircleCenter(targetRect, contentRect, targetRadius)

            outerOffset = if (isTargetInGutter) {
                outerRect.center
            } else {
                possibleOffset
            }

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

    var contentOffsetY by remember(currentTarget) {
        mutableStateOf(0f)
    }

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

private fun getOuterCircleCenter(
    targetRect: Rect,
    contentRect: Rect,
    targetRadius: Float
): Offset {
    val outerCenterX: Float
    val outerCenterY: Float

    val contentHeight = contentRect.height
    val onTop =
        targetRect.center.y - targetRadius - contentHeight > 0

    val left = min(
        contentRect.left,
        targetRect.left - targetRadius
    )
    val right = max(
        contentRect.right,
        targetRect.right + targetRadius
    )

    val centerY =
        if (onTop) targetRect.center.y - targetRadius - contentHeight
        else targetRect.center.y + targetRadius + contentHeight

    outerCenterY = centerY
    outerCenterX = (left + right) / 2

    return Offset(outerCenterX, outerCenterY)
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
    val targetCircleColor: Color = Color.White
) {

    fun copy(
        backgroundColor: Color = this.backgroundColor,
        /*@FloatRange(from = 0.0, to = 1.0)*/
        backgroundAlpha: Float = this.backgroundAlpha,
        targetCircleColor: Color = this.targetCircleColor
    ): ShowcaseStyle {

        return ShowcaseStyle(
            backgroundColor = backgroundColor,
            backgroundAlpha = backgroundAlpha,
            targetCircleColor = targetCircleColor
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