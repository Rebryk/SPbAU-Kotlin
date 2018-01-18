package ru.spbau.mit.droidlike.game.painter

interface Drawable {
    fun draw(painter: Painter, params: DrawingParameters? = null): Unit
}