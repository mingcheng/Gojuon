package com.gracecode.android.gojuon.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class StrokeView extends View {
    private static final long CLEAN_DELAY = 1000;
    private final Runnable mClearPathRunnable;
    private final Handler mHandler;
    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private OnStockListener listener;

    public static abstract interface OnStockListener {
        public abstract void onStockStart(View view);

        public abstract void OnStock(View view);

        public abstract void onStockFinish(View view);
    }

    public StrokeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(12f);
        mPaint.setColor(Color.RED);
        mPaint.setAlpha((int) (255 * .5));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);

        mClearPathRunnable = new Runnable() {
            @Override
            public void run() {
                if (mPath != null) {
                    mPath.reset();
                    if (listener != null) {
                        listener.onStockFinish(getView());
                    }
                    invalidate();
                }
            }
        };
        mHandler = new Handler();
    }

    public void setOnStockListener(OnStockListener listener) {
        this.listener = listener;
    }

    public StrokeView getView() {
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(eventX, eventY);
                mHandler.removeCallbacks(mClearPathRunnable);
                if (listener != null) {
                    listener.onStockStart(getView());
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(eventX, eventY);
                if (listener != null) {
                    listener.OnStock(getView());
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mHandler.postDelayed(mClearPathRunnable, CLEAN_DELAY);
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }
}
