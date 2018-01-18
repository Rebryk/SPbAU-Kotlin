package ru.spbau.mit.droidlike.game.painter

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.createScaledBitmap
import android.util.AttributeSet
import android.view.View
import ru.spbau.mit.droidlike.R
import ru.spbau.mit.droidlike.game.artifacts.MedicineChest
import ru.spbau.mit.droidlike.game.artifacts.Shield
import ru.spbau.mit.droidlike.game.artifacts.Sword
import ru.spbau.mit.droidlike.game.creatures.Goblin
import ru.spbau.mit.droidlike.game.creatures.Player
import ru.spbau.mit.droidlike.game.creatures.Scavenger
import ru.spbau.mit.droidlike.game.world.ArrayField
import ru.spbau.mit.droidlike.game.world.World

class AndroidPainter : Painter, View {
    private val paint: Paint = Paint()
    private var field: Bitmap? = null
    private lateinit var canvas: Canvas

    private var xShift: Int = 0
    private var yShift: Int = 0
    private var fieldWidth: Int = 0
    private var fieldHeight: Int = 0
    private var cellSize: Int = 0

    companion object {
        val BACKGROUND_COLOR: Int = Color.rgb(238, 242, 167)
    }

    var onLayoutCallback: () -> Unit = {}

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun initialize(height: Int, width: Int) {
        fieldHeight = height
        fieldWidth = width
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        field?.let {
            canvas.drawBitmap(it, 0.0f, 0.0f, paint)
        }
    }

    override fun clear() {
        canvas.drawColor(Color.BLACK)
    }

    override fun show() {
        this.invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        field = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(field)
        cellSize = Math.min(canvas.height / fieldHeight, canvas.width / fieldWidth)
        xShift = (canvas.height - cellSize * fieldHeight) / 2
        yShift = (canvas.width - cellSize * fieldWidth) / 2

        onLayoutCallback()
    }

    override fun draw(artifact: Sword, params: DrawingParameters?) = drawObject(R.drawable.sword, params)

    override fun draw(artifact: Shield, params: DrawingParameters?) = drawObject(R.drawable.shield, params)

    override fun draw(artifact: MedicineChest, params: DrawingParameters?) = drawObject(R.drawable.medicine, params)

    override fun draw(creature: Player, params: DrawingParameters?) {
        drawObject(R.drawable.android, params)

        drawHP(creature, 10.0f, 5.0f)
        drawDamage(creature, 10.0f + 5.2f * cellSize, 5.0f)
        drawArmor(creature, 10.0f + 7 * cellSize, 5.0f)
    }

    override fun draw(creature: Goblin, params: DrawingParameters?) = drawObject(R.drawable.goblin, params)

    override fun draw(creature: Scavenger, params: DrawingParameters?) = drawObject(R.drawable.scavenger, params)

    override fun draw(world: World, params: DrawingParameters?) {
        world.field.draw(this)
        world.creatures.forEach { pos, creature -> creature.draw(this, DrawingParameters(pos)) }
        world.artifacts.forEach { pos, artifacts -> artifacts.forEach { it.draw(this, DrawingParameters(pos)) } }
    }

    override fun draw(field: ArrayField, params: DrawingParameters?) {
        for (i in 0 until fieldHeight) {
            for (j in 0 until fieldWidth) {
                if (field.isWall(i, j)) {
                    drawObject(R.drawable.wall, DrawingParameters(i, j))
                } else {
                    drawCell(i, j, BACKGROUND_COLOR)
                }
            }
        }
    }

    private fun drawCell(x: Int, y: Int, color: Int) {
        val cell = Rect(y * cellSize + yShift,
                x * cellSize + xShift,
                (y + 1) * cellSize + yShift,
                (x + 1) * cellSize + xShift)

        paint.color = color
        paint.style = Paint.Style.FILL
        canvas.drawRect(cell, paint)
    }

    private fun drawObject(id: Int, params: DrawingParameters?) {
        if (params == null) {
            throw NullPointerException("DrawingParameters is null")
        }

        val src = BitmapFactory.decodeResource(context.resources, id)
        val image = createScaledBitmap(src, cellSize, cellSize, true)
        canvas.drawBitmap(image, 1.0f * params.y * cellSize + yShift, 1.0f * params.x * cellSize + xShift, paint)
    }

    private fun drawHP(player: Player, left: Float, top: Float) {
        val src = BitmapFactory.decodeResource(context.resources, R.drawable.heart)
        val image = createScaledBitmap(src, cellSize, cellSize, true)
        canvas.drawBitmap(image, left, top, paint)

        val length = 4 * cellSize
        val healthRatio = (player.hp / player.maxHp).toFloat()

        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        canvas.drawRect(left + cellSize,
                top + 0.25f * cellSize,
                left + cellSize + healthRatio * length,
                top + 0.75f * cellSize,
                paint)

        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        canvas.drawRect(left + cellSize,
                top + 0.25f * cellSize,
                left + cellSize + length,
                top + 0.75f * cellSize,
                paint)

        val textPaint = Paint()
        textPaint.color = Color.BLACK
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = 36.0f
        canvas.drawText("${player.hp}/${player.maxHp} (+${player.hpGenerationSpeed})",
                left + 3.0f * cellSize,
                top + 0.5f * cellSize - (textPaint.descent() + textPaint.ascent()) / 2,
                textPaint)
    }

    private fun drawDamage(player: Player, left: Float, top: Float) {
        val src = BitmapFactory.decodeResource(context.resources, R.drawable.sword)
        val image = createScaledBitmap(src, cellSize, cellSize, true)
        canvas.drawBitmap(image, left, top, paint)

        val textPaint = Paint()
        textPaint.color = Color.BLACK
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = 36.0f
        canvas.drawText("${player.damage}",
                left + 1.2f * cellSize,
                top + 0.5f * cellSize - (textPaint.descent() + textPaint.ascent()) / 2,
                textPaint)
    }

    private fun drawArmor(player: Player, left: Float, top: Float) {
        val src = BitmapFactory.decodeResource(context.resources, R.drawable.shield)
        val image = createScaledBitmap(src, cellSize, cellSize, true)
        canvas.drawBitmap(image, left, top, paint)

        val textPaint = Paint()
        textPaint.color = Color.BLACK
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = 36.0f
        canvas.drawText("${player.armor}",
                left + 1.2f * cellSize,
                top + 0.5f * cellSize - (textPaint.descent() + textPaint.ascent()) / 2,
                textPaint)
    }
}