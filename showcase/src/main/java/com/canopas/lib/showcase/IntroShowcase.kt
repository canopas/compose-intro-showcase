package com.canopas.lib.showcase

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.canopas.lib.showcase.component.IntroShowcaseState
import com.canopas.lib.showcase.component.ShowcasePopup
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.canopas.lib.showcase.component.introShowcaseTarget
import com.canopas.lib.showcase.component.rememberIntroShowcaseState
import com.canopas.lib.showcase.data.RevealShape

@Composable
fun IntroShowcase(
    showIntroShowCase: Boolean,
    onShowCaseCompleted: () -> Unit,
    state: IntroShowcaseState = rememberIntroShowcaseState(),
    dismissOnClickOutside: Boolean = false,
    revealShape: RevealShape = RevealShape.Circle,
    content: @Composable IntroShowcaseScope.() -> Unit,
) {
    val scope = remember(state) {
        IntroShowcaseScope(state)
    }

    scope.content()

    if (showIntroShowCase) {
        ShowcasePopup(
            state = state,
            revealShape = revealShape,
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
