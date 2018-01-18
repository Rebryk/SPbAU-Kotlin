package ru.spbau.mit.droidlike.game.creatures

import ru.spbau.mit.droidlike.game.painter.DrawingParameters
import ru.spbau.mit.droidlike.game.painter.Painter
import ru.spbau.mit.droidlike.game.strategies.GreedyStrategy
import ru.spbau.mit.droidlike.game.strategies.Strategy


class Scavenger : Creature() {
    override val strategy: Strategy = GreedyStrategy()

    init {
        hp = 50.0
        maxHp = 50.0
        damage = 20.0
        hpGenerationSpeed = 1.0
    }

    override fun draw(painter: Painter, params: DrawingParameters?) = painter.draw(this, params)
}