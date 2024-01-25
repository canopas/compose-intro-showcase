package com.canopas.lib.showcase.data


/**
 * Sealed class representing different shapes for a reveal effect in [IntroShowcase].
 *
 * This sealed class provides three types of reveal shapes:
 * - [Circle]: Represents a circular reveal shape.
 * - [Square]: Represents a square reveal shape.
 * - [Rounded]: Represents a rounded rectangle reveal shape with a specified corner radius.
 *
 */
sealed class RevealShape {
    /**
     * Object representing a circular reveal shape.
     */
    data object Circle : RevealShape()

    /**
     * Object representing a square reveal shape.
     */
    data object Square : RevealShape()

    /**
     * Data class representing a rounded rectangle reveal shape with a corner radius.
     *
     * @property cornerRadius The corner radius for the rounded rectangle.
     */
    data class Rounded(val cornerRadius: Float? = null) : RevealShape()
}