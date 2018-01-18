package ru.spbau.mit.droidlike.game.artifacts

import java.util.*

/**
 * Factory for generating artifacts of different types
 */
object ArtifactFactory {
    enum class ArtifactType {
        SWORD,
        SHIELD,
        MEDICINE_CHEST;

        companion object {
            fun random(): ArtifactType = ArtifactType.values()[Random().nextInt(ArtifactType.values().size)]
        }
    }

    /**
     * Creates artifact of the given type
     * @param type artifact type
     * @return artifact of the given type
     */
    fun create(type: ArtifactType): Artifact = when (type) {
        ArtifactType.SWORD -> Sword()
        ArtifactType.SHIELD -> Shield()
        ArtifactType.MEDICINE_CHEST -> MedicineChest()
    }
}