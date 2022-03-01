# Intro Showcase View
[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-Intro--showcase--view-green.svg?style=flat )]( https://android-arsenal.com/details/1/8387 )
<img alt="Badge" height="20px" src="https://androidweekly.net/issues/issue-506/badge">


<img src="https://github.com/canopas/Intro-showcase-view/blob/master/docs/assets/4-%20intro%20showcase.jpg" />


An android library to highlight different features of the app built using Jetpack Compose.

The library is inspired by the [TapTargetView](https://github.com/KeepSafe/TapTargetView) that is useful for legacy views.


<img src="https://github.com/canopas/Intro-showcase-view/blob/master/docs/assets/intro1.gif" height="540" />

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
    var showAppIntro by remember {
        mutableStateOf(true)
    }

    Box {
        Image(
            painter = painterResource(id = R.drawable.ic_unknown_profile),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .clip(CircleShape)
                .onGloballyPositioned { coordinates ->
                    targets["profile"] = ShowcaseProperty(
                        0, // specify index to show feature in order
                        coordinates, // specify coordinates of target
                        "User profile", // specify text to show as title
                        "Click here to update your profile", // specify text to show as description
                        // ShowcaseStyle is optional
                        style = ShowcaseStyle.Default.copy(
                            titleStyle = TextStyle(
                                color = Color.Black,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            ), // specify the style for title
                            descriptionStyle = TextStyle(
                                color = Color.Black,
                                fontSize = 16.sp
                            ), // specify style for description
                            backgroundColor = Color(0xFFFFCC80), // specify color of background
                            backgroundAlpha = 0.98f, // specify transparency of background
                            targetCircleColor = Color.White // specify color of target circle
                        )
                    )
                }
        )

        if (showAppIntro) {
            IntroShowCase(targets) {
                //App Intro finished!!
                showAppIntro = false
            }
        }
    }
}

```
<img src="https://github.com/canopas/Intro-showcase-view/blob/master/docs/assets/intro2.gif" height="480" />

# Demo
[Sample](https://github.com/canopas/Intro-showcase-view/tree/master/app) app demonstrates how simple the usage of the library actually is.

# Interested in library implementation?
Checkout the [Article](https://blog.canopas.com/intro-showcase-view-in-jetpack-compose-ac044cd3bf28)

# Bugs and Feedback
For bugs, questions and discussions please use the [Github Issues](https://github.com/canopas/JetTapTarget/issues).

# Credits

Intro showcaseview is owned and maintained by the [Canopas team](https://canopas.com/). You can follow them on Twitter at [@canopassoftware](https://twitter.com/canopassoftware) for project updates and releases.
