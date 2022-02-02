package com.canopas.campose.jettaptarget.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
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
fun TapTarget(targets: List<TapTargetProperty> = emptyList()) {

    var currentTargetIndex by remember {
        mutableStateOf(0)
    }
    val currentTarget = if (targets.isNotEmpty()) targets[currentTargetIndex] else null

    currentTarget?.let { target ->
        val screenHeight = LocalConfiguration.current.screenHeightDp
        val targetCords = target.coordinates
        val width = targetCords.size.width
        val height = targetCords.size.height
        val topArea = 88.dp
        val density = LocalDensity.current

        var textCoordinate: LayoutCoordinates? by remember {
            mutableStateOf(null)
        }

        val xOffset = with(density) {
            targetCords.positionInRoot().x.toDp()
        }
        val yOffset = with(density) {
            targetCords.positionInRoot().y.toDp()
        }

        val maxDimension = max(width.absoluteValue, height.absoluteValue)
        val targetRadius = maxDimension / 2f + 10f

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
            val textHeight = textCoords.size.height
            val isInGutter = topArea > yOffset || yOffset > screenHeight.dp.minus(topArea)

            val outerCircleOffset = getOuterCircleCenter(
                targetCords.boundsInParent(),
                textCoords.boundsInParent(), targetRadius, textHeight, isInGutter
            )

            outerXOffset = outerCircleOffset.x
            outerYOffset = outerCircleOffset.y

            outerRadius = getOuterRadius(
                textCoords.boundsInParent(),
                targetCords.boundsInParent(),
                topArea > yOffset
            ) + targetRadius
        }

        Box(
            modifier = Modifier
        ) {

            val animatables = listOf(
                remember { Animatable(0f) },
                remember { Animatable(0f) }
            )

            animatables.forEachIndexed { index, animatable ->
                LaunchedEffect(animatable) {
                    delay(index * 1000L)
                    animatable.animateTo(
                        targetValue = 1f, animationSpec = animationSpec
                    )
                }
            }

            val dys = animatables.map { it.value }
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                drawCircle(
                    color = Purple700,
                    center = Offset(x = outerXOffset, y = outerYOffset),
                    radius = outerRadius,
                    alpha = 0.8f
                )
            }

            Canvas(modifier = Modifier
                .fillMaxSize()
                .offset(xOffset, yOffset)
                .clickable {
                    currentTargetIndex++
                }) {

                dys.forEach { dy ->
                    drawCircle(
                        color = Color.White,
                        radius = maxDimension * dy,
                        center = Offset(width / 2f, height / 2f),
                        alpha = 1 - dy
                    )
                }

                drawCircle(
                    color = Color.White,
                    radius = targetRadius,
                    center = Offset(width / 2f, height / 2f)
                )
            }

            ShowCaseText(target, targetCords.boundsInParent(), targetRadius) {
                textCoordinate = it
            }
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

    val textSpacing = 40f

    Column(modifier = Modifier
        .offset(y = with(LocalDensity.current) {
            txtOffsetY.toDp()
        })
        .padding(16.dp)
        .onGloballyPositioned {
            onGloballyPositioned(it)
            val textHeight = it.size.height

            val possibleTop =
                boundsInParent.center.y - targetRadius - textHeight - textSpacing

            txtOffsetY = if (possibleTop > 0) {
                possibleTop
            } else {
                boundsInParent.center.y + targetRadius + textSpacing
            }
        })
    {
        Text(
            text = currentTarget.title,
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(text = currentTarget.subTitle, fontSize = 16.sp, color = Color.White)
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

fun getOuterRadius(textRect: Rect, targetRect: Rect, inGutter: Boolean): Float {

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
    val coordinates: LayoutCoordinates,
    val title: String, val subTitle: String
)