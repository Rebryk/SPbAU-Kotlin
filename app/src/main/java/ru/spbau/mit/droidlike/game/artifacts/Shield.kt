package ru.spbau.mit.droidlike.game.artifacts

import ru.spbau.mit.droidlike.game.painter.DrawingParameters
import ru.spbau.mit.droidlike.game.painter.Painter


class Shield : Artifact() {
    init {
        armor = 20.0
    }

    override fun draw(painter: Painter, params: DrawingParameters?) = painter.draw(this, params)
}
