package ru.spbau.mit.droidlike

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.spbau.mit.droidlike.game.artifacts.Artifact
import ru.spbau.mit.droidlike.game.artifacts.ArtifactFactory
import ru.spbau.mit.droidlike.game.creatures.Creature
import ru.spbau.mit.droidlike.game.creatures.CreatureFactory

class TestArtifact {
    private val creature: Creature
    private val sword: Artifact
    private val medicineChest: Artifact

    companion object {
        val EPS: Double = 1e-5
    }

    init {
        creature = CreatureFactory.create(CreatureFactory.CreatureType.GOBLIN)
        sword = ArtifactFactory.create(ArtifactFactory.ArtifactType.SWORD)
        medicineChest = ArtifactFactory.create(ArtifactFactory.ArtifactType.MEDICINE_CHEST)

        creature.hp = 0.0
        creature.addArtifact(sword)
        creature.addArtifact(medicineChest)
    }

    @Test
    fun testActivation() {
        creature.activateArtifact(sword)
        assertEquals(15.0, creature.damage, EPS)
    }

    @Test
    fun testDeactivation() {
        creature.deactivateArtifact(sword)
        assertEquals(5.0, creature.damage, EPS)
    }

    @Test
    fun testDisposableArtifact() {
        creature.activateArtifact(medicineChest)
        assertEquals(10.0, creature.hp, EPS)
        creature.deactivateArtifact(medicineChest)

        creature.activateArtifact(medicineChest)
        assertEquals(10.0, creature.hp, EPS)
    }
}
