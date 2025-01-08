package com.taylorz.easytextviewkotlin

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.ColorUtils

class EasyTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    var pressedTextColor: Int = Color.WHITE
        set(value) {
            field = value
            applyBackground()
        }
    var disableTextColor: Int = Color.GRAY
        set(value) {
            field = value
            applyBackground()
        }

    var strokeColor: Int = Color.TRANSPARENT
        set(value) {
            field = value
            applyBackground()
        }

    var normalBackgroundColor: Int = Color.LTGRAY
        set(value) {
            field = value
            pressedBackgroundColor = brightnessColor(value, 0.75f) // 更新按压状态颜色
            applyBackground()
        }
    var pressedBackgroundColor: Int = Color.DKGRAY
        set(value) {
            field = value
            applyBackground()
        }
    var disableBackgroundColor: Int = ColorUtils.setAlphaComponent(normalBackgroundColor, 80)
        set(value) {
            field = value
            applyBackground()
        }

    var strokeWidth: Int = dp2px(1f)
        set(value) {
            field = value
            applyBackground()
        }
    var radius: Int = dp2px(4f)
        set(value) {
            field = value
            applyBackground()
        }
    private var normalText: CharSequence = this.text

    var pressedText: CharSequence = this.text
        set(value) {
            field = value
            applyText()
        }
    var disabledText: CharSequence = this.text
        set(value) {
            field = value
            applyText()
        }
    var selectedText: CharSequence = this.text
        set(value) {
            field = value
            applyText()
        }

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.EasyTextView)
        try {
            pressedTextColor = a.getColor(R.styleable.EasyTextView_textPressedColor, pressedTextColor)
            disableTextColor = a.getColor(R.styleable.EasyTextView_textDisableColor, disableTextColor)

            strokeColor = a.getColor(R.styleable.EasyTextView_strokeColor, strokeColor)
            normalBackgroundColor = a.getColor(R.styleable.EasyTextView_backgroundColor, normalBackgroundColor)
            pressedBackgroundColor = a.getColor(
                R.styleable.EasyTextView_backgroundPressedColor,
                brightnessColor(normalBackgroundColor, 0.75f)
            )
            disableBackgroundColor = a.getColor(
                R.styleable.EasyTextView_backgroundDisableColor,
                ColorUtils.setAlphaComponent(normalBackgroundColor, 80)
            )

            strokeWidth = a.getDimensionPixelSize(R.styleable.EasyTextView_custom_stroke_width, strokeWidth)
            radius = a.getDimensionPixelSize(R.styleable.EasyTextView_corner_radius_value, radius)
            pressedText = a.getString(R.styleable.EasyTextView_pressedText) ?: pressedText
            disabledText = a.getString(R.styleable.EasyTextView_disabledText) ?: disabledText
            selectedText = a.getString(R.styleable.EasyTextView_selectedText) ?: selectedText
        } finally {
            a.recycle()
        }
        applyBackground()
        applyText()
    }

    private fun applyBackground() {
        // 状态 Drawable
        val backgroundDrawable = StateListDrawable().apply {
            addState(intArrayOf(android.R.attr.state_selected), createDrawable(pressedBackgroundColor))
            addState(intArrayOf(android.R.attr.state_pressed), createDrawable(pressedBackgroundColor))
            addState(intArrayOf(-android.R.attr.state_enabled), createDrawable(disableBackgroundColor))
            addState(intArrayOf(), createDrawable(normalBackgroundColor))
        }

        // 状态文字颜色
        val textColorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf()
            ),
            intArrayOf(pressedTextColor, pressedTextColor, disableTextColor, this.currentTextColor)
        )

        background = backgroundDrawable
        setTextColor(textColorStateList)
    }

    private fun createDrawable(color: Int): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius.toFloat()
            setStroke(strokeWidth, strokeColor)
        }
    }

    private fun applyText() {
        // 根据状态设置文本内容
        val text = when {
            isSelected -> selectedText
            !isEnabled -> disabledText
            isPressed -> pressedText
            else -> normalText
        }
        this.text = text
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        applyText() // 更新文本
        applyBackground() // 重新应用背景和文字颜色
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        applyText() // 更新文本
        applyBackground() // 重新应用背景和文字颜色
    }

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)
        applyText() // 更新文本
        applyBackground() // 重新应用背景和文字颜色
    }

    private fun dp2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    private fun brightnessColor(color: Int, brightness: Float): Int {
        if (color == Color.TRANSPARENT) return color
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(color, hsl)
        hsl[2] *= brightness
        return ColorUtils.HSLToColor(hsl)
    }
}