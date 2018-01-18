package ru.spbau.mit.droidlike.game.creatures

import ru.spbau.mit.droidlike.game.painter.DrawingParameters
import ru.spbau.mit.droidlike.game.painter.Painter
import ru.spbau.mit.droidlike.game.strategies.GreedyStrategy
import ru.spbau.mit.droidlike.game.strategies.Strategy


class Goblin : Creature() {
    override val strategy: Strategy = GreedyStrategy()

    init {
        hp = 30.0
        maxHp = 30.0
        damage = 5.0
        hpGenerationSpeed = 3.0
    }

    override fun draw(painter: Painter, params: DrawingParameters?) = painter.draw(this, params)
}