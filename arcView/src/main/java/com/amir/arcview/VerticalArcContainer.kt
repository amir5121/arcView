package com.amir.arcview

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import java.util.*

class VerticalArcContainer : ViewGroup {
    var prevChildBottom = 0 //kinda doesn't need to be global
    var isKnockedIn: Boolean = true
    lateinit var childrenStrokes: MutableList<Int>
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

    private fun startUp(context: Context) {
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
        val size = childrenStrokes.size
        if (i in 0 until size) child.animate() //                        .translationY(childrenStrokes.get(i) + child.getStrokeWidth())
            //                        .translationY(prevChildBottom)
            .translationY(totalHeight)
            .setDuration(500)
            .setListener(object : AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    if (i == 0) {
                        animationCallBack?.itAllKnockedOut()
                        isKnockedIn = false
                    }

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
            if (childPosition >= 0 && childPosition < childrenStrokes.size) child.animate()
                .translationY(0f)
                .setDuration(400)
                .setListener(object : AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        if (childPosition == childrenStrokes.size - 1) {
                            animationCallBack?.itAllKnockedIn()
                            isKnockedIn = true
                        }
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

        prevChildBottom = 0
        for (i in 0 until count) {
            val child = getChildAt(i)

            rightOffset = child.left
            if (child is ArcScrollView) {
                if (child.isCenterHorizontal) rightOffset =
                    width / 2 - child.getMeasuredWidth() / 2
                child.layout(
                    rightOffset,
                    prevChildBottom,
                    rightOffset + child.getMeasuredWidth(),  //                            rightOffset + 100,
                    totalHeight.toInt()
                )

                prevChildBottom += child.strokeWidth
            } else {
                Log.e(
                    javaClass.simpleName,
                    " onLayout else was called ****MUST NOT HAPPEN***"
                )
            }
        }
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

        if (prevChildBottom == 0) {
            childrenStrokes.clear()
            for (i in count - 1 downTo 0) {
                val child = getChildAt(i)
                if (child is ArcScrollView) {
                    child.setPrevChildBottom(prevChildBottom)
                    childrenStrokes.add(prevChildBottom)
                    prevChildBottom += child.strokeWidth
                    totalHeight += child.strokeWidth
                } else {
                    throw RuntimeException("VerticalArcContainer can only contain ArcScrollView")
                }
            }
        }

        val desiredHeight = prevChildBottom
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val width: Int
        val height: Int

        //Measure Width
        width = when (widthMode) {
            MeasureSpec.EXACTLY -> {
                //Must be this size
                widthSize
            }
            MeasureSpec.AT_MOST -> {
                //Can't be bigger than...
                widthMeasureSpec.coerceAtMost(widthSize)
            }
            else -> {
                //Be whatever you want
                widthMeasureSpec
            }
        }

        //Measure Height
        height = when (heightMode) {
            MeasureSpec.EXACTLY -> {
                //Must be this size
                heightSize
            }
            MeasureSpec.AT_MOST -> {
                //Can't be bigger than...
                desiredHeight.coerceAtMost(heightSize)
            }
            else -> {
                //Be whatever you want
                desiredHeight
            }
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height)
        for (i in 0 until count) {
            val child = getChildAt(i)
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