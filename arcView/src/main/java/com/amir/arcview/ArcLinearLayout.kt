package com.amir.arcview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class ArcLinearLayout : LinearLayout {
    private var radiusPow2 = 0f
    private var arcElevation = 0f
    private var radius = 0f
    private var containerWidth = 0
    private var containerHeight = 0
    private var startOffset = 0
    private var itemsOffset = 0f

    constructor(context: Context) : super(context) {
        starUp(context, null)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        starUp(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        starUp(context, attrs)
    }

    private fun starUp(
        context: Context,
        attrs: AttributeSet?
    ) {
        var minimumPadding = (MIN_PADDING * resources.displayMetrics.density).toInt()
        orientation = HORIZONTAL
        if (attrs != null) {
            val obtainedStyle = context.obtainStyledAttributes(attrs, R.styleable.ArcLinearLayout)
            for (i in 0 until obtainedStyle.indexCount) {
                when (obtainedStyle.getIndex(i)) {
                    R.styleable.ArcLinearLayout_useMinPadding -> if (!obtainedStyle.getBoolean(
                            R.styleable.ArcLinearLayout_useMinPadding,
                            true
                        )
                    ) {
                        minimumPadding = 0
                    }
                    R.styleable.ArcLinearLayout_itemsOffset -> itemsOffset =
                        obtainedStyle.getDimension(R.styleable.ArcLinearLayout_itemsOffset, 0f)
                }
            }
            obtainedStyle.recycle()
        }

        setPadding(
            minimumPadding + paddingLeft,
            paddingTop,
            minimumPadding + paddingRight,
            paddingBottom
        )
    }

    fun getWidthOfTheVisibleCircle(radius: Float, strokeWidth: Int): Int {
        radiusPow2 = radius.toDouble().pow(2.0).toFloat()

        //The Pythagorean is used in circle to calculate the width of the visible circle
        return (2 * sqrt(2 * (radius * strokeWidth) - strokeWidth * strokeWidth.toDouble())).toInt()
    }

    fun headsUp() {
        if (width != 0) {
            val yPow2 = (containerHeight - radius.toDouble()).pow(2.0).toFloat()
            var x = (-(sqrt(
                abs(radiusPow2 - yPow2).toDouble()
            ) - containerWidth / 2)).toFloat() //this is the start position of the circle
            if (containerWidth > width) { //if  this layout is not as big as the width of the visible circle (which also mean no scrolling)
                x += containerWidth / 2 - x - width / 2
                val config = resources.configuration
                translationX = if (config.layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                    -x
                } else x
            } else {
                try {
                    (parent as ArcScrollView).scrollTo(
                        (width - containerWidth) / 2,
                        0
                    ) // trying to scroll to the center of the scrollView which is kinda working
                } catch (e: ClassCastException) {
                    throw RuntimeException("ArcLinearLayout must be parented by a ArcScrollView")
                }
            }
        }
    }

    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        super.onLayout(changed, l, t, r, b)
        headsUp()
        notifyKids(startOffset, arcElevation)
    }

    fun notifyKids(startOffset: Int, elevation: Float) {
        // called when scrolling to calculate new position
        this.startOffset = startOffset
        this.arcElevation = elevation
        val pos = IntArray(2)
        val count = childCount
        var currChild: View
        var y: Float
        radiusPow2 = radius.toDouble().pow(2.0).toFloat()

        for (i in 0 until count) {
            currChild = getChildAt(i)
            currChild.y = containerHeight.toFloat() //making the current view invisible
            currChild.getLocationOnScreen(pos)

            pos[0] -= startOffset

            if (pos[0] + currChild.width > 0 && currChild.width != 0) { //check also if smaller than width of the container
                pos[0] -= containerWidth / 2
                val xPow2 = (pos[0] + currChild.width / 2.toDouble()).pow(2.0).toFloat()
                y = abs(sqrt(abs(radiusPow2 - xPow2).toDouble()) - radius).toFloat()
                currChild.y = y * 1.2f + elevation + itemsOffset
            }
        }
    }

    fun setRadius(radius: Float) {
        this.radius = radius
    }

    fun setContainerHeight(containerHeight: Int) {
        this.containerHeight = containerHeight
    }

    fun setContainerWidth(containerWidth: Int) {
        this.containerWidth = containerWidth
    }

    companion object {
        private const val MIN_PADDING = 30
    }
}