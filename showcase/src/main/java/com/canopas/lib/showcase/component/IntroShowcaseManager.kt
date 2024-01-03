package com.canopas.lib.showcase.component

import com.canopas.lib.showcase.handler.IntroRestartHandler

/**
 * Manager class for IntroShowcase. Manages the registration and invocation of
 * [IntroRestartHandler] callbacks.
 */
object IntroShowcaseManager {
    /**
     * A nullable [IntroRestartHandler] that holds a callback function to be invoked when restarting
     * IntroShowcase. It allows external components to register custom logic to execute when
     * restarting the IntroShowcase, providing flexibility for handling restart events.
     */
    var introRestartHandler: IntroRestartHandler? = null

    /**
     * Register a [IntroRestartHandler] callback to be invoked when restarting the IntroShowcase.
     */
    @JvmStatic
    internal fun registerRestoreHandler(introRestartHandler: IntroRestartHandler?) {
        this.introRestartHandler = introRestartHandler
    }
}