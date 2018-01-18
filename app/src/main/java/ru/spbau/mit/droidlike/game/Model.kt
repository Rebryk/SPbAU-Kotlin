package ru.spbau.mit.droidlike.game

import ru.spbau.mit.droidlike.game.creatures.Creature
import ru.spbau.mit.droidlike.game.creatures.Player
import ru.spbau.mit.droidlike.game.strategies.Action
import ru.spbau.mit.droidlike.game.world.World

/**
 * Model implements game logic
 * Responsible for objects interaction
 */
object Model {
    private const val MAX_ARMOR: Double = 10.0
    private const val MIN_DAMAGE_K: Double = 0.3

    /**
     * Simulates the next step of the game
     * @param world description of the world
     * @return true if it isn't the end of the game, false otherwise
     */
    fun run(world: World): Boolean {
        val creatures = moveCreatures(world)
        creatures.removeIf { it.second.heal(); it.second.isDead() }
        world.creatures.apply { clear(); putAll(creatures) }

        giveArtifacts(world)

        return !isTheEndOfTheGame(world)
    }

    /**
     * Changes the given position according to the action
     * @param position position to move
     * @param action current action
     * @return new position
     */
    private fun movePosition(position: Pair<Int, Int>, action: Action): Pair<Int, Int> = when (action) {
        Action.GO_UP -> Pair(position.first - 1, position.second)
        Action.GO_DOWN -> Pair(position.first + 1, position.second)
        Action.GO_LEFT -> Pair(position.first, position.second - 1)
        Action.GO_RIGHT -> Pair(position.first, position.second + 1)
        else -> position
    }

    /**
     * Move creature
     * If the cell is occupied, creature attacks
     * @param creature creature to move
     * @param from current position of the creature
     * @param world description of the world
     * @return new position of the creature
     */
    private fun moveCreature(creature: Creature, from: Pair<Int, Int>, world: World): Pair<Pair<Int, Int>, Creature> {
        var position = movePosition(from, creature.move(from, world))

        if (position != from) {
            world.creatures[position]?.let {
                creature attack it
                position = from
            }
        }

        if (world.field.isWall(position.first, position.second)) {
            position = from
        }

        return Pair(position, creature)
    }

    /**
     * Moves all creatures
     * @param world description of the world
     * @return new positions of the creatures
     */
    private fun moveCreatures(world: World): MutableList<Pair<Pair<Int, Int>, Creature>> {
        return world.creatures.map { moveCreature(it.value, it.key, world) }.toMutableList()
    }

    /**
     * Gives an artifact to a creature if they are in the same cell
     * @param world description of the world
     */
    private fun giveArtifacts(world: World) {
        world.creatures.forEach { (position, creature) ->
            world.artifacts
                    .getOrElse(position, { mutableListOf() })
                    .removeAll { creature.addArtifact(it); true }
        }
    }

    /**
     * Implements fight logic
     */
    private infix fun Creature.attack(target: Creature) {
        target.hp -= damage * Math.max(MIN_DAMAGE_K, 1.0 - target.armor / MAX_ARMOR)
    }

    /**
     * Checks the end of the game
     * @param world description of the world
     * @return true if player is dead or there is no enemies, false otherwise
     */
    private fun isTheEndOfTheGame(world: World): Boolean {
        return world.creatures.size == 1 || world.creatures.filter { it.value is Player }.isEmpty()
    }
}