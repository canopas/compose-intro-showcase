package com.canopas.campose.jettaptarget

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun TapTarget(
    targets: MutableList<TapTargetProperty> = mutableListOf(),
    backgroundColor: Color = Color.Black,
    onShowcaseCompleted: () -> Unit
) {
    val uniqueTargets = targets.distinctBy { it.tag }.sortedBy { it.index }
    var currentTargetIndex by remember { mutableStateOf(0) }

    val currentTarget =
        if (uniqueTargets.isNotEmpty() && currentTargetIndex < uniqueTargets.size) uniqueTargets[currentTargetIndex] else null


    currentTarget?.let {
        TargetContent(it, backgroundColor) {
            if (++currentTargetIndex >= uniqueTargets.size) {
                onShowcaseCompleted()
            }
        }
    }
}

@Composable
fun TargetContent(
    target: TapTargetProperty,
    backgroundColor: Color,
    onShowcaseCompleted: () -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val targetCords = target.coordinates
    val topArea = 88.dp
    val targetRect = targetCords.boundsInRoot()
    var textCoordinate: LayoutCoordinates? by remember {
        mutableStateOf(null)
    }

    val yOffset = with(LocalDensity.current) {
        targetCords.positionInRoot().y.toDp()
    }

    val maxDimension =
        max(targetCords.size.width.absoluteValue, targetCords.size.height.absoluteValue)
    val targetRadius = maxDimension / 2f + 40f

    val animationSpec = infiniteRepeatable<Float>(
        animation = tween(2000, easing = FastOutLinearInEasing),
        repeatMode = RepeatMode.Restart,
    )

    var outerXOffset by remember {
        mutableStateOf(0f)
    }
    var outerYOffset by remember {
        mutableStateOf(0f)
    }

    var outerRadius by remember {
        mutableStateOf(0f)
    }

    textCoordinate?.let { textCoords ->
        val textRect = textCoords.boundsInRoot()

        val textHeight = textCoords.size.height
        val isInGutter = topArea > yOffset || yOffset > screenHeight.dp.minus(topArea)

        val outerCircleOffset = getOuterCircleCenter(
            targetRect, textRect, targetRadius, textHeight, isInGutter
        )

        outerXOffset = outerCircleOffset.x
        outerYOffset = outerCircleOffset.y

        outerRadius = getOuterRadius(textRect, targetRect) + targetRadius
    }

    val outerAnimatable = remember { Animatable(0.6f) }

    val animatables = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) }
    )

    LaunchedEffect(target.tag) {
        outerAnimatable.snapTo(0.6f)

        outerAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing,
            ),
        )
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
    Box {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(target) {
                    detectTapGestures { tapOffeset ->
                        if (targetRect.contains(tapOffeset)) {
                            onShowcaseCompleted()
                        }
                    }
                }
                .graphicsLayer(alpha = 0.99f)
        ) {

            drawCircle(
                color = backgroundColor,
                center = Offset(x = outerXOffset, y = outerYOffset),
                radius = outerRadius * outerAnimatable.value,
                alpha = 0.9f
            )

            dys.forEach { dy ->
                drawCircle(
                    color = Color.White,
                    radius = maxDimension * dy * 2f,
                    center = targetRect.center,
                    alpha = 1 - dy
                )
            }

            drawCircle(
                color = Color.Transparent,
                radius = targetRadius,
                center = targetRect.center,
                blendMode = BlendMode.Clear
            )

        }

        ShowCaseText(target, targetRect, targetRadius) {
            textCoordinate = it
        }
    }
}


@Composable
fun ShowCaseText(
    currentTarget: TapTargetProperty,
    boundsInParent: Rect,
    targetRadius: Float,
    onGloballyPositioned: (LayoutCoordinates) -> Unit
) {

    var txtOffsetY by remember {
        mutableStateOf(0f)
    }

    Column(modifier = Modifier
        .offset(y = with(LocalDensity.current) {
            txtOffsetY.toDp()
        })
        .onGloballyPositioned {
            onGloballyPositioned(it)
            val textHeight = it.size.height

            val possibleTop =
                boundsInParent.center.y - targetRadius - textHeight

            txtOffsetY = if (possibleTop > 0) {
                possibleTop
            } else {
                boundsInParent.center.y + targetRadius
            }
        }
        .padding(16.dp)
    )
    {
        Text(
            text = currentTarget.title,
            fontSize = 24.sp,
            color = currentTarget.subTitleColor,
            fontWeight = FontWeight.Bold
        )
        Text(text = currentTarget.subTitle, fontSize = 16.sp, color = currentTarget.subTitleColor)
    }

}

fun getOuterCircleCenter(
    targetBound: Rect,
    textBound: Rect,
    targetRadius: Float,
    textHeight: Int,
    isInGutter: Boolean,
): Offset {
    var outerCenterX: Float
    var outerCenterY: Float

    val onTop =
        targetBound.center.y - targetRadius - textHeight > 0

    val left = min(
        textBound.left,
        targetBound.left - targetRadius
    )
    val right = max(
        textBound.right,
        targetBound.right + targetRadius
    )

    val centerY =
        if (onTop) targetBound.center.y - targetRadius - textHeight
        else targetBound.center.y + targetRadius + textHeight

    outerCenterY = centerY
    outerCenterX = (left + right) / 2

    if (isInGutter) {
        outerCenterX = (left + right) / 2
        outerCenterY = targetBound.center.y
    }

    return Offset(outerCenterX, outerCenterY)
}

fun getOuterRadius(textRect: Rect, targetRect: Rect): Float {

    val topLeftX = min(textRect.topLeft.x, targetRect.topLeft.x)
    val topLeftY = min(textRect.topLeft.y, targetRect.topLeft.y)
    val bottomRightX = max(textRect.bottomRight.x, targetRect.bottomRight.x)
    val bottomRightY = max(textRect.bottomRight.y, targetRect.bottomRight.y)

    val expandedBounds = Rect(topLeftX, topLeftY, bottomRightX, bottomRightY)

    val d = sqrt(
        expandedBounds.height.toDouble().pow(2.0)
                + expandedBounds.width.toDouble().pow(2.0)
    ).toFloat()

    return (d / 2f)
}

data class TapTargetProperty(
    val tag: String,
    val index: Int,
    val coordinates: LayoutCoordinates,
    val title: String, val subTitle: String,
    val titleColor: Color = Color.White, val subTitleColor: Color = Color.White,
)