package com.taylorz.easytextviewkotlin

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * 自动适配网格布局
 * @Author: Taylor
 * @CreateDate: 2023/5/24
 */
open class AutoFitGridLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    var verticalSpace: Int = 0
        set(value) {
            field = value
            requestLayout()
        }

    var horizontalSpace: Int = 0
        set(value) {
            field = value
            requestLayout()
        }

    var columnCount: Int = 0
        set(value) {
            field = value
            requestLayout()
        }

    private var childWidth = 0
    private val visibleChildren = mutableListOf<View>()

    init {
        attrs?.let {
            val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoFitGridLayout)
            columnCount = a.getInt(R.styleable.AutoFitGridLayout_columnCount, 1)
            horizontalSpace = a.getDimensionPixelSize(R.styleable.AutoFitGridLayout_horizontalSpace, 0)
            verticalSpace = a.getDimensionPixelSize(R.styleable.AutoFitGridLayout_verticalSpace, 0)
            a.recycle()
        }
    }

    private fun refreshVisibleChildren() {
        visibleChildren.clear()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                visibleChildren.add(child)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        refreshVisibleChildren()

        if (columnCount <= 0) return

        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        childWidth = (parentWidth - (columnCount - 1) * horizontalSpace) / columnCount

        var totalHeight = paddingTop
        var currentRowHeight = 0
        var childrenInRow = 0

        visibleChildren.forEach { child ->
            measureChild(
                child,
                MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            currentRowHeight = maxOf(currentRowHeight, child.measuredHeight)
            childrenInRow++

            if (childrenInRow == columnCount) {
                totalHeight += currentRowHeight + verticalSpace
                currentRowHeight = 0
                childrenInRow = 0
            }
        }

        if (childrenInRow > 0) {
            totalHeight += currentRowHeight // Add last row height
        }
        totalHeight += paddingBottom

        setMeasuredDimension(parentWidth, totalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val parentPaddingLeft = paddingLeft
        var x = parentPaddingLeft
        var y = paddingTop
        var currentRowHeight = 0

        visibleChildren.forEachIndexed { index, child ->
            val childHeight = child.measuredHeight
            val childWidth = child.measuredWidth

            if (index % columnCount == 0 && index != 0) {
                x = parentPaddingLeft
                y += currentRowHeight + verticalSpace
                currentRowHeight = 0
            }

            child.layout(x, y, x + childWidth, y + childHeight)
            x += childWidth + horizontalSpace
            currentRowHeight = maxOf(currentRowHeight, childHeight)
        }
    }
}