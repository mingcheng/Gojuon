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

import java.util.Timer;

public class StrokeView extends View {
    private final Runnable mClearPathRunnable;
    private final Handler mHandler;
    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private Timer mTimer;

    public StrokeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(12f);
        mPaint.setColor(Color.RED);
        mPaint.setAlpha((int) (255 * .5));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);


        mClearPathRunnable = new Runnable() {
            @Override
            public void run() {
                if (mPath != null) {
                    mPath.reset();
                    invalidate();
                }
            }
        };
        mHandler = new Handler();
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
                return true;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mHandler.postDelayed(mClearPathRunnable, 1000);
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }
}
