package com.amir.arcview

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import kotlin.math.pow
import kotlin.math.sqrt

class ArcLinearLayout : LinearLayout {
    var radiusPow2 = 0f
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        starUp(context, attrs)
    }

    private fun starUp(
        context: Context,
        attrs: AttributeSet?
    ) {
        var minimumPadding =
            (MIN_PADDING * resources.displayMetrics.density).toInt()
        orientation = HORIZONTAL
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ArcLinearLayout)
            val N = a.indexCount
            for (i in 0 until N) {
                val attr = a.getIndex(i)
                when (attr) {
                    R.styleable.ArcLinearLayout_useMinPadding -> if (!a.getBoolean(
                            R.styleable.ArcLinearLayout_useMinPadding,
                            true
                        )
                    ) {
                        minimumPadding = 0
                    }
                    R.styleable.ArcLinearLayout_itemsOffset -> itemsOffset =
                        a.getDimension(R.styleable.ArcLinearLayout_itemsOffset, 0f)
                }
            }
            a.recycle()
        }
        //        Log.e(getClass().getSimpleName(), "minimumPadding: " + minimumPadding);
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
            val yPow2 =
                Math.pow(containerHeight - radius.toDouble(), 2.0).toFloat()
            var x = (-(Math.sqrt(
                Math.abs(radiusPow2 - yPow2).toDouble()
            ) - containerWidth / 2)).toFloat() //this is the start position of the circle
            if (containerWidth > width) { //if  this layout is not as big as the width of the visible circle (which also mean no scrolling)
                x += containerWidth / 2 - x - width / 2
                //                Log.e(getClass().getSimpleName(), "was smaller x: " + x + " containerWidth: " + containerWidth + " getWidth(): " + getWidth());
                val config = resources.configuration
                translationX = if (config.layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                    -x
                    //in Right To Left layout
                } else x
                //            setTranslationX(0);
//            Log.e(getClass().getSimpleName(), " x: " + x);
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
        //        Log.e(getClass().getSimpleName(), " onLayout was called: " + getWidth());
        headsUp()
        notifyKids(startOffset, arcElevation)
    }

    //    void notifyKids(float radius, int containerWidth, int containerHeight, int startOffset, float elevation) {
    fun notifyKids(startOffset: Int, elevation: Float) {
        this.startOffset = startOffset
        this.arcElevation = elevation
        val pos = IntArray(2)
        val count = childCount
        var currChild: View
        var y: Float
        radiusPow2 = Math.pow(radius.toDouble(), 2.0).toFloat()
//        var x: Float
        //        Log.e(getClass().getSimpleName(), "radius: " + radius + " containerHeight: " + containerHeight + " containerWidth: " + containerWidth + " startOffset: " + startOffset + " elevation: " + elevation);
//        if (count > 1) {
//            currChild = getChildAt(2);
//            Log.e(getClass().getSimpleName(), "count: " + count + " pos[0]: " + pos[0] + " currChild.getWidth(): " + currChild.getWidth());
//        }
        for (i in 0 until count) {
            currChild = getChildAt(i)
            currChild.y = containerHeight.toFloat() //making the current view invisible
            currChild.getLocationOnScreen(pos)
//            x = pos[0].toFloat()
            pos[0] -= startOffset
            //            if (pos[0] + currChild.getWidth() > 0 && pos[0] + currChild.getWidth() / 2 > startPos && pos[0] + currChild.getWidth() / 2 < endPos) { //check also if smaller than width of the container
            if (pos[0] + currChild.width > 0 && currChild.width != 0) { //check also if smaller than width of the container
                pos[0] -= containerWidth / 2
                val xPow2 = Math.pow(
                    pos[0] + currChild.width / 2.toDouble(),
                    2.0
                ).toFloat()
                y = Math.abs(
                    Math.sqrt(
                        Math.abs(radiusPow2 - xPow2).toDouble()
                    ) - radius
                ).toFloat()
                //                Log.e(getClass().getSimpleName(), " elevation: " + elevation + " offset: " + (y * 1.2f + elevation + itemsOffset));

//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
//                    currChild.setY(y * 1.2f);
//                else
                currChild.y = y * 1.2f + elevation + itemsOffset

//                currChild.setY(0);
//                Log.e(getClass().getSimpleName(), "------------posX: " + (pos[0] + currChild.getWidth() / 2) + " y: " + y);

//                float angle = (float) (Math.atan2(y, x) * (180 / Math.PI));
//                Log.e(getClass().getSimpleName(), "i: " + i + " angle is: " + (angle) + " posX: " + x + " posY:  " + y);
//                currChild.setRotation(angle);
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