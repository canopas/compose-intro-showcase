<img src="https://github.com/canopas/JetTapTarget/blob/master/gif/4-%20intro%20showcase.jpg" />

# Intro Showcase View
An android library to highlight different features of the app built using Jetpack Compose.

The library is inspired by the [TapTargetView](https://github.com/KeepSafe/TapTargetView) that is useful for legacy views.


<img src="https://github.com/canopas/Intro-showcase-view/blob/master/gif/intro1.gif" height="540" />

## Configuration
Add it in your root build.gradle at the end of repositories:
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
  ```
  
Add the dependency
```gradle
  implementation 'com.github.canopas:Intro-showcase-view:1.0.2'
```

## How to use ?
```kotlin
@Composable
fun ShowcaseSample() {
    val targets = remember {
        mutableStateMapOf<String, ShowcaseProperty>()
    }
    Box {
        FloatingActionButton(
            onClick = {},
            modifier = Modifier.padding(16.dp).align(Alignment.BottomStart).onGloballyPositioned { coordinates ->
                targets["email"] = ShowcaseProperty(
                    1, coordinates, "Check emails", "Click here to check/send emails"
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

        IntroShowCase(targets) {
            // Showcase finished!!
        }
    }
}
   
```
<img src="https://github.com/canopas/Intro-showcase-view/blob/master/gif/intro2.gif" height="480" />

# Further reading
For more details, checkout the [tutorial](https://blog.canopas.com/intro-showcase-view-in-jetpack-compose-ac044cd3bf28)

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
