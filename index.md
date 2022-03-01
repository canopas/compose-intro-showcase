# Intro Showcase View
[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-Intro--showcase--view-green.svg?style=flat )]( https://android-arsenal.com/details/1/8387 )
<img alt="Badge" height="20px" src="https://androidweekly.net/issues/issue-506/badge">

An android library to highlight different features of the app built using Jetpack Compose.

The library is inspired by the [TapTargetView](https://github.com/KeepSafe/TapTargetView) that is useful for legacy views.

## Configuration

Available on [Maven Central](https://search.maven.org/artifact/com.canopas.intro-showcase-view/introshowcaseview).

Add the dependency
```gradle
    implementation 'com.canopas.intro-showcase-view:introshowcaseview:1.0.3'

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

# Demo
[Sample](https://github.com/canopas/Intro-showcase-view/tree/master/app) app demonstrates how simple the usage of the library actually is.

# Further reading
For more details about the library implementation, checkout the [Article](https://blog.canopas.com/intro-showcase-view-in-jetpack-compose-ac044cd3bf28)

# Bugs and Feedback
For bugs, questions and discussions please use the [Github Issues](https://github.com/canopas/JetTapTarget/issues).

# Credits

Intro showcaseview is owned and maintained by the [Canopas team](https://canopas.com/). You can follow them on Twitter at [@canopassoftware](https://twitter.com/canopassoftware) for project updates and releases.
