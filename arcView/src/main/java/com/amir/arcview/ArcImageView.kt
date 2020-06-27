package com.amir.arcview
//package com.amir.stickergram.arcList;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.os.Build;
//import android.support.annotation.RequiresApi;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.widget.ImageView;
//
//import com.amir.stickergram.R;
//import com.squareup.otto.Subscribe;
//
//public class ArcImageView extends ImageView {
//    float y = 0;
//    int[] pos;
//    private float radius;
//
//    public ArcImageView(Context context) {
//        super(context);
//        startUp(context, null);
//    }
//
//    public ArcImageView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        startUp(context, attrs);
//    }
//
//    public ArcImageView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        startUp(context, attrs);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public ArcImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        startUp(context, attrs);
//    }
//
//
//    private void startUp(Context context, AttributeSet attrs) {
////        try {
////            EventBus.getBus().unregister(this);
////        } catch (IllegalArgumentException e) {
////            Log.e(getClass().getSimpleName(), "exception");
////        }
////        EventBus.getBus().register(this);
//        if (attrs != null) {
//            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ArcImageView);
//            final int N = a.getIndexCount();
//            for (int i = 0; i < N; ++i) {
//                int attr = a.getIndex(i);
//                switch (attr) {
//                    case R.styleable.ArcImageView_radius_inner:
//                        radius = a.getDimension(R.styleable.ArcImageView_radius_inner, 111);
//                        break;
////                case R.styleable.ArcScrollView_level:
////                    int level = a.getInteger(R.styleable.ArcScrollView_level, 0);
////                    event.setLevel(level);
////                    break;
//                }
//            }
//            a.recycle();
//        }
//        pos = new int[2];
//    }
//
//    @Override
//    public void onWindowFocusChanged(boolean hasWindowFocus) {
//        super.onWindowFocusChanged(hasWindowFocus);
//        getLocationOnScreen(pos);
//        Log.e(getClass().getSimpleName(), "radius: " + radius);
//        if (pos[0] + getWidth() > 0) { //smaller than width of the screen
//            pos[0] -= ArcScrollView.screenWidth / 2;
//            float xPow2 = (float) Math.pow(pos[0] + getWidth() / 2, 2);
//            float radiusPow2 = radius * radius;
//            y = (float) Math.sqrt(Math.abs(radiusPow2 - xPow2)) - radius;
////            setTranslationY(Math.abs(y) - y * .1f + 5);
//            setY(Math.abs(y) - y * .1f + 5);
////            Log.e(getClass().getSimpleName(), "y: " + y);
////
////            float angle = (float) (Math.atan2(pos[0] + ArcScrollView.screenWidth / 2, y) * (180 / Math.PI));
////            Log.e(getClass().getSimpleName(), "angle is: " + angle);
////            setRotation(angle);
////            setRotation(180);
//        }
//
////        Log.e(getClass().getSimpleName(), "top: " + getTop());
//
//    }
//
//
////    @Subscribe
////    public void scrollEvent(ScrollEvent ev) {
////        getLocationOnScreen(pos);
//////        Log.e(getClass().getSimpleName(), "eventRecieved");
////        if (ev.getUnregistred()) {
////            EventBus.getBus().unregister(this);
//////            Log.e(getClass().getSimpleName(), "got unregistered");
////            return;
////        }
////        if (pos[0] + getWidth() > 0) { //smaller than width of the screen
////            pos[0] -= ArcScrollView.screenWidth / 2;
////            float xPow2 = (float) Math.pow(pos[0] + getWidth() / 2, 2);
////            float radiusPow2 = ev.getRadius() * ev.getRadius();
////            y = (float) Math.sqrt(Math.abs(radiusPow2 - xPow2)) - ev.getRadius();
//////            setTranslationY(Math.abs(y) - y * .1f + 5);
////            setY(Math.abs(y) - y * .1f + 5);
//////            Log.e(getClass().getSimpleName(), "y: " + y);
//////
//////            float angle = (float) (Math.atan2(pos[0] + ArcScrollView.screenWidth / 2, y) * (180 / Math.PI));
//////            Log.e(getClass().getSimpleName(), "angle is: " + angle);
//////            setRotation(angle);
//////            setRotation(180);
////        }
////    }
//}
