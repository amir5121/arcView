package com.amir.arcview

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.view.animation.BounceInterpolator
import android.widget.HorizontalScrollView


class ArcScrollView : HorizontalScrollView {
    private var circleRadius = 0f
    var strokeWidth = 0
    private var arcWidth = 0
    private var startOffset = 0
    var isCenterHorizontal = true
    private var arcElevation = 0f
    private var arcHeight = 0
    private var useBestWidth = true
    private var prevChildBottom = 0
    private var animationInProgress = false

    constructor(context: Context) : super(context) {
        startUp(context, null)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        startUp(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        startUp(context, attrs)
    }

    private fun startUp(
        context: Context,
        attrs: AttributeSet?
    ) {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            outlineProvider = object : ViewOutlineProvider() {
//                override fun getOutline(view: View, outline: Outline) {
//                    // Or read size directly from the view's width/height
//                    val size = resources.getDimensionPixelSize(R.dimen.fab_size)
//                    outline.setOval(0, 0, size, size)
//                }
//            }
//        }
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ArcScrollView)
        for (i in 0 until styledAttributes.indexCount) {
            when (styledAttributes.getIndex(i)) {
                R.styleable.ArcScrollView_radius -> circleRadius =
                    styledAttributes.getDimension(R.styleable.ArcScrollView_radius, 0f)
                R.styleable.ArcScrollView_stroke_width -> strokeWidth =
                    styledAttributes.getDimension(R.styleable.ArcScrollView_stroke_width, 0f)
                        .toInt()
                R.styleable.ArcScrollView_findBestWidth -> useBestWidth =
                    styledAttributes.getBoolean(R.styleable.ArcScrollView_findBestWidth, true)
            }
        }
        styledAttributes.recycle()
        if (circleRadius == 0f || strokeWidth == 0) throw RuntimeException("You need to specify radius and stoke width of your ArcScrollView and they must not be zero")
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x.toFloat()
        isHorizontalScrollBarEnabled = false
        for (i in 0 until attrs!!.attributeCount) {
            if (attrs.getAttributeName(i) == "layout_centerHorizontal") {
                isCenterHorizontal = attrs.getAttributeBooleanValue(i, true)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var visibleWidth = 0
        if (childCount == 1) visibleWidth =
            (getChildAt(0) as ArcLinearLayout).getWidthOfTheVisibleCircle(
                circleRadius,
                strokeWidth + prevChildBottom
            )

        if (MeasureSpec.getSize(widthMeasureSpec) < visibleWidth || !useBestWidth || visibleWidth == 0) {
            super.onMeasure(widthMeasureSpec, measureHeight(heightMeasureSpec))
        } else {
            super.onMeasure(
                MeasureSpec.makeMeasureSpec(visibleWidth, MeasureSpec.EXACTLY),
                measureHeight(heightMeasureSpec)
            )

        }

    }

    private fun measureHeight(measureSpec: Int): Int {
        Log.wtf(TAG, "measureHeight: $measureSpec")
        // taken from http://stackoverflow.com/questions/7420060/make-children-of-horizontalscrollview-as-big-as-the-screen
        // and from  http://stackoverflow.com/questions/14493732/what-are-widthmeasurespec-and-heightmeasurespec-in-android-custom-views
        // and of course a simple modification in one line of copied code :D
        var result = (prevChildBottom + strokeWidth)
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        }
        return MeasureSpec.makeMeasureSpec(result, MeasureSpec.EXACTLY)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)

        if (arcWidth == 0) {
            arcWidth = width
            arcElevation = 0f
            startOffset = left
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) arcElevation = elevation
            background = SemiCircleDrawable(
                getBackgroundColor(this),
                SemiCircleDrawable.Direction.BOTTOM,
                circleRadius,
                arcWidth,
                arcElevation
            )
        }
        if (arcHeight == 0) arcHeight = height
        if (childCount == 1 && arcHeight != 0 && arcWidth != 0) {
            setMeasurements()
            (getChildAt(0) as ArcLinearLayout).headsUp()
            notifyScroll()
        }

    }

