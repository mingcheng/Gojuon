package com.gracecode.android.gojuon.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.gracecode.android.gojuon.common.Gojuon;
import com.gracecode.android.gojuon.service.PronounceService;

class BaseActivity extends FragmentActivity {
    public Gojuon mGojunon;
    protected Intent mServiceIntent;
    protected SharedPreferences mSharedPreferences;

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

        mGojunon.setLanguage();
        mServiceIntent = new Intent(this, PronounceService.class);
        mSharedPreferences = mGojunon.getSharedPreferences();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(mServiceIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceIntent != null) {
            stopService(mServiceIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(mSharedPreferences.getBoolean(Gojuon.KEY_AUTO_ROTATE, false) ?
                ActivityInfo.SCREEN_ORIENTATION_SENSOR :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
