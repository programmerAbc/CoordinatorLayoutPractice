package com.practice.coordinatorlayout;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by usera on 2018/3/8.
 */

public class TitleBarBehavior extends CoordinatorLayout.Behavior<View> {
    float maxScrollDistance;
    View dependencyView;

    public TitleBarBehavior() {
    }

    public TitleBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        Resources res = parent.getResources();
        maxScrollDistance = dependencyView.getMeasuredHeight() - res.getDimension(R.dimen.title_bar_height) - res.getDimension(R.dimen.tab_height);
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (dependency.getId() == R.id.appBarLayout) {
            dependencyView = dependency;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float scrollDistance = Math.abs(dependency.getTranslationY());
        child.setAlpha((float) Math.pow(scrollDistance/maxScrollDistance,2));
        return false;
    }
}
