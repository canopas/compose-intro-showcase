package com.canopas.campose.jettaptarget

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canopas.campose.jettaptarget.ui.theme.JetTapTargetTheme
import com.canopas.campose.jettaptarget.ui.theme.ThemeColor

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
                    showcaseSample()
                }
            }
        }
    }
}

@Composable
fun showcaseSample() {
    val context = LocalContext.current

    val targets = remember {
        mutableStateListOf<ShowcaseProperty>()
    }
    var isIntroCompleted by remember {
        mutableStateOf(false)
    }

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
                                    ShowcaseProperty(
                                        "back", 4, coordinates,
                                        "Go back!!", "You can go back by clicking here."
                                    )
                                )
                            }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Search")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                targets.add(
                                    ShowcaseProperty(
                                        "search",
                                        3,
                                        coordinates,
                                        "Search anything!!",
                                        "You can search anything by clicking here."
                                    )
                                )
                            },
                        ) {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {},
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        targets.add(
                            ShowcaseProperty(
                                "email", 1, coordinates,
                                "Check emails", "Click here to check/send emails"
                            )
                        )
                    },
                    backgroundColor = ThemeColor,
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
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(90.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Intro Showcase view", fontWeight = FontWeight.Bold,
                            fontSize = 24.sp, color = ThemeColor
                        )
                        Text(
                            text = "This is an example of Intro Showcase view",
                            fontSize = 20.sp, color = Color.Black, textAlign = TextAlign.Center
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
                                    ShowcaseProperty(
                                        "profile", 0, coordinates,
                                        "User profile", "Click here to update your profile"
                                    )
                                )
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
                                ShowcaseProperty(
                                    "follow", 2, coordinates,
                                    "Follow me", "Click here to follow"
                                )
                            )
                        }

                ) {
                    Text(text = "Follow")
                }

            }
        }
        if (!isIntroCompleted)
            IntroShowCase(targets) {
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
    }
}