package com.gracecode.android.gojuon.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gracecode.android.gojuon.R;

public class CharacterLayout extends RelativeLayout {
    public CharacterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void autoAdjustTextSize() {
        for (int i = 0, count = getChildCount(); i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                switch (view.getId()) {
                    case R.id.hiragana:
                        ((TextView) view).setTextSize(getWidth() / 8);
                        break;
                    default:
                        ((TextView) view).setTextSize(getWidth() / 16);
                        break;
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }
}
