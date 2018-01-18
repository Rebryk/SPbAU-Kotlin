package ru.spbau.mit.droidlike.game.artifacts

import ru.spbau.mit.droidlike.game.painter.DrawingParameters
import ru.spbau.mit.droidlike.game.painter.Painter

class MedicineChest : Artifact() {
    init {
        hp = 10.0
        disposable = true
    }

    override fun draw(painter: Painter, params: DrawingParameters?) = painter.draw(this, params)
}