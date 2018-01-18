package ru.spbau.mit.droidlike.game.painter

/**
 * Class to store drawing parameters
 * For example, drawing position
 */
data class DrawingParameters(val x: Int, val y: Int) {
    constructor(data: Pair<Int, Int>) : this(data.first, data.second)
}