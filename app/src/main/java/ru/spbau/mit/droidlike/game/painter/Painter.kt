package ru.spbau.mit.droidlike.game.painter

import ru.spbau.mit.droidlike.game.artifacts.MedicineChest
import ru.spbau.mit.droidlike.game.artifacts.Shield
import ru.spbau.mit.droidlike.game.artifacts.Sword
import ru.spbau.mit.droidlike.game.creatures.Goblin
import ru.spbau.mit.droidlike.game.creatures.Player
import ru.spbau.mit.droidlike.game.creatures.Scavenger
import ru.spbau.mit.droidlike.game.world.ArrayField
import ru.spbau.mit.droidlike.game.world.World


interface Painter {
    fun initialize(height: Int, width: Int)

    fun clear()
    fun show()

    fun draw(artifact: Sword, params: DrawingParameters?)
    fun draw(artifact: Shield, params: DrawingParameters?)
    fun draw(artifact: MedicineChest, params: DrawingParameters?)

    fun draw(creature: Player, params: DrawingParameters?)
    fun draw(creature: Goblin, params: DrawingParameters?)
    fun draw(creature: Scavenger, params: DrawingParameters?)

    fun draw(world: World, params: DrawingParameters?)
    fun draw(field: ArrayField, params: DrawingParameters?)
}