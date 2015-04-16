package com.gracecode.android.gojuon.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gracecode.android.gojuon.R;

public class CharacterLayout extends RelativeLayout {
    public CharacterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void autoAdjustTextSize(ViewGroup rootView) {
        for (int i = 0, count = rootView.getChildCount(); i < count; i++) {
            View view = rootView.getChildAt(i);
            if (view instanceof ViewGroup) {
                autoAdjustTextSize((ViewGroup) view);
                continue;
            }

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

        invalidate();
    }

    public void autoAdjustTextSize() {
        autoAdjustTextSize(this);
    }
}
