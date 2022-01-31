package com.canopas.campose.jettaptarget.ui.theme

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import kotlin.math.absoluteValue
import kotlin.math.max

@Composable
fun TapTarget(targets: List<TapTargetProperty> = emptyList()) {

    val currentTargetIndex by remember {
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

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Purple700,
                    center = Offset(width / 2f, height / 2f),
                    alpha = 0.8f
                )
                drawCircle(
                    color = Color.White,
                    radius = maxDimension.toFloat(),
                    center = Offset(width / 2f, height / 2f),
                    alpha = 0.5f
                )
                drawCircle(
                    color = Color.White,
                    radius = maxDimension.plus(60) * 0.5f,
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