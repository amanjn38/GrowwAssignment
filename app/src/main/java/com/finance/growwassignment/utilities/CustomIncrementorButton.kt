package com.finance.growwassignment.utilities

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.finance.growwassignment.R

class CustomIncrementorButton : LinearLayout {

    private lateinit var root: LinearLayout
    public lateinit var incView: ImageView
    public lateinit var decView: ImageView
    private lateinit var value: TextView

    private var minimum = 1
    private var maximum = 6
    private var currentValue = minimum

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        getDataFromXML(attrs, context)
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        getDataFromXML(attrs, context)
        initView(context)
    }

    private fun getDataFromXML(attrs: AttributeSet?, context: Context) {

        val data = context.obtainStyledAttributes(attrs, R.styleable.CustomIncrementorButton)

        minimum = data.getInt(R.styleable.CustomIncrementorButton_minCounter, 0)
        maximum = data.getInt(R.styleable.CustomIncrementorButton_maxCounter, 5000)

        currentValue = minimum

        data.recycle()
    }

    private fun initView(context: Context) {

        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        LayoutInflater.from(context).inflate(R.layout.custom_button_incrementor, this, true)

        root = findViewById(R.id.ct_btn_inc_root)
        incView = findViewById(R.id.ct_btn_inc_inc)
        decView = findViewById(R.id.ct_btn_inc_dec)
        value = findViewById(R.id.ct_btn_inc_value)

        value.text = currentValue.toString()

    }

    fun increment() {
        if (currentValue >= maximum)
            return
        currentValue++
        value.text = currentValue.toString()
    }

    fun decrement() {

        if (currentValue <= minimum)
            return

        currentValue--
        value.text = currentValue.toString()
    }


    fun getCurrentValue() = currentValue

    fun setValue(num: Int) {
        currentValue = num.coerceIn(minimum, maximum)
        value.text = currentValue.toString()
    }
}