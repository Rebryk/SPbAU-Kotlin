package ru.spbau.mit.droidlike.game.world

import ru.spbau.mit.droidlike.game.artifacts.Artifact
import ru.spbau.mit.droidlike.game.artifacts.ArtifactFactory
import ru.spbau.mit.droidlike.game.creatures.Creature
import ru.spbau.mit.droidlike.game.creatures.CreatureFactory
import ru.spbau.mit.droidlike.game.painter.Drawable
import ru.spbau.mit.droidlike.game.painter.DrawingParameters
import ru.spbau.mit.droidlike.game.painter.Painter
import java.util.*


/**
 * Class to describe the state of the game
 * Stores field, creatures, artifacts
 */
class World private constructor(val field: WorldField,
                                val creatures: MutableMap<Pair<Int, Int>, Creature>,
                                val artifacts: MutableMap<Pair<Int, Int>, MutableList<Artifact>>) : Drawable {

    private constructor(builder: Builder) : this(builder.field, builder.creatures, builder.artifacts)

    override fun draw(painter: Painter, params: DrawingParameters?) = painter.draw(this, params)

    companion object {
        fun create(init: Builder.() -> Unit) = Builder(init).build()
    }

    /**
     * Builder for creating instances of the World
     */
    class Builder private constructor() {
        lateinit var field: WorldField
        val creatures: MutableMap<Pair<Int, Int>, Creature> = mutableMapOf()
        val artifacts: MutableMap<Pair<Int, Int>, MutableList<Artifact>> = mutableMapOf()

        companion object {
            private val random = Random()
        }

        constructor(init: Builder.() -> Unit) : this() {
            init()
        }

        fun build(): World = World(this)

        /**
         * Returns a free random position
         * @return a free random position
         */
        tailrec fun getFreeRandomPosition(): Pair<Int, Int> {
            val pos = Pair(random.nextInt(field.height), random.nextInt(field.width))

            if (!field.isWall(pos.first, pos.second) && !creatures.containsKey(pos) && !artifacts.containsKey(pos)) {
                return pos
            }

            return getFreeRandomPosition()
        }

        /**
         * Creates a creature in the given position
         * @param position coordinates of the position
         * @param init method to create creature
         * @return builder
         */
        fun creatureAt(position: Pair<Int, Int>, init: Builder.() -> Creature): Builder = apply {
            creatures.put(position, init())
        }

        /**
         * Creates an artifact in the given position
         * @param position coordinates of the position
         * @param init method to create artifact
         * @return builder
         */
        fun artifactAt(position: Pair<Int, Int>, init: Builder.() -> Artifact): Builder = apply {
            artifacts.getOrPut(position, { mutableListOf() }).add(init())
        }

        /**
         * Creates creatures of the given type at random positions
         * @param type creature type
         * @param count count of creatures
         */
        fun creatureAtRandomPosition(type: CreatureFactory.CreatureType, count: Int = 1) {
            repeat(count) {
                creatureAt(getFreeRandomPosition()) {
                    CreatureFactory.create(type)
                }
            }
        }

        /**
         * Creates artifacts of the given type at random positions
         * @param type artifact type
         * @param count count of artifacts
         */
        fun artifactAtRandomPosition(type: ArtifactFactory.ArtifactType, count: Int = 1) {
            repeat(count) {
                artifactAt(getFreeRandomPosition()) {
                    ArtifactFactory.create(type)
                }
            }
        }
    }
}