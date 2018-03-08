package com.practice.coordinatorlayout;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by usera on 2017/10/19.
 */

public class ScrollHeaderBehavior extends CoordinatorLayout.Behavior<View> {
    private static final String TAG = ScrollHeaderBehavior.class.getSimpleName();
    private boolean isScrolling = false;
    private float nestedScrollDistance = 0;
    private Scroller scroller;
    private Handler handler;
    private static final int MIN_VELOCITY = 30;
    View header = null;


    public ScrollHeaderBehavior() {
    }

    public ScrollHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        handler = new Handler();
        nestedScrollDistance = context.getResources().getDimension(R.dimen.banner_height);
    }


    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        header = child;
        nestedScrollDistance = header.getMeasuredHeight() - child.getResources().getDimension(R.dimen.title_bar_height) - child.getResources().getDimension(R.dimen.tab_height);
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        scroller.abortAnimation();
        isScrolling = false;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (dy < 0) {
            return;
        }

        if (child.getTranslationY() > -nestedScrollDistance) {
            child.setTranslationY(Math.max(-nestedScrollDistance, child.getTranslationY() - dy));
            consumed[1] = dy;
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed >= 0) {
            return;
        }

        if (child.getTranslationY() < 0) {
            child.setTranslationY(Math.min(0, child.getTranslationY() - dyUnconsumed));
        }
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        if (velocityY > 0) {//向上划
            if (child.getTranslationY() > -nestedScrollDistance) {
                if (Math.abs(velocityY) > MIN_VELOCITY) {
                    animateTranslateTo(-nestedScrollDistance, 300);
                }
                return true;
            } else {
                return false;
            }

        }else{
            return false;
        }
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        if (!consumed) {
            if (velocityY < 0) {//向下划
                if (child.getTranslationY() < 0) {
                    if (Math.abs(velocityY) > MIN_VELOCITY) {
                        animateTranslateTo(0, 300);
                    }
                    return true;
                } else {
                    return false;
                }
            }else{
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        if (!isScrolling) {
            if (-child.getTranslationY() > nestedScrollDistance / 2) {
                animateTranslateTo(-nestedScrollDistance, 1000);
            } else {
                animateTranslateTo(0, 1000);
            }
        }
    }

    public void animateTranslateTo(float posotion, int duration) {
        scroller.startScroll(0, (int) header.getTranslationY(), 0, (int) (posotion - header.getTranslationY()), duration);
        handler.post(flingRunnable);
        isScrolling = true;
    }


    private Runnable flingRunnable = new Runnable() {
        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                header.setTranslationY(scroller.getCurrY());
                handler.post(this);
            } else {
                isScrolling = false;
            }
        }
    };

}
