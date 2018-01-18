package ru.spbau.mit.droidlike.game.strategies

import ru.spbau.mit.droidlike.game.creatures.Creature
import ru.spbau.mit.droidlike.game.world.World

interface Strategy {
    fun move(creature: Creature, from: Pair<Int, Int>, world: World): Action
}