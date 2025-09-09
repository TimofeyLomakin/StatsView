package com.example.statsview.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.example.statsview.R
import com.example.statsview.utils.AndroidUtils
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random


class StatsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(
    context,
    attributeSet,
    defStyleAttr,
    defStyleRes
) {

    private var textSize = AndroidUtils.dp(context, 20).toFloat()
    private var lineWidth = AndroidUtils.dp(context, 5)
    private var colors = emptyList<Int>()
    private var point = AndroidUtils.dp(context, 5).toFloat()

    init {
        context.withStyledAttributes(attributeSet, R.styleable.StatsView) {
            textSize = getDimension(R.styleable.StatsView_textSize, textSize)
            lineWidth = getDimension(R.styleable.StatsView_lineWidth, lineWidth.toFloat()).toInt()
            point = getDimension(R.styleable.StatsView_point, point)

            colors = listOf(
                getColor(R.styleable.StatsView_color1, generateRandomColor()),
                getColor(R.styleable.StatsView_color2, generateRandomColor()),
                getColor(R.styleable.StatsView_color3, generateRandomColor()),
                getColor(R.styleable.StatsView_color4, generateRandomColor()),
            )
        }
    }

    private var translateData : List<Float> = emptyList()
    var data: List<Float> = emptyList()
        set(value) {
            field = value
            val sum = value.sum()
            translateData = if (sum != 0F) {
                value.map {
                    it / sum
                }
            } else {
                value.map { 0F }
            }
            invalidate()
        }
    private var radius = 0F
    private var center = PointF()
    private var oval = RectF()
    private val paint = Paint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        strokeWidth = lineWidth.toFloat()
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        textSize = this@StatsView.textSize
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }

    private val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2F - AndroidUtils.dp(context, 5)
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            center.x - radius,
            center.y - radius,
            center.x + radius,
            center.y + radius
        )
    }

    override fun onDraw(canvas: Canvas) {
        if (data.isEmpty()) {
            return
        }

        var startAngle = -90F
        translateData.forEachIndexed { index, datum ->
            var angle = datum * 360F
            paint.color = colors.getOrElse(index) { generateRandomColor() }
            canvas.drawArc(oval, startAngle, angle, false, paint)
            startAngle += angle
        }

        if (translateData.isNotEmpty() && colors.isNotEmpty()) {
            val startAngle = Math.toRadians(-90.0)
            val pointX = center.x + radius * cos(startAngle).toFloat()
            val pointY = center.y + radius * sin(startAngle).toFloat()

            pointPaint.color = colors[0]
            canvas.drawCircle(pointX, pointY, point, pointPaint)
        }

        canvas.drawText(
            "%.2f%%".format(translateData.sum() * 100),
            center.x,
            center.y + textPaint.textSize / 4,
            textPaint
        )
    }
}

private fun generateRandomColor(): Int = Random.nextInt(0xFF000000.toInt(), 0x1000000.toInt())