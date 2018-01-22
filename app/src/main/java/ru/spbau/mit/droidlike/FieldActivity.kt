package ru.spbau.mit.droidlike

import kotlinx.android.synthetic.main.activity_field.*
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import ru.spbau.mit.droidlike.game.Roguelike
import ru.spbau.mit.droidlike.game.artifacts.MedicineChest
import ru.spbau.mit.droidlike.game.artifacts.Shield
import ru.spbau.mit.droidlike.game.artifacts.Sword
import ru.spbau.mit.droidlike.game.painter.AndroidPainter
import ru.spbau.mit.droidlike.game.strategies.Action
import java.util.*


class FieldActivity : AppCompatActivity() {
    companion object {
        private val SMALL_ALPHA: Float = 0.50f
        private val DEFAULT_ALPHA: Float = 0.85f
    }

    private lateinit var game: Roguelike

//    private lateinit var up: ImageButton
//    private lateinit var down: ImageButton
//    private lateinit var left: ImageButton
//    private lateinit var right: ImageButton
//
//    private lateinit var heart: ImageButton
//    private lateinit var shield: ImageButton
//    private lateinit var sword: ImageButton

    private lateinit var player: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_field)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        up.alpha = DEFAULT_ALPHA
        up.setOnClickListener {
            clickOnArrow(Action.GO_UP)
        }

        down.alpha = DEFAULT_ALPHA
        down.setOnClickListener {
            clickOnArrow(Action.GO_DOWN)
        }

        left.alpha = DEFAULT_ALPHA
        left.setOnClickListener {
            clickOnArrow(Action.GO_LEFT)
        }

        right.alpha = DEFAULT_ALPHA
        right.setOnClickListener {
            clickOnArrow(Action.GO_RIGHT)
        }

        heart.setOnClickListener {
            clickOnArtifact<MedicineChest>()
        }

        shield.setOnClickListener {
            clickOnArtifact<Shield>()
        }

        sword.setOnClickListener {
            clickOnArtifact<Sword>()
        }

        player = MediaPlayer.create(this@FieldActivity, R.raw.sound)
        player.isLooping = true
        player.setVolume(1.0f, 1.0f)

        startGame()
    }

    private inline fun <reified T> clickOnArtifact() {
        if (game.getActivatedArtifactCount<T>() == 0) {
            game.activate<T>()
        } else {
            game.deactivate<T>()
        }

        game.render()
        updateButtons()
    }

    private fun notifyUser() {
        Toast.makeText(this, if (game.win()) "You win!" else "You lose!", Toast.LENGTH_LONG).show()
    }

    private fun clickOnArrow(action: Action) {
        game.move(action)

        if (game.finished) {
            notifyUser()
            startGame()
        } else {
            game.render()
            updateButtons()
        }
    }

    private inline fun <reified T> updateArtifactButton(button: ImageButton) {
        if (game.getActivatedArtifactCount<T>() > 0) {
            button.visibility = View.VISIBLE
            button.alpha = DEFAULT_ALPHA
        } else if (game.getArtifactCount<T>() > 0) {
            button.visibility = View.VISIBLE
            button.alpha = SMALL_ALPHA
        } else {
            button.visibility = View.INVISIBLE
        }
    }

    private fun updateButtons() {
        updateArtifactButton<MedicineChest>(heart)
        updateArtifactButton<Shield>(shield)
        updateArtifactButton<Sword>(sword)
    }

    private fun startGame() {
        val painter = findViewById<AndroidPainter>(R.id.androidPainter)
        painter.onLayoutCallback = {
            game.render()
        }

        val fields = application.assets.list("fields")
        val fieldName = "fields/${fields[Random().nextInt(fields.size)]}"

        // read field
        val field = application.assets.open(fieldName).bufferedReader().use {
            it.readLines().map { row -> row.map { cell -> cell == '#' }.toMutableList() }
        }

        game = Roguelike(field, painter)
        updateButtons()
    }

    override fun onResume() {
        super.onResume()
        player.start()
    }

    override fun onPause() {
        super.onPause()
        player.stop()
    }
}
