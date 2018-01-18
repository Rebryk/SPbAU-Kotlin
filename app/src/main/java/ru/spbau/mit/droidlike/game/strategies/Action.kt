package ru.spbau.mit.droidlike.game.strategies

import java.util.*

enum class Action {
    GO_LEFT,
    GO_RIGHT,
    GO_UP,
    GO_DOWN,
    SKIP;

    companion object {
        fun random(): Action = values()[Random().nextInt(values().size)]
    }
}