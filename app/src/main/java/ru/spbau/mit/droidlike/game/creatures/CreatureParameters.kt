package ru.spbau.mit.droidlike.game.creatures

/**
 * Class to store different parameters of the creature
 */
abstract class CreatureParameters {
    var hp: Double = 0.0
    var maxHp: Double = 0.0
    var armor: Double = 0.0
    var damage: Double = 0.0
    var hpGenerationSpeed: Double = 0.0
}