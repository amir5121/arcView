package com.amir.arcview

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.content.Context
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.annotation.RequiresApi
import java.util.*

class VerticalArcContainer : ViewGroup {
    var prevChildBottom = 0 //kinda doesn't need to be global
    var childrenStrokes: MutableList<Int>? = null
    private var totalHeight = 0f
    private var animationBuffer = 0
    private var animationCallBack: ArcCallBack? = null

    constructor(context: Context) : super(context) {
        startUp(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        startUp(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        startUp(context)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        startUp(context)
    }

    private fun startUp(context: Context) {
//        mScrollViewChildren = new HashMap<>();
        childrenStrokes = ArrayList()
        try {
            animationCallBack = context as ArcCallBack
        } catch (e: ClassCastException) {
            //INTENTIONALLY EMPTY
        }
    }

    fun hideChild(id: Int) {
        collapseChild(indexOfChild(findViewById(id)))
    }

    private fun collapseChild(i: Int) {
        val child = getChildAt(i) as ArcScrollView
        val size = childrenStrokes!!.size
        if (i in 0 until size) child.animate() //                        .translationY(childrenStrokes.get(i) + child.getStrokeWidth())
            //                        .translationY(prevChildBottom)
            .translationY(totalHeight)
            .setDuration(500)
            .setListener(object : AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    if (i == 0) animationCallBack!!.itAllKnockedOut()
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            }) //                        .alpha(.2f)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    fun showChild(id: Int) {
        expandChild(indexOfChild(findViewById(id)))
    }

    fun expandChild(childPosition: Int) {
        val child = getChildAt(childPosition)
        if (child != null) {
            if (childPosition >= 0 && childPosition < childrenStrokes!!.size) child.animate()
                .translationY(0f)
                .setDuration(400)
                .setListener(object : AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        if (childPosition == childrenStrokes!!.size - 1) animationCallBack!!.itAllKnockedIn()
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                }) //                        .alpha(1)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }
    }

    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        var rightOffset: Int
        val count = childCount
        //        childrenStrokes.clear();
//        mScrollViewChildren.clear();

//        Log.e(getClass().getSimpleName(), " onLayout was called");
        prevChildBottom = 0
        for (i in 0 until count) {
            val child = getChildAt(i)
            //            rightOffset = child.getLeft();
            rightOffset = child.left
            if (child is ArcScrollView) {
                //                Log.e(getClass().getSimpleName(), " getWidth(): " + getWidth());
                if (child.isCenterHorizontal) rightOffset =
                    width / 2 - child.getMeasuredWidth() / 2
                //                Log.e(getClass().getSimpleName(), " rightOffset: " + rightOffset + " getWidth: " + getWidth() + " currArcScrollView.getWidth(): " + currArcScrollView.getWidth() + " child.getMeasuredWidth(): " + child.getMeasuredWidth());
//                Log.e(getClass().getSimpleName(), "count: " + count + " i: " + i);

//                int bestWidth = currArcScrollView.getNewWidth(childrenStrokes.get(count - i - 1));
//                int bestWidth = currArcScrollView.getNewWidth(childrenStrokes.get(i));
//                int bestWidth = currArcScrollView.getNewWidth();
//                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) //FML
//                    bestWidth *= 2;
//                int bestWidth = currArcScrollView.getNewWidth(prevChildBottom);
//                Log.e(getClass().getSimpleName(), "newWidth: " + bestWidth + " i: " + i + " prevChildBottom: " + prevChildBottom + " measuredWidth: " + child.getMeasuredWidth());
//                Log.e(getClass().getSimpleName(), "i: " + i + " prevChildBottom: " + prevChildBottom + " measuredWidth: " + child.getMeasuredWidth() + " bottom: " + childrenStrokes.get(count - 1));
//                Log.e(getClass().getSimpleName(), "-----i: " + i + " used bestWidth: " + bestWidth + " measuredWidth: " + child.getMeasuredWidth() + " rightOffset: " + rightOffset);
//                if (bestWidth != 0 && child.getMeasuredWidth() > bestWidth) {
//                    child.layout(
//                            rightOffset,
//                            prevChildBottom,
////                            rightOffset + child.getMeasuredWidth(),
//                            rightOffset + bestWidth,
//                            prevChildBottom + child.getMeasuredHeight());
////                            (int) (prevChildBottom + currArcScrollView.getStrokeWidth()));
////                            childrenStrokes.get(count - 1));
//                } else {
                child.layout(
                    rightOffset,
                    prevChildBottom,
                    rightOffset + child.getMeasuredWidth(),  //                            rightOffset + 100,
                    //                            prevChildBottom + child.getMeasuredHeight());
                    //                            (int) (prevChildBottom + currArcScrollView.getStrokeWidth()));
                    //                            prevChildBottom + childrenStrokes.get(count - i - 1));
                    //                            childrenStrokes.get(count - 1));
                    totalHeight.toInt()
                )

//                }
                prevChildBottom += child.strokeWidth
                //                mScrollViewChildren.put(currArcScrollView.getLevel(), currArcScrollView);
//                Log.e(getClass().getSimpleName(), "i: " + i + " level: " + currArcScrollView.getLevel() + " visibility: " + currArcScrollView.getVisibility());
            } else {
                Log.e(
                    javaClass.simpleName,
                    " onLayout else was called ****MUST NOT HAPPEN***"
                )
                //                child.layout(rightOffset, prevChildBottom,
//                        rightOffset + child.getMeasuredWidth(),
//                        prevChildBottom + child.getMeasuredHeight());
//                prevChildBottom += child.getMeasuredHeight();
//                childrenStrokes.add(child.getMeasuredHeight());
            }
            //            childrenStrokes.add(prevChildBottom);
        }
        //        Log.e(getClass().getSimpleName(), "onLayout prevChildBottom: " + prevChildBottom);
    }

