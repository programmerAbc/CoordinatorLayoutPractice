package com.practice.coordinatorlayout;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

/**
 * Created by usera on 2017/10/19.
 */

public class MyNestedScrollViewBehavior extends CoordinatorLayout.Behavior<NestedScrollView> {
    private static final String TAG = MyNestedScrollViewBehavior.class.getSimpleName();
    private View dependency;
    private boolean isExpanded = false;
    private boolean isScrolling = false;
    private float nestedScrollDistance = 0;
    private Scroller scroller;
    private Handler handler;
    private ViewConfiguration viewConfiguration;
    private static final int MIN_VELOCITY = 300;

    public MyNestedScrollViewBehavior() {
    }

    public MyNestedScrollViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        handler = new Handler();
        viewConfiguration = ViewConfiguration.get(context);
    }


    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, NestedScrollView child, int layoutDirection) {
        child.layout(0, parent.findViewById(R.id.appBarLayout).getMeasuredHeight(), child.getMeasuredWidth(), parent.findViewById(R.id.appBarLayout).getMeasuredHeight() + child.getMeasuredHeight() - parent.findViewById(R.id.tabLayout).getMeasuredHeight());
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, NestedScrollView child, View dependency) {
        if (dependency.getId() == R.id.appBarLayout) {
            this.dependency = dependency;
            nestedScrollDistance = dependency.getResources().getDimension(R.dimen.banner_height);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, NestedScrollView child, View dependency) {
        child.setTranslationY(dependency.getTranslationY());
        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, NestedScrollView child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, NestedScrollView child, View directTargetChild, View target, int nestedScrollAxes) {
        scroller.abortAnimation();
        isScrolling = false;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, NestedScrollView child, View target, int dx, int dy, int[] consumed) {
        if (dy < 0) {
            return;
        }

        if (dependency.getTranslationY() > -nestedScrollDistance) {
            dependency.setTranslationY(Math.max(-nestedScrollDistance, dependency.getTranslationY() - dy));
            consumed[1] = dy;
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, NestedScrollView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed >= 0) {
            return;
        }

        if (dependency.getTranslationY() < 0) {
            dependency.setTranslationY(Math.min(0, dependency.getTranslationY() - dyUnconsumed));
        }
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, NestedScrollView child, View target, float velocityX, float velocityY) {
        if (velocityY > 0) {//向上划
            if (dependency.getTranslationY() > -nestedScrollDistance) {
                if (Math.abs(velocityY) > MIN_VELOCITY) {
                    animateTranslateTo(-nestedScrollDistance, 300);
                }
                return true;
            } else {
                return false;
            }

        } else if (velocityY < 0) {//向下划
            if (dependency.getTranslationY() < 0 && !ViewCompat.canScrollVertically(target, -1)) {
                if (Math.abs(velocityY) > MIN_VELOCITY) {
                    animateTranslateTo(0, 300);
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, NestedScrollView child, View target) {
        if (!isScrolling) {
            if (-dependency.getTranslationY() > nestedScrollDistance / 2) {
                animateTranslateTo(-nestedScrollDistance, 1000);
            } else {
                animateTranslateTo(0, 1000);
            }
        }
    }

    public void animateTranslateTo(float posotion, int duration) {
        scroller.startScroll(0, (int) dependency.getTranslationY(), 0, (int) (posotion - dependency.getTranslationY()), duration);
        handler.post(flingRunnable);
        isScrolling = true;
    }


    private Runnable flingRunnable = new Runnable() {
        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                dependency.setTranslationY(scroller.getCurrY());
                handler.post(this);
            } else {
                isScrolling = false;
            }
        }
    };

}
