package ru.spbau.mit.droidlike.game.creatures

import ru.spbau.mit.droidlike.game.artifacts.Artifact
import ru.spbau.mit.droidlike.game.painter.Drawable
import ru.spbau.mit.droidlike.game.strategies.Action
import ru.spbau.mit.droidlike.game.strategies.Strategy
import ru.spbau.mit.droidlike.game.world.World


abstract class Creature : CreatureParameters(), Drawable {
    abstract val strategy: Strategy
    val artifacts: MutableList<Artifact> = mutableListOf()
    val activatedArtifacts: MutableList<Artifact> = mutableListOf()

    fun addArtifact(artifact: Artifact) = artifacts.add(artifact)

    /**
     * Runs the strategy to get next move
     * @param from current position of the creature
     * @param world description of the world
     */
    fun move(from: Pair<Int, Int>, world: World): Action = strategy.move(this, from, world)

    /**
     * Activates the given artifact
     * Activation of the artifact improves creature parameters according to the artifact properties
     * @param artifact for activation
     */
    fun activateArtifact(artifact: Artifact) {
        if (artifacts.remove(artifact)) {
            artifact.activate(this)
            activatedArtifacts.add(artifact)
        }
    }

    /**
     * Deactivates the given artifact
     * Deactivation of the artifact returns creature parameters to the initial state
     * @param artifact for deactivation
     */
    fun deactivateArtifact(artifact: Artifact) {
        if (activatedArtifacts.remove(artifact)) {
            artifact.deactivate(this)
            artifacts.add(artifact)
        }
    }

    inline fun <reified T> getArtifactCount(): Int = artifacts.filter { it is T && !it.used }.count()

    inline fun <reified T> getActivatedArtifactCount(): Int = activatedArtifacts.filter { it is T && !it.used }.count()

    inline fun <reified T> getArtifact(): Artifact? = artifacts.firstOrNull { it is T && !it.used }

    inline fun <reified T> getActivatedArtifact(): Artifact? = activatedArtifacts.firstOrNull { it is T && !it.used }

    fun heal() {
        hp = Math.max(0.0, Math.min(hp + hpGenerationSpeed, maxHp))
    }

    fun isDead() = hp == 0.0
}