package com.canopas.lib.showcase

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.canopas.lib.showcase.component.IntroShowcaseManager
import com.canopas.lib.showcase.component.IntroShowcaseState
import com.canopas.lib.showcase.component.ShowcasePopup
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.canopas.lib.showcase.component.introShowcaseTarget
import com.canopas.lib.showcase.component.rememberIntroShowcaseState

@Composable
fun IntroShowcase(
    showIntroShowCase: Boolean,
    onShowCaseCompleted: () -> Unit,
    state: IntroShowcaseState = rememberIntroShowcaseState(),
    dismissOnClickOutside: Boolean = false,
    content: @Composable IntroShowcaseScope.() -> Unit,
) {
    val scope = remember(state) {
        IntroShowcaseScope(state)
    }

    DisposableEffect(Unit) {
        IntroShowcaseManager.registerRestoreHandler {
            state.currentTargetIndex = 0
        }
        onDispose {
            IntroShowcaseManager.registerRestoreHandler(null)
        }
    }

    scope.content()

    if (showIntroShowCase) {
        ShowcasePopup(
            state = state,
            dismissOnClickOutside = dismissOnClickOutside,
            onShowCaseCompleted = onShowCaseCompleted,
        )
    }
}


class IntroShowcaseScope(
    private val state: IntroShowcaseState,
) {

    /**
     * Modifier that marks Compose UI element as a target for [IntroShowcase]
     */
    fun Modifier.introShowCaseTarget(
        index: Int,
        style: ShowcaseStyle = ShowcaseStyle.Default,
        content: @Composable BoxScope.() -> Unit,
    ): Modifier = introShowcaseTarget(
        state = state,
        index = index,
        style = style,
        content = content,
    )
}
