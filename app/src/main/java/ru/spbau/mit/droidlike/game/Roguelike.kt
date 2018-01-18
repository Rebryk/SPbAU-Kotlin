package ru.spbau.mit.droidlike.game

import ru.spbau.mit.droidlike.game.artifacts.ArtifactFactory
import ru.spbau.mit.droidlike.game.creatures.Creature
import ru.spbau.mit.droidlike.game.creatures.CreatureFactory
import ru.spbau.mit.droidlike.game.painter.Painter
import ru.spbau.mit.droidlike.game.strategies.Action
import ru.spbau.mit.droidlike.game.strategies.PlayerStrategy
import ru.spbau.mit.droidlike.game.world.ArrayField
import ru.spbau.mit.droidlike.game.world.World
import java.util.*

class Roguelike(field: List<List<Boolean>>, val painter: Painter) {
    val player: Creature = CreatureFactory.create(CreatureFactory.CreatureType.PLAYER)
    var finished = false

    companion object {
        val GOBLIN_MAX_COUNT = 5
        val SCAVENGER_MAX_COUNT = 4
        val HEART_MAX_COUNT = 2
        val SWORD_MAX_COUNT = 3
        val SHIELD_MAX_COUNT = 3
    }

    val world: World = World.create {
        val random = Random()

        this.field = ArrayField(field.size, field.first().size, field)

        creatureAtRandomPosition(CreatureFactory.CreatureType.GOBLIN, random.nextInt(GOBLIN_MAX_COUNT))
        creatureAtRandomPosition(CreatureFactory.CreatureType.SCAVENGER, random.nextInt(SCAVENGER_MAX_COUNT))
        artifactAtRandomPosition(ArtifactFactory.ArtifactType.SWORD, random.nextInt(SWORD_MAX_COUNT))
        artifactAtRandomPosition(ArtifactFactory.ArtifactType.SHIELD, random.nextInt(SHIELD_MAX_COUNT))
        artifactAtRandomPosition(ArtifactFactory.ArtifactType.MEDICINE_CHEST, random.nextInt(HEART_MAX_COUNT))
        creatureAt(getFreeRandomPosition()) { player }
    }


    init {
        painter.initialize(field.size, field.first().size)
    }

    fun move(action: Action) {
        (player.strategy as PlayerStrategy).action = action

        if (!finished) {
            finished = !Model.run(world)
        }
    }

    fun win() = finished && !player.isDead()

    inline fun <reified T> getArtifactCount(): Int = player.artifacts.filter { it is T && !it.used }.count()

    inline fun <reified T> getActivatedArtifactCount(): Int = player.activatedArtifacts.filter { it is T && !it.used }.count()

    inline fun <reified T> activate() {
        if (getActivatedArtifactCount<T>() > 0) {
            return
        }

        val artifact = player.getArtifact<T>() ?: return
        player.activateArtifact(artifact)
    }

    inline fun <reified T> deactivate() {
        val artifact = player.getActivatedArtifact<T>() ?: return
        player.deactivateArtifact(artifact)
    }

    fun render() {
        painter.clear()
        world.draw(painter)
        painter.show()
    }
}