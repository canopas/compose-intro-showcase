package com.canopas.lib.showcase

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun IntroShowCaseScaffold(
    showIntroShowCase: Boolean,
    onShowCaseCompleted: () -> Unit,
    modifier: Modifier = Modifier,
    state: IntroShowCaseState = rememberIntroShowCaseState(),
    content: @Composable IntroShowCaseScope.() -> Unit,
) {
    val scope = remember(state) {
        IntroShowCaseScope(state)
    }

    Box(modifier) {

        scope.content()

        if (showIntroShowCase) {
            IntroShowCase(
                state = state,
                onShowCaseCompleted = onShowCaseCompleted,
            )
        }
    }
}

class IntroShowCaseScope(
    private val state: IntroShowCaseState,
) {

    /**
     * Modifier that marks Compose UI element as a target for [IntroShowCase]
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
