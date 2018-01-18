package ru.spbau.mit.droidlike

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.spbau.mit.droidlike.game.artifacts.Artifact
import ru.spbau.mit.droidlike.game.artifacts.ArtifactFactory
import ru.spbau.mit.droidlike.game.creatures.Creature
import ru.spbau.mit.droidlike.game.creatures.CreatureFactory

class TestCreature {
    private val creature: Creature
    private val artifacts: List<Artifact>

    init {
        creature = CreatureFactory.create(CreatureFactory.CreatureType.GOBLIN)

        artifacts = mutableListOf(ArtifactFactory.ArtifactType.SWORD,
                ArtifactFactory.ArtifactType.SHIELD,
                ArtifactFactory.ArtifactType.MEDICINE_CHEST)
                .map { ArtifactFactory.create(it) }
                .toMutableList()
    }

    @Test
    fun testManipulationsWithArtifacts() {
        testAddition()
        testActivation()
        testDeactivation()
    }

    fun testAddition() {
        artifacts.forEachIndexed { index, artifact ->
            creature.addArtifact(artifact)
            assertEquals(index + 1, creature.artifacts.size)
            assertEquals(0, creature.activatedArtifacts.size)
        }
    }

    fun testActivation() {
        artifacts.forEachIndexed { index, artifact ->
            creature.activateArtifact(artifact)
            assertEquals(artifacts.size - index - 1, creature.artifacts.size)
            assertEquals(index + 1, creature.activatedArtifacts.size)
        }
    }

    fun testDeactivation() {
        artifacts.forEachIndexed { index, artifact ->
            creature.deactivateArtifact(artifact)
            assertEquals(index + 1, creature.artifacts.size)
            assertEquals(artifacts.size - index - 1, creature.activatedArtifacts.size)
        }
    }
}