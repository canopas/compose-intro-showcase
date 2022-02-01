package com.canopas.campose.jettaptarget

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canopas.campose.jettaptarget.ui.theme.JetTapTargetTheme
import com.canopas.campose.jettaptarget.ui.theme.TapTarget
import com.canopas.campose.jettaptarget.ui.theme.TapTargetProperty

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetTapTargetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TapTargetButton()
                }
            }
        }
    }
}

@Composable
fun TapTargetButton() {
    val targets = remember {
        mutableStateListOf<TapTargetProperty>()
    }
    Box(modifier = Modifier.fillMaxSize()) {
        TapTarget(targets)

        Button(onClick = {
        }, shape = CircleShape, modifier = Modifier
            .padding(50.dp)
            .align(Alignment.Center)
            .onGloballyPositioned { coordinates ->
                targets.add(
                    TapTargetProperty(
                        coordinates,
                        "Hello world",
                        "This is the sample of TapTarget in Jetpack compose"
                    )
                )
            }) {
            Text(text = "Button 2")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetTapTargetTheme {
        TapTargetButton()
    }
}