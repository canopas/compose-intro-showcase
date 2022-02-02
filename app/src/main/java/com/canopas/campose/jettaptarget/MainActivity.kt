package com.canopas.campose.jettaptarget

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canopas.campose.jettaptarget.ui.theme.JetTapTargetTheme
import com.canopas.campose.jettaptarget.ui.theme.Pink

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
    var isIntroCompleted by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    Box {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { },
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    navigationIcon = {
                        IconButton(onClick = {},
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                targets.add(
                                    TapTargetProperty(
                                        "back", 4,
                                        coordinates,
                                        "Go back!!",
                                        "You can go back by clicking here."
                                    )
                                )
                                Log.e("", "Target back ${targets.size}")

                            }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Search")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                targets.add(
                                    TapTargetProperty(
                                        "search", 3,
                                        coordinates,
                                        "Search anything!!",
                                        "You can search anything by clicking here."
                                    )
                                )
                                Log.e("", "Target search ${targets.size}")

                            },
                        ) {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        Toast.makeText(
                            context,
                            "Clicked",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        targets.add(
                            TapTargetProperty(
                                "email", 1,
                                coordinates,
                                "Check emails",
                                "Click here to check/send emails"
                            )
                        )
                        Log.e("", "Target email ${targets.size}")

                    },
                    backgroundColor = Pink,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(6.dp)
                ) {
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = "Email"
                    )
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxHeight(0.3f)) {

                    Column(
                        Modifier
                            .align(Alignment.BottomStart)
                            .background(Pink)
                            .fillMaxWidth()
                            .height(90.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Intro Showcase view", fontWeight = FontWeight.Bold,
                            fontSize = 20.sp, color = Color.White
                        )
                        Text(
                            text = "This is an example of Intro Showcase view",
                            fontSize = 16.sp, color = Color.White
                        )

                    }

                    Image(
                        painter = painterResource(id = R.drawable.ic_unknown_profile),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .clip(CircleShape)
                            .onGloballyPositioned { coordinates ->
                                targets.add(
                                    TapTargetProperty(
                                        "profile",
                                        0,
                                        coordinates,
                                        "User profile",
                                        "Click here to update your profile"
                                    )
                                )
                                Log.e("", "Target profile ${targets.size}")
                            }
                    )
                }

                Button(
                    onClick = {},
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 16.dp)
                        .onGloballyPositioned { coordinates ->
                            targets.add(
                                TapTargetProperty(
                                    "follow", 2,
                                    coordinates,
                                    "Follow me",
                                    "Click here to follow"
                                )
                            )
                            Log.e("", "Target profile ${targets.size}")
                        }

                ) {
                    Text(text = "Follow")
                }

            }
        }
        if (!isIntroCompleted)
            TapTarget(targets) {
                targets.clear()
                isIntroCompleted = true
                Toast.makeText(
                    context,
                    "App Intro finished!!",
                    Toast.LENGTH_SHORT
                ).show()
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