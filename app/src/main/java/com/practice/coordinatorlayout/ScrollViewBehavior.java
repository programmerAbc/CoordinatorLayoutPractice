package com.practice.coordinatorlayout;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by usera on 2018/3/8.
 */

public class ScrollViewBehavior extends CoordinatorLayout.Behavior<View> {
    View dependencyView = null;

    public ScrollViewBehavior() {
    }

    public ScrollViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        CoordinatorLayout.LayoutParams layoutParams= (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        layoutParams.topMargin=dependencyView.getMeasuredHeight();
        layoutParams.height=(int)(parent.getMeasuredHeight()-parent.getResources().getDimension(R.dimen.tab_height)-parent.getResources().getDimension(R.dimen.title_bar_height));
        return false;
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
        child.setTranslationY(dependency.getTranslationY());
        return true;
    }
}
