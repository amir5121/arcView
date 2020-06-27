package com.amir.arcview

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
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
import androidx.annotation.RequiresApi

class ArcScrollView : HorizontalScrollView {
    private var circleRadius = 0f
    var strokeWidth = 0
        private set
    private var arcWidth = 0
    private var startOffset = 0
    var isCenterHorizontal = true
        private set
    private var arcElevation = 0f

    //    private int level;
    private var arcHeight = 0
    private var useBestWidth = true
    private val isClipping = false
    private var prevChildBottom = 0
    private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null
    private var mBounds: Rect? = null
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        startUp(context, attrs)
    }

    private fun startUp(
        context: Context,
        attrs: AttributeSet?
    ) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ArcScrollView)
        val N = a.indexCount
        for (i in 0 until N) {
            val attr = a.getIndex(i)
            when (attr) {
                R.styleable.ArcScrollView_radius -> circleRadius =
                    a.getDimension(R.styleable.ArcScrollView_radius, 0f)
                R.styleable.ArcScrollView_stroke_width -> strokeWidth =
                    a.getDimension(R.styleable.ArcScrollView_stroke_width, 0f).toInt()
                R.styleable.ArcScrollView_findBestWidth -> useBestWidth =
                    a.getBoolean(R.styleable.ArcScrollView_findBestWidth, true)
            }
        }
        a.recycle()
        if (circleRadius == 0f || strokeWidth == 0) throw RuntimeException("You need to specify radius and stoke width of your ArcScrollView and they must not be zero")
        val wm =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
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
//        super.onMeasure(widthMeasureSpec, measureHeight(heightMeasureSpec));
        var visibleWidth = 0
        if (childCount == 1) visibleWidth =
            (getChildAt(0) as ArcLinearLayout).getWidthOfTheVisibleCircle(
                circleRadius,
                strokeWidth + prevChildBottom
            )
        //        Log.e(getClass().getSimpleName(), "width: " + MeasureSpec.getSize(widthMeasureSpec) + " visibleWidth: " + visibleWidth + " useBestWidth: " + useBestWidth);
        if (MeasureSpec.getSize(widthMeasureSpec) < visibleWidth || !useBestWidth || visibleWidth == 0) {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            super.onMeasure(widthMeasureSpec, measureHeight(heightMeasureSpec))
            //            Log.e(getClass().getSimpleName(), "-----didn't use bestWidth: " + MeasureSpec.getSize(widthMeasureSpec));
        } else {
//            super.onMeasure(View.MeasureSpec.makeMeasureSpec(visibleWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
            super.onMeasure(
                MeasureSpec.makeMeasureSpec(visibleWidth, MeasureSpec.EXACTLY),
                measureHeight(heightMeasureSpec)
            )
            //            Log.e(getClass().getSimpleName(), "used bestWidth: " + visibleWidth);
        }


//        Log.e(getClass().getSimpleName(), "-----widthMeasureSpec: " + widthMeasureSpec + " heightMeasureSpec: " + heightMeasureSpec);
//        setMeasuredDimension(widthMeasureSpec, 1000);
//        newWidth = ((ArcLinearLayout) getChildAt(0)).getWidthOfTheVisibleCircle(circleRadius, getStrokeWidth(), width, height);
//        notifyScroll();
    }

    private fun measureHeight(measureSpec: Int): Int {
        // taken from http://stackoverflow.com/questions/7420060/make-children-of-horizontalscrollview-as-big-as-the-screen
        // and from  http://stackoverflow.com/questions/14493732/what-are-widthmeasurespec-and-heightmeasurespec-in-android-custom-views
        // and of course a simple modification in one line of copied code :D
        var result = (prevChildBottom + strokeWidth)
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        //        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//        int screenWidth = display.getWidth();
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
//            Log.e(getClass().getSimpleName(), "EXACTLY");
            result = specSize
        }
        //        else {
//            // Measure the view
//            if (specMode == MeasureSpec.AT_MOST) {
//                // Respect AT_MOST value if that was what is called for by measureSpec
////                Log.e(getClass().getSimpleName(), "AT_MOST");
////                result = Math.min(result, specSize);
//            }
//        }
////        Log.d(getClass().getSimpleName(), "newHeight: " + result + " prevChildBottom: " + prevChildBottom + " specSize: " + specSize);
//        Log.d(getClass().getSimpleName(), "newHeight: " + result + " prevChildBottom: " + (prevChildBottom + strokeWidth) + " specSize: " + specSize);

//        return result;
//        return View.MeasureSpec.makeMeasureSpec((int) strokeWidth, MeasureSpec.EXACTLY);
        return MeasureSpec.makeMeasureSpec(result, MeasureSpec.EXACTLY)
        //        return measureSpec;
    }

    //    @Override
    //    public void addView(View child, int width, int height) {
    //        super.addView(child, width, height);
    //        notify();
    //        notifyScroll();
    //    }
    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)

//        Log.e(getClass().getSimpleName(), "onWindowFocusChanged");
        if (arcWidth == 0) {
            arcWidth = getWidth()
            arcElevation = 0f
            startOffset = left
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) arcElevation = getElevation()
            background = SemiCircleDrawable(
                getBackgroundColor(this),
                SemiCircleDrawable.Direction.BOTTOM,
                circleRadius,
                arcWidth,
                arcElevation
            )
        }
        if (arcHeight == 0) arcHeight = getHeight()
        if (childCount == 1 && arcHeight != 0 && arcWidth != 0) {
            setMeasurements()
            (getChildAt(0) as ArcLinearLayout).headsUp()
            notifyScroll()
        }
        //        if (centerHorizontal) {
