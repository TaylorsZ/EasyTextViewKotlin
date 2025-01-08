package com.taylorz.easytextviewkotlin

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity

open class IconFontTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : EasyTextView(context, attrs, defStyleAttr) {
    init {
        init()
    }
    private fun init() {
        typeface = Typeface.createFromAsset(context.assets, "iconfont.ttf")
        this.gravity = Gravity.CENTER
    }
}