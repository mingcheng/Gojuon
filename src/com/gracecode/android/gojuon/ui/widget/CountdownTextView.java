package com.gracecode.android.gojuon.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.gracecode.android.gojuon.helper.ViewHelper;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 15/4/16
 */
public class CountdownTextView extends TextView implements Animator.AnimatorListener {
    private static final int DURATION = 1500;

    private int mCountdownNumber;
    private AnimatorSet mAnimatorSet;
    private CountdownListener mListener;
    private String[] mCountdownCharacters;

    @Override
    public void onAnimationStart(Animator animator) {
        try {
            String character = mCountdownCharacters[mCountdownNumber - 1];
            setText(character);
        } catch (RuntimeException e) {
            setText(String.valueOf(mCountdownNumber));
        }

        if (mListener != null) {
            mListener.onRepeat(mCountdownNumber);
        }
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        if (--mCountdownNumber > 0) {
            startCountdown();
        } else {
            if (mListener != null) {
                mListener.onCountdownEnd();
            }
        }
    }

    @Override
    public void onAnimationCancel(Animator animator) {
        if (mListener != null) {
            mListener.onCancel();
        }
    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    public void setCountdownCharacters(String[] characters) {
        this.mCountdownCharacters = characters;
    }

    public interface CountdownListener {
        abstract public void onRepeat(int repeat);

        abstract public void onCountdownEnd();

        abstract public void onCancel();
    }

    public CountdownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCountdownListener(CountdownListener listener) {
        this.mListener = listener;
    }

    public void setCountdownNumber(int number) {
        this.mCountdownNumber = number;
    }

    public void startCountdown() {
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(
                ViewHelper.getFadeOutAnimator(this, DURATION),
                ViewHelper.getScaleAnimator(this, 1f, 2f, DURATION));
        mAnimatorSet.setDuration((long) (DURATION * .8));
        mAnimatorSet.addListener(this);
        mAnimatorSet.start();
    }

    public void stopCountdown() {
        mAnimatorSet.cancel();
    }
}
