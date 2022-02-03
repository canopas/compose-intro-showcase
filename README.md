<img src="https://github.com/canopas/JetTapTarget/blob/master/gif/4-%20intro%20showcase.jpg" />

# JetTapTarget
An android library to highlights different features of the app built using Jetpack Compose.

<img src="https://github.com/canopas/JetTapTarget/blob/master/gif/Peek%202022-02-02%2019-38.gif" height="540" />

## How to add in your project
Add it in your root build.gradle at the end of repositories:
```
repositories {
    maven { url 'https://jitpack.io' }
}
  ```
  
Add the dependency
```
 implementation 'com.github.canopas:JetTapTarget:1.0.1'
```

## How to use ?
```
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
        FloatingActionButton(
            onClick = {
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .onGloballyPositioned { coordinates ->
                    targets.add(
                        ShowcaseProperty(
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
```
<img src="https://github.com/canopas/JetTapTarget/blob/master/gif/Peek%202022-02-03%2010-40.gif" height="540" />

# Further reading
For more details, checkout the [tutorial]()

# Bugs and Feedback
For bugs, questions and discussions please use the [Github Issues](https://github.com/canopas/JetTapTarget/issues).

# Credits

[Canopas Software LLP](https://canopas.com/)

# Licence

```
Copyright 2022 Radika Saliya

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
