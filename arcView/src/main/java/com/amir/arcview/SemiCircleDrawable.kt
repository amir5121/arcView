package com.amir.arcview

import android.graphics.*
import android.graphics.drawable.Drawable

class SemiCircleDrawable(
    private var color: Int,
    private val angle: Direction,
    circleRadius: Float,
    width: Int,
    elevation: Float
) : Drawable() {
    //http://stackoverflow.com/questions/15962745/draw-a-semicircle-in-the-background-of-a-view
    private val circleRadius: Float
    private val elevation: Float
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF: RectF
    private val width: Int

    enum class Direction {
        LEFT, RIGHT, TOP, BOTTOM
    }

    fun getColor(): Int {
        return color
    }

    /**
     * A 32bit color not a color resources.
     *
     * @param color
     */
    fun setColor(color: Int) {
        this.color = color
        paint.color = color
    }

    override fun draw(canvas: Canvas) {
        canvas.save()

//        canvas.drawCircle(BaseActivity.screenWidth / 2,
//                500 * BaseActivity.density,
//                500 * BaseActivity.density, paint);

//        canvas.drawCircle(ArcScrollView.screenWidth / 2,
        canvas.drawCircle(
            width / 2.toFloat(),
            circleRadius + elevation,
            circleRadius - elevation, paint
        )

//        Log.e(getClass().getSimpleName(), "circleRad: " + circleRadius);
//
//        Rect bounds = getBounds();
//
//        if (angle == Direction.LEFT || angle == Direction.RIGHT) {
//            canvas.scale(2, 1);
//            if (angle == Direction.RIGHT) {
//                canvas.translate(-(bounds.right / 2), 0);
//            }
//        } else {
//            canvas.scale(1, 2);
//            if (angle == Direction.BOTTOM) {
//                canvas.translate(0, -(bounds.bottom / 2));
//            }
//        }
//
//
//        rectF.set(bounds);
//
//        if (angle == Direction.LEFT)
//            canvas.drawArc(rectF, 90, 180, true, paint);
//        else if (angle == Direction.TOP)
//            canvas.drawArc(rectF, -180, 180, true, paint);
//        else if (angle == Direction.RIGHT)
//            canvas.drawArc(rectF, 270, 180, true, paint);
//        else if (angle == Direction.BOTTOM)
//            canvas.drawArc(rectF, 0, 180, true, paint);
    }

    override fun setAlpha(alpha: Int) {
        // Has no effect
    }

    override fun setColorFilter(cf: ColorFilter?) {
        // Has no effect
    }

    override fun getOpacity(): Int {
        // Not Implemented
        return PixelFormat.TRANSLUCENT
    }

    //    public SemiCircleDrawable() {
    //        this(Color.BLUE, Direction.TOP, 0);
    //    }
    init {
        paint.color = color
        paint.style = Paint.Style.FILL
        rectF = RectF()
        this.elevation = elevation
        this.width = width
        this.circleRadius = circleRadius
    }
}