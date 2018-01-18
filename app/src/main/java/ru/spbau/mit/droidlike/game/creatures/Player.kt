package ru.spbau.mit.droidlike.game.creatures

import ru.spbau.mit.droidlike.game.painter.DrawingParameters
import ru.spbau.mit.droidlike.game.painter.Painter
import ru.spbau.mit.droidlike.game.strategies.PlayerStrategy
import ru.spbau.mit.droidlike.game.strategies.Strategy

class Player : Creature() {
    override val strategy: Strategy = PlayerStrategy()

    init {
        hp = 70.0
        maxHp = 70.0
        damage = 10.0
        hpGenerationSpeed = 2.0
    }

    override fun draw(painter: Painter, params: DrawingParameters?) = painter.draw(this, params)
}