package com.gracecode.android.gojuon.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.gracecode.android.gojuon.common.Gojuon;

class BaseActivity extends FragmentActivity {
    public Gojuon mGojunon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGojunon = Gojuon.getInstance();

        try {
            getActionBar().setIcon(android.R.color.transparent);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
