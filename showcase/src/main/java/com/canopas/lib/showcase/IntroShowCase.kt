package com.canopas.lib.showcase

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun IntroShowCase(
    showIntroShowCase: Boolean,
    onShowCaseCompleted: () -> Unit,
    modifier: Modifier = Modifier,
    state: IntroShowCaseState = rememberIntroShowCaseState(),
    dismissOnClickOutside: Boolean = false,
    content: @Composable IntroShowCaseScope.() -> Unit,
) {
    val scope = remember(state) {
        IntroShowCaseScope(state)
    }

    Box(modifier) {

        scope.content()

        if (showIntroShowCase) {
            IntroShowCaseTarget(
                state = state,
                dismissOnClickOutside = dismissOnClickOutside,
                onShowCaseCompleted = onShowCaseCompleted,
            )
        }
    }
}

class IntroShowCaseScope(
    private val state: IntroShowCaseState,
) {

    /**
     * Modifier that marks Compose UI element as a target for [IntroShowCaseTarget]
     */
    fun Modifier.introShowCaseTarget(
        index: Int,
        style: ShowcaseStyle = ShowcaseStyle.Default,
        content: @Composable BoxScope.() -> Unit,
    ): Modifier = introShowCaseTarget(
        state = state,
        index = index,
        style = style,
        content = content,
    )
}
