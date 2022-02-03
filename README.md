<img src="https://github.com/canopas/JetTapTarget/blob/master/gif/4-%20intro%20showcase.jpg" />

# Intro Showcase View
An android library to highlights different features of the app built using Jetpack Compose.

The library is inspired by the [TapTargetView](https://github.com/KeepSafe/TapTargetView) that is useful for legacy views.


<img src="https://github.com/canopas/Intro-showcase-view/blob/master/gif/introshowcase2.gif" height="540" />

## Configuration
Add it in your root build.gradle at the end of repositories:
```
repositories {
    maven { url 'https://jitpack.io' }
}
  ```
  
Add the dependency
```
  implementation 'com.github.canopas:IntroShowcase-View:X.X.X'
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
                            "email", 1, coordinates, "Check emails", "Click here to check/send emails" )
                    )     
                },
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
            }
    }
}   
```
<img src="https://github.com/canopas/Intro-showcase-view/blob/master/gif/intro-ex-1.gif" height="480" />

# Further reading
For more details, checkout the [tutorial]()

# Bugs and Feedback
For bugs, questions and discussions please use the [Github Issues](https://github.com/canopas/JetTapTarget/issues).

# Credits

[Canopas Software LLP](https://canopas.com/)

# Licence

```
Copyright 2022 Canopas Software LLP

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
