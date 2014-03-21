package com.gracecode.android.gojuon.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getActionBar().setIcon(android.R.color.transparent);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