    private fun getBackgroundColor(view: View): Int {
        //http://stackoverflow.com/questions/8089054/get-the-background-color-of-a-button-in-android
        // The actual color, not the id.
        var color = Color.BLACK
        if (view.background is ColorDrawable) {
            color = (view.background as ColorDrawable).color
        }
        return color
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> if (Math.pow(
                    ev.x - arcWidth / 2.toDouble(),
                    2.0
                ) + Math.pow(
                    ev.y - circleRadius.toDouble(),
                    2.0
                ) >= circleRadius * circleRadius || !isEnabled
            ) {
                return false
            }
        }
        return super.onTouchEvent(ev)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        notifyScroll()
    }

    private fun notifyScroll() {
        try {
            (getChildAt(0) as ArcLinearLayout).notifyKids(startOffset, arcElevation)
        } catch (e: ClassCastException) {
            throw RuntimeException("ArcScrollView can only contain ArcLinearLayout")
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outlineProvider = CustomOutline()
        }
    }

    fun setPrevChildBottom(prevChildBottom: Int) {
        this.prevChildBottom = prevChildBottom
    }

    fun swapView(view: ArcLinearLayout?) {
        if (!animationInProgress) Log.e(TAG, "swapView: ")
        when {
            view == null -> { //in case you want to hide the view
                animate()
                    .alpha(0f)
                    .translationY((strokeWidth + prevChildBottom).toFloat())
                    .setDuration(500)
                    .setInterpolator(BounceInterpolator())
                    .setListener(object : AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {
                            animationInProgress = true
                        }

                        override fun onAnimationEnd(animator: Animator) {
                            visibility = View.GONE
                            animationInProgress = false
                        }

                        override fun onAnimationCancel(animator: Animator) {}
                        override fun onAnimationRepeat(animator: Animator) {}
                    })
                    .start()
            }
            visibility != View.GONE -> {
                animate()
                    .alpha(0f)
                    .translationY((strokeWidth + prevChildBottom).toFloat())
                    .setDuration(700)
                    .setInterpolator(BounceInterpolator())
                    .setListener(object : AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {
                            animationInProgress = true
                        }

                        override fun onAnimationEnd(animator: Animator) {
                            bringBackUp(view)
                            animationInProgress = false
                        }

                        override fun onAnimationCancel(animator: Animator) {}
                        override fun onAnimationRepeat(animator: Animator) {}
                    })
                    .start()
            }
            else -> { //if it is already hidden make it appear
                translationY = (strokeWidth + prevChildBottom).toFloat()
                visibility = View.VISIBLE
                bringBackUp(view)
            }
        }
    }

    private fun bringBackUp(view: ArcLinearLayout) {
        removeView(getChildAt(0))
        addView(view, 0)
        setMeasurements()
        animate().alpha(1f).translationY(0f).setDuration(300)
            .setListener(object : AnimatorListener {
                override fun onAnimationStart(animator: Animator) {
                    animationInProgress = true
                }

                override fun onAnimationEnd(animator: Animator) {
                    animationInProgress = false
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            }).setInterpolator(BounceInterpolator()).start()
        notifyScroll()
    }

    private fun setMeasurements() {
        val child = getChildAt(0) as ArcLinearLayout
        child.setRadius(circleRadius)
        child.setContainerHeight(arcHeight)
        child.setContainerWidth(arcWidth)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private inner class CustomOutline internal constructor() :
        ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {

            outline.setOval(
                (this@ArcScrollView.arcWidth / 2 - circleRadius).toInt(),
                0,
                (this@ArcScrollView.arcWidth / 2 + circleRadius).toInt(),
                (circleRadius * 2).toInt()
            )
        }

    }

    companion object {
        private const val TAG = "ArcScrollView"
        var screenWidth = 0f
    }
}