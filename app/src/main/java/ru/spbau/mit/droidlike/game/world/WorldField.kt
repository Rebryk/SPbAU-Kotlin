package ru.spbau.mit.droidlike.game.world

import ru.spbau.mit.droidlike.game.painter.Drawable

interface WorldField : Drawable {
    val height: Int
    val width: Int

    fun isWall(x: Int, y: Int): Boolean
}