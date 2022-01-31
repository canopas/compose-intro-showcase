package com.canopas.campose.jettaptarget.ui.theme

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import com.canopas.campose.jettaptarget.R
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

    Log.e("Recompose ", "${currentTarget} size ${targets.size}")

    currentTarget?.let {
        val coordinates = it.coordinates
        val width = coordinates.size.width
        val height = coordinates.size.height
        val xOffset = with(LocalDensity.current) {
            coordinates.positionInRoot().x.toDp()
        }
        val yOffset = with(LocalDensity.current) {
            coordinates.positionInRoot().y.toDp()
        }

        Box(
            modifier = Modifier
                .offset(xOffset, yOffset)
        ) {

            val maxDimension = max(width.absoluteValue, height.absoluteValue)
            val minDimension = min(width.absoluteValue, height.absoluteValue)

            val animationSpec = infiniteRepeatable<Float>(
                animation = tween(2500, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Restart,
            )
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

            Canvas(modifier = Modifier.fillMaxSize().clickable {
                currentTargetIndex++
            }) {
                drawCircle(
                    color = Purple700,
                    center = Offset(width / 2f, height / 2f),
                    alpha = 0.8f
                )

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
                    color = Color.Transparent,
                    radius = minDimension * 1.5f,
                    center = Offset(width / 2f, height / 2f)
                )

            }

        }
    }


}


data class TapTargetProperty(
    val coordinates: LayoutCoordinates,
    val title: String, val subTitle: String
)