////            setLeft((int) (screenWidth / 2 - width / 2));
////            setLeft(((VerticalArcContainer) getParent()).getWidth() / 2 - width / 2);
//            Log.e(getClass().getSimpleName(), "width: " + width + " getWidth(): " + getWidth() + " height: " + height + " getHeight():" + getHeight() + " leftWasSet: " + getLeft());
//        }

//        Log.e(getClass().getSimpleName(), " mColor: " + mColor);

//        if (mColor != null)
//            setBackground(new SemiCircleDrawable(Color.parseColor(mColor), SemiCircleDrawable.Direction.BOTTOM, circleRadius, width, elevation));
//        else
//            setBackground(new SemiCircleDrawable(Color.BLUE, SemiCircleDrawable.Direction.BOTTOM, circleRadius, width, elevation));
//            setBackground(new SemiCircleDrawable(getBackgroundColor(this), SemiCircleDrawable.Direction.BOTTOM, circleRadius, width, elevation));
    }

//    fun initIfNeeded() {
//        if (mBitmap == null) {
//            mBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
//            mCanvas = Canvas(mBitmap)
//            mBounds = Rect()
//        }
//    }

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
        //        Log.e(getClass().getSimpleName(), "onScrollChanged");
    }

    private fun notifyScroll() {
        try {
//            Log.e(getClass().getSimpleName(), "width: " + width + " getWidth(): " + getWidth() + " height: " + height + " getHeight():" + getHeight());
//            ((ArcLinearLayout) getChildAt(0)).notifyKids(circleRadius, width, height, startOffset, elevation);
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

    //    public int getNewWidth(int prevChildBottom) {
    //    public int getNewWidth() {
    ////        this.prevChildBottom = prevChildBottom;
    //        if (useBestWidth) {
    ////            int visibleWidth = ((ArcLinearLayout) getChildAt(0)).getWidthOfTheVisibleCircle(circleRadius, getStrokeWidth() + prevChildBottom);
    //            int visibleWidth = ((ArcLinearLayout) getChildAt(0)).getWidthOfTheVisibleCircle(circleRadius, strokeWidth + prevChildBottom);
    //            Log.e(getClass().getSimpleName(), " radius: " + circleRadius + " prevChildBottom: " + prevChildBottom + " strokeWidth: " + strokeWidth);
    ////            int visibleWidth = ((ArcLinearLayout) getChildAt(0)).getWidthOfTheVisibleCircle(circleRadius, getStrokeWidth() );
    //            if (getWidth() != 0)
    //                isClipping = (getWidth() != 0 && visibleWidth > width);
    //
    ////            requestLayout();
    ////            Log.e(getClass().getSimpleName(), "isClipping: " + isClipping + " visibleWidth: " + visibleWidth + " getWidth(): " + getWidth() + " width: " + width);
    //            return visibleWidth;
    //        }
    //        return 0;
    //    }
    //    public int getLevel() {
    //        return level;
    //    }
    //
    //    public void setLevel(int level) {
    //        this.level = level;
    //    }
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
//            float yPow2 = (float) Math.pow(ArcScrollView.this.height, 2);
//            int radiusPow2 = (int) Math.pow(ArcScrollView.this.circleRadius, 2);
//            int x = (int) -(Math.sqrt(Math.abs(radiusPow2 - yPow2)) - ArcScrollView.this.width / 2); //this is the start position of the circle
//            int x = (int) -(Math.sqrt(Math.abs(radiusPow2 - yPow2)) - ArcScrollView.this.width / 2); //this is the start position of the circle
//            int y = (int) -(Math.sqrt(Math.abs(radiusPow2 - Math.pow(ArcScrollView.this.width / 2f, 2))) - ArcScrollView.this.circleRadius);

//            Log.e(getClass().getSimpleName(), "ArcScrollView height: " + ArcScrollView.this.height + " height: " + height + " ArcScrollView width: " + ArcScrollView.this.width + " width: " + width + " x: " + x + " y: " + y);


////            this is not totally correct  this will only work if the visible part of the circle is complete and not being cut off... weirdly the shadow will go on beyond the scope of the current view
//            //should use path..
//            if (!isClipping) {
            outline.setOval(
                (this@ArcScrollView.arcWidth / 2 - circleRadius).toInt(),
                0,
                (this@ArcScrollView.arcWidth / 2 + circleRadius).toInt(),
                (circleRadius * 2).toInt()
            )
            //            } else {

//                Path path = new Path();
//                path.moveTo(0, height);
//                path.lineTo(width, height);
//                int radiusPow2 = (int) (circleRadius * circleRadius);
//                int yRight = (int) Math.abs(Math.sqrt(Math.abs(radiusPow2 - (-width / 2))) - circleRadius);
//                int yLeft = (int) Math.abs(Math.sqrt(Math.abs(radiusPow2 - (width / 2))) - circleRadius);
//
//                path.lineTo(width, yLeft);
//                path.addOval(0, yRight, width, yLeft, Path.Direction.CCW);
//                path.lineTo(0, height);
//
//                outline.setConvexPath(path);
//        }
        }

    }

    companion object {
        private const val TAG = "ArcScrollView"
        var screenWidth = 0f
    }
}