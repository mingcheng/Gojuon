package com.gracecode.android.gojuon.ui.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.gracecode.android.gojuon.common.Gojuon;

class BaseActivity extends FragmentActivity {
    public Gojuon mGojunon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGojunon = Gojuon.getInstance();
        if (mGojunon.isMeizuDevice()) {
            getWindow().setUiOptions(ActivityInfo.UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW);
        }

        try {
            getActionBar().setIcon(android.R.color.transparent);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
