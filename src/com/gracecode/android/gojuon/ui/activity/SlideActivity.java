package com.gracecode.android.gojuon.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.helper.ViewHelper;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 15/4/22
 */
abstract class SlideActivity extends BaseActivity implements Animator.AnimatorListener {
    private static final int DURATION = 500;
    private STATUS mStatus;
    private AnimatorSet mAnimatorSet;

    private enum STATUS {
        OPEN, CLOSE
    }

    @Override
    public void onAnimationStart(Animator animator) {
        mStatus = isOpened() ? STATUS.CLOSE : STATUS.OPEN;
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        if (mStatusChangeListener != null) {
            if (isOpened()) {
                mStatusChangeListener.OnPanelOpened();
            } else {
                mStatusChangeListener.OnPanelClosed();
            }
        }
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }


    @InjectView(R.id.toolbar)
    Toolbar mToolbarView;

    @InjectView(R.id.container)
    View mContainerView;

    @InjectView(R.id.panel)
    View mPanelView;


    private OnPanelStatusChangeListener mStatusChangeListener;

    public interface OnPanelStatusChangeListener {
        abstract public void OnPanelOpened();

        abstract public void OnPanelClosed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbarView);
        init();
    }

    private void init() {
        mPanelView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public boolean isReady;

            @Override
            public void onGlobalLayout() {
                if (!isReady) {
                    mPanelView.setY(getScreenHeight());
                    isReady = true;
                }
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_slide;
    }

    public void setOnPanelStatusChangeListener(OnPanelStatusChangeListener listener) {
        mStatusChangeListener = listener;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isOpened()) {
                dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    protected boolean isOpened() {
        return mStatus == STATUS.OPEN;
    }

    protected void open() {
        animate(ValueAnimator.ofFloat(getScreenHeight(), 0f));
    }

    protected void dismiss() {
        animate(ValueAnimator.ofFloat(0f, getScreenHeight()));
    }


    private void animate(ValueAnimator animator) {
        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            mAnimatorSet.cancel();
        }

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mPanelView.setY((float) valueAnimator.getAnimatedValue());
            }
        });

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animator, isOpened() ?
                ViewHelper.getFadeOutAnimator(mPanelView, DURATION) : ViewHelper.getFadeInAnimator(mPanelView, DURATION));
        mAnimatorSet.setDuration(DURATION);
        mAnimatorSet.setInterpolator(new DecelerateInterpolator());
        mAnimatorSet.addListener(this);
        mAnimatorSet.start();
    }
}
