package com.practice.coordinatorlayout;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by usera on 2017/10/12.
 */

public class MyCoordinatorLayoutBehavior extends CoordinatorLayout.Behavior<View> {

    public MyCoordinatorLayoutBehavior() {
    }

    public MyCoordinatorLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.setAlpha((float) (1 - Math.pow(dependency.getTop() / parent.getContext().getResources().getDimension(R.dimen.banner_height), 2)));
        return false;
    }
}
