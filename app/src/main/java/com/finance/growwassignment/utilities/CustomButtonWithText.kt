package com.finance.growwassignment.utilities

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.finance.growwassignment.R

class CustomButtonWithText : ConstraintLayout {

    private lateinit var rootLayout: ConstraintLayout
    private lateinit var primaryTv: TextView
    private lateinit var secondaryTv: TextView


    lateinit var primaryText: String
    private var secondaryText: String? = null

    private var primaryTextColor = -1
    private var secondaryTextColor = -1

    private var primaryTextSelectedColor = -1
    private var secondaryTextSelectedColor = -1

    private var isButtonSelected = false

    private var selectedBGColor: Drawable? = null
    private var bgColor: Drawable? = null


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
        val data = context.obtainStyledAttributes(attrs, R.styleable.CustomButtonWithText)

        isButtonSelected =
            data.getBoolean(R.styleable.CustomButtonWithText_isCTButtonWithTextSelected, false)

        bgColor = data.getDrawable(R.styleable.CustomButtonWithText_CTButtonWithTextBgColor)
        selectedBGColor =
            data.getDrawable(R.styleable.CustomButtonWithText_CTButtonWithTextSelectedBgColor)

        primaryText = data.getString(R.styleable.CustomButtonWithText_primaryText) ?: "W"
        secondaryText = data.getString(R.styleable.CustomButtonWithText_secondaryText)

        primaryTextColor = data.getColor(
            R.styleable.CustomButtonWithText_primaryTextColor,
            ContextCompat.getColor(context, R.color.secondaryColor)
        )
        secondaryTextColor = data.getColor(
            R.styleable.CustomButtonWithText_secondaryTextColor,
            ContextCompat.getColor(context, R.color.secondaryColor)
        )

        primaryTextSelectedColor = data.getColor(
            R.styleable.CustomButtonWithText_primarySelectedTextColor,
            ContextCompat.getColor(context, R.color.white)
        )
        secondaryTextSelectedColor = data.getColor(
            R.styleable.CustomButtonWithText_secondarySelectedTextColor,
            ContextCompat.getColor(context, R.color.white)
        )


        data.recycle()
    }


    private fun initView(context: Context) {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
        LayoutInflater.from(context).inflate(R.layout.custom_button_text, this, true)

        rootLayout = findViewById(R.id.ct_btn_text_root)
        primaryTv = findViewById(R.id.ct_btn_text_tv_primary)
        secondaryTv = findViewById(R.id.ct_btn_text_tv_secondary)

        setView()

        if (isButtonSelected) {
            setSelectedView()
        }
    }

    public fun toggleButton() {

        if (isButtonSelected) {

            setView()
            isButtonSelected = false

        } else {

            setSelectedView()
            isButtonSelected = true

        }
    }

    fun setButtonSelected() {

        isButtonSelected = true
        setSelectedView()

    }


    fun setView() {

        rootLayout.background = bgColor

        primaryTv.text = primaryText
        primaryTv.setTextColor(primaryTextColor)

        if (secondaryText != null) {

            secondaryTv.visibility = View.VISIBLE
            secondaryTv.text = secondaryText
            secondaryTv.setTextColor(secondaryTextColor)
        } else {
            secondaryTv.visibility = View.GONE
        }
    }

    private fun setSelectedView() {

        rootLayout.background = selectedBGColor

        primaryTv.setTextColor(primaryTextSelectedColor)
        secondaryTv.setTextColor(secondaryTextSelectedColor)

    }

    public fun isButtonSelected(): Boolean {
        return isButtonSelected
    }

    fun retrievePrimaryText() : String{
        return primaryText
    }

    fun setButtonUnselected() {
        isButtonSelected = false
        setView()
    }
}