package ru.spbau.mit.droidlike.game.strategies

import ru.spbau.mit.droidlike.game.creatures.Creature
import ru.spbau.mit.droidlike.game.world.World


class GreedyStrategy : Strategy {
    /**
     * Activates all artifacts
     * Tries to attack enemies in the adjacent cells
     * Makes random action if there is no enemies
     * @param creature creature to move
     * @param from current position of the creature
     * @param world description of the world
     * @return action
     */
    override fun move(creature: Creature, from: Pair<Int, Int>, world: World): Action {
        creature.artifacts.forEach { creature.activateArtifact(it) }

        world.creatures[Pair(from.first - 1, from.second)]?.let {
            return Action.GO_UP
        }

        world.creatures[Pair(from.first + 1, from.second)]?.let {
            return Action.GO_DOWN
        }

        world.creatures[Pair(from.first, from.second - 1)]?.let {
            return Action.GO_LEFT
        }

        world.creatures[Pair(from.first, from.second + 1)]?.let {
            return Action.GO_RIGHT
        }

        return Action.random()
    }
}