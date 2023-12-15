package com.canopas.lib.showcase.component

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned

/**
 * Creates a [IntroShowcaseState] that is remembered across compositions.
 *
 * Changes to the provided values for [initialIndex] will **not** result in the state being
 * recreated or changed in any way if it has already
 * been created.
 *
 * @param initialIndex the initial value for [IntroShowcaseState.currentTargetIndex]
 */
@Composable
fun rememberIntroShowcaseState(
    initialIndex: Int = 0,
): IntroShowcaseState {
    return remember {
        IntroShowcaseState(
            initialIndex = initialIndex,
        )
    }
}

/**
 * Modifier that marks Compose UI element as a target for [IntroShowCaseTarget]
 */
internal fun Modifier.introShowcaseTarget(
    state: IntroShowcaseState,
    index: Int,
    style: ShowcaseStyle = ShowcaseStyle.Default,
    content: @Composable BoxScope.() -> Unit,
): Modifier = onGloballyPositioned { coordinates ->
    state.targets[index] = IntroShowcaseTargets(
        index = index,
        coordinates = coordinates,
        style = style,
        content = content
    )
}

class IntroShowcaseState internal constructor(
    initialIndex: Int,
) {

    internal var targets = mutableStateMapOf<Int, IntroShowcaseTargets>()

    var currentTargetIndex by mutableStateOf(initialIndex)
        internal set

    val currentTarget: IntroShowcaseTargets?
        get() = targets[currentTargetIndex]
}
