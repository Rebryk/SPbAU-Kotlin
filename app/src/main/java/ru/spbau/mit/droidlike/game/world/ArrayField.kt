package ru.spbau.mit.droidlike.game.world

import ru.spbau.mit.droidlike.game.painter.DrawingParameters
import ru.spbau.mit.droidlike.game.painter.Painter

/**
 * Class to store field
 */
class ArrayField(override val height: Int,
                 override val width: Int,
                 val field: List<List<Boolean>>) : WorldField {
    override fun isWall(x: Int, y: Int): Boolean {
        return field.getOrNull(x) == null || field[x].getOrNull(y) == null || field[x][y]
    }

    override fun draw(painter: Painter, params: DrawingParameters?) = painter.draw(this, params)
}