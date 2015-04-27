package com.gracecode.android.gojuon.helper;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 15/4/16
 */
final public class ViewHelper {
    public static Animator getFadeOutAnimator(View view, int duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        animator.setDuration((long) (duration));
        return animator;
    }

    public static Animator getFadeInAnimator(View view, int duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        animator.setDuration((long) (duration));
        return animator;
    }

    public static Animator getScaleAnimator(final View view, float from, float to, int duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float i = (float) valueAnimator.getAnimatedValue();
                view.setScaleX(i);
                view.setScaleY(i);
            }
        });

        return animator;
    }

    public static int getActivityWidth(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            return view.getWidth();
        } else {
            return 0;
        }
    }
}
