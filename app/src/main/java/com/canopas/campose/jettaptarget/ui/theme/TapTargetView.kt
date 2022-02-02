package com.canopas.campose.jettaptarget.ui.theme

import android.util.Log
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

    currentTarget?.let { currentTarget ->
        val screenHeight = LocalConfiguration.current.screenHeightDp
        val targetCoords = currentTarget.coordinates
        val width = targetCoords.size.width
        val height = targetCoords.size.height
        val topArea = 88.dp
        val density = LocalDensity.current

        var textCoordinate: LayoutCoordinates? by remember {
            mutableStateOf(null)
        }
        var txtOffsetY by remember {
            mutableStateOf(0f)
        }


        val xOffset = with(LocalDensity.current) {
            targetCoords.positionInRoot().x.toDp()
        }
        val yOffset = with(LocalDensity.current) {
            targetCoords.positionInRoot().y.toDp()
        }

        val maxDimension = max(width.absoluteValue, height.absoluteValue)
        val minDimension = min(width.absoluteValue, height.absoluteValue)

        val targetRadius = maxDimension / 2f + 10f

        val animationSpec = infiniteRepeatable<Float>(
            animation = tween(2500, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Restart,
        )

        var parentXOffset by remember {
            mutableStateOf(0f)
        }
        var parentYOffset by remember {
            mutableStateOf(0f)
        }

        var outerRadius by remember {
            mutableStateOf(0f)
        }

        textCoordinate?.let { textCoords ->
            var outerCenterX: Float
            var outerCenterY: Float
            val textHeight = textCoords.size.height

            val onTop =
                targetCoords.boundsInParent().center.y - targetRadius - textHeight > 0

            val left = min(
                textCoords.boundsInParent().left,
                targetCoords.boundsInParent().left - targetRadius
            )
            val right = max(
                textCoords.boundsInParent().right,
                targetCoords.boundsInParent().right + targetRadius
            )

            val centerY =
                if (onTop) targetCoords.boundsInParent().center.y - targetRadius - textHeight
                else targetCoords.boundsInParent().center.y + targetRadius + textHeight

            outerCenterY = centerY
            outerCenterX = (left + right) / 2

            if (topArea > yOffset || yOffset > screenHeight.dp.minus(topArea)) {
                outerCenterX = (left + right) / 2
                outerCenterY = targetCoords.boundsInParent().center.y
            }

            parentXOffset = outerCenterX
            parentYOffset = outerCenterY

            outerRadius = getOuterRadius(
                textCoords.boundsInParent(),
                targetCoords.boundsInParent(),
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
                textCoordinate?.let {
                    val outerRadius =
                        getOuterRect(it.boundsInParent(), targetCoords.boundsInParent())
                    drawLine(
                        color = Color.Red,
                        start = outerRadius.topLeft,
                        end = outerRadius.bottomRight
                    )
                }

                drawCircle(
                    color = Purple700,
                    center = Offset(x = parentXOffset, y = parentYOffset),
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
                        radius = minDimension * dy * 4f,
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

            Column(modifier = Modifier
                .offset(y = with(LocalDensity.current) {
                    txtOffsetY.toDp()
                })
                .padding(16.dp)
                .onGloballyPositioned {
                    textCoordinate = it
                    val textHeight = it.size.height

                    val possibleTop =
                        targetCoords.boundsInParent().center.y - targetRadius - textHeight

                    txtOffsetY = if (possibleTop > 0) {
                        possibleTop
                    } else {
                        targetCoords.boundsInParent().center.y + targetRadius
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
    }
}

fun getOuterRect(textRect: Rect, targetRect: Rect): Rect {

    val topLeftX = min(textRect.topLeft.x, targetRect.topLeft.x)
    val topLeftY = min(textRect.topLeft.y, targetRect.topLeft.y)
    val bottomRightX = max(textRect.bottomRight.x, targetRect.bottomRight.x)
    val bottomRightY = max(textRect.bottomRight.y, targetRect.bottomRight.y)

    val expandedBounds = Rect(topLeftX, topLeftY, bottomRightX, bottomRightY)

    val d = sqrt(
        expandedBounds.height.toDouble().pow(2.0)
                + expandedBounds.width.toDouble().pow(2.0)
    ).toFloat()

    return expandedBounds
}

fun getOuterRadius(textRect: Rect, targetRect: Rect, inGutter: Boolean): Float {


    val topLeftX = min(textRect.topLeft.x, targetRect.topLeft.x)
    val topLeftY = min(textRect.topLeft.y, targetRect.topLeft.y)
    val bottomRightX = max(textRect.bottomRight.x, targetRect.bottomRight.x)
    val bottomRightY = max(textRect.bottomRight.y, targetRect.bottomRight.y)

    val expandedBounds = Rect(topLeftX, topLeftY, bottomRightX, bottomRightY)

    if (inGutter) {

    }

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