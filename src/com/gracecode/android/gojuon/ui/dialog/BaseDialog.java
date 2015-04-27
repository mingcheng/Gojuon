package com.gracecode.android.gojuon.ui.dialog;


import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ViewGroup;
import android.view.Window;
import com.gracecode.android.common.helper.UIHelper;
import com.gracecode.android.gojuon.common.Gojuon;

abstract class BaseDialog extends DialogFragment {
    private static Typeface mCustomTypeface;
    protected final SharedPreferences mSharedPreferences;

    BaseDialog() {
        super();
        mSharedPreferences = Gojuon.getInstance().getSharedPreferences();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
//
//        if (mContext != null) {
//            mCustomTypeface = Typeface.createFromAsset(mContext.getAssets(), Gojuon.CUSTOM_FONT_NAME);
//        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mCustomTypeface != null) {
            UIHelper.setCustomTypeface((ViewGroup) getView(), mCustomTypeface);
        }
    }
}
