package com.example.story_app.ui.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.story_app.R

class ButtonStroke : AppCompatButton {
    private lateinit var bgCollor: Drawable
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setBackgroundDrawable(bgCollor)
        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, R.color.purple_light)
        bgCollor = ContextCompat.getDrawable(
            context,
            R.drawable.stroke_button
        ) as Drawable
    }
}