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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

@Composable
fun TapTarget(targets: List<TapTargetProperty> = emptyList()) {

    var currentTargetIndex by remember {
        mutableStateOf(0)
    }
    val currentTarget = if (targets.isNotEmpty()) targets[currentTargetIndex] else null

    currentTarget?.let { currentTarget ->
        val targetCoords = currentTarget.coordinates
        val width = targetCoords.size.width
        val height = targetCoords.size.height


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

        parentXOffset = targetCoords.positionInParent().x
        parentYOffset = targetCoords.positionInParent().y
        Log.e(
                        "parentXOffset",
                        "parentXOffset: $parentXOffset parentYOffset $parentYOffset txtOffsetX $txtOffsetY"
                    )
        Box(
            modifier = Modifier.onGloballyPositioned {

            }
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
                    center = Offset(parentXOffset, parentYOffset),
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
                    val minRadius = min(size.minDimension / 3f, minDimension * dy * 4f)
                    drawCircle(
                        color = Color.White,
                        radius = minRadius,
                        center = Offset(width / 2f, height / 2f),
                        alpha = 1 - dy
                    )
                }

                drawCircle(
                    color = Color.White,
                    radius = maxDimension / 2f + 10f,
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
                        targetCoords.boundsInParent().center.y - (maxDimension / 2f + 10f) - textHeight

                    txtOffsetY = if (possibleTop > 0) {
                        possibleTop
                    } else {
                        targetCoords.boundsInParent().center.y + (maxDimension / 2f + 10f)
                    }


//                    Log.e(
//                        "possibleTop",
//                        "Top: $possibleTop boundary $targetTop txtOffsetX $txtOffsetY"
//                    )
                })
            {
                Text(text = currentTarget.title, fontSize = 24.sp)
                Text(text = currentTarget.subTitle, fontSize = 16.sp)
            }

        }
    }


}


data class TapTargetProperty(
    val coordinates: LayoutCoordinates,
    val title: String, val subTitle: String
)