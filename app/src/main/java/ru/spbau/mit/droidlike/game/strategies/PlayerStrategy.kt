package ru.spbau.mit.droidlike.game.strategies

import ru.spbau.mit.droidlike.game.creatures.Creature
import ru.spbau.mit.droidlike.game.world.World

class PlayerStrategy : Strategy {
    var action: Action = Action.SKIP

    override fun move(creature: Creature, from: Pair<Int, Int>, world: World): Action = action
}