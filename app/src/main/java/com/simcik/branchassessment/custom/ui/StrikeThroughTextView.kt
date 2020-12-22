package com.simcik.branchassessment.custom.ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.simcik.branchassessment.R

class StrikeThroughTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    companion object{
        const val DEFAULT_STROKE = 10.0f
    }
    
    private var midHeight = 0.0f
    private var padding = 0.0f

    // determines whether completed or not
    var completed = false
        set(value) {
            field = value
            invalidate()
        }

    // set transparency of strike-through
    private var transparency: Int = 0
        set(value) {
            field = value
            strikePaint.alpha = value
            invalidate()
        }


    // set color of strike-through
    private var color: Int = Color.RED
        set(value) {
            field = value
            paint.color = value
            invalidate()
        }

    private var strokeSz: Float = DEFAULT_STROKE
        set(value) {
            field = value
            strikePaint.strokeWidth = value
        }

    private val strikePaint = Paint().apply {
        color = Color.RED
        strokeWidth = strokeSz
    }
    
    init {
        applyCustomAttr(getAttributes(attrs))
    }

    private fun getAttributes(attrs: AttributeSet?): TypedArray {
        return context.theme.obtainStyledAttributes(attrs, R.styleable.StrikeThroughTextView, 0, 0)
    }

    private fun applyCustomAttr(attr: TypedArray) {
        try {
            color = attr.getInt(R.styleable.StrikeThroughTextView_color, Color.RED)
            strokeSz = attr.getDimension(R.styleable.StrikeThroughTextView_strokeSz, 10.0f)
            padding = attr.getDimension(R.styleable.StrikeThroughTextView_padding, 25.0f)
            completed = attr.getBoolean(R.styleable.StrikeThroughTextView_completed, false)
            transparency = attr.getInteger(R.styleable.StrikeThroughTextView_transparency, 255)
        } finally {
            attr.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        midHeight = height.halved()
    }

    //determine how long strike should be based on text and padding values
    private fun getTextWidth(): Float {
        return paint.measureText(text.toString()).coerceAtMost((width - paddingStart.double()))
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (completed) {
            canvas?.drawLine(
                paddingStart.toFloat(),
                midHeight,
                getTextWidth() + paddingStart,
                midHeight,
                strikePaint
            )
        }
    }
    
    //extension to halve
    private fun Int.halved(): Float {
        return this * 0.5f
    }

    // extension to double
    private fun Int.double(): Float {
        return this * 2.0f
    }

}