    fun knockout() {
        val count = childCount
        for (i in 0 until count) {
            val handler = Handler()
            animationBuffer++
            handler.postDelayed({
                animationBuffer--
                collapseChild(i)
            }, 75 * animationBuffer.toLong())
        }
    }

    fun knockIn() {
        val count = childCount
        for (i in count - 1 downTo 0) {
            animationBuffer++
            val handler = Handler()
            handler.postDelayed({
                animationBuffer--
                expandChild(i)
            }, 75 * animationBuffer.toLong())
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //taken from http://stackoverflow.com/questions/12266899/onmeasure-custom-view-explanation

        //todo: optimize... wtf you have written
        val count = childCount

//        int count = getChildCount();
        if (prevChildBottom == 0) {
//            prevChildBottom = 0;
//            int prevChildBottomReversed = 0;
            childrenStrokes!!.clear()
            //            for (int i = 0; i < count; i++) {
            for (i in count - 1 downTo 0) {
                val child = getChildAt(i)
                //                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                if (child is ArcScrollView) {
                    val currArcScrollView = child
                    currArcScrollView.setPrevChildBottom(prevChildBottom)
                    childrenStrokes!!.add(prevChildBottom)
                    prevChildBottom += currArcScrollView.strokeWidth
                    totalHeight += currArcScrollView.strokeWidth
                    //                Log.e(getClass().getSimpleName(), "prevChildBottom: " + prevChildBottom);
                } else {
                    throw RuntimeException("VerticalArcContainer can only contain ArcScrollView")
                }
            }
        }

//        for (int i = count - 1; i >= 0; i--) {
//            final View child = getChildAt(i);
////                measureChild(child, widthMeasureSpec, heightMeasureSpec);
//            if (child instanceof ArcScrollView) {
//                ArcScrollView currArcScrollView = ((ArcScrollView) child);
//                currArcScrollView.setPrevChildBottom(childrenStrokes.get(count - 1 - i));
//                Log.e(getClass().getSimpleName(), "i: " + i + " childrenStrokes.get(i): " + childrenStrokes.get(count - 1 - i));
//            }
//        }

//        Log.e(getClass().getSimpleName(), "onMeasure widthMeasureSpec: " + widthMeasureSpec + " heightMeasureSpec: " + heightMeasureSpec);
        val desiredHeight = prevChildBottom
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val width: Int
        val height: Int

        //Measure Width
        width = if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            widthSize
            //            Log.e(getClass().getSimpleName(), "width EXACTLY: " + width);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            Math.min(widthMeasureSpec, widthSize)
            //            Log.e(getClass().getSimpleName(), "width AT_MOST: " + width);
        } else {
            //Be whatever you want
            widthMeasureSpec
            //            Log.e(getClass().getSimpleName(), "width else: " + width);
        }

        //Measure Height
        height = if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            heightSize
            //            Log.e(getClass().getSimpleName(), "height EXACTLY: " + height);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
//            height = Math.min(desiredHeight, heightSize);
            Math.min(desiredHeight, heightSize)
            //            Log.e(getClass().getSimpleName(), "height AT_MOST: " + height);
        } else {
            //Be whatever you want
            desiredHeight
            //            Log.e(getClass().getSimpleName(), "height else: " + height);
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height)
        //        Log.e(getClass().getSimpleName(), "onMeasure height is: " + height + " width: " + width + " prevChildBottom: " + prevChildBottom + " widthMeasureSpec: " + widthMeasureSpec + " heightMeasureSpec: " + heightMeasureSpec);
        for (i in 0 until count) {
            val child = getChildAt(i)
            //            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            measureChild(
                child,
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            )
        }

//        invalidate();
//        requestLayout();
    }

    companion object {
        private const val TAG = "VerticalArcContainer"
    }
}