package com.gracecode.android.gojuon.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.common.Gojuon;

abstract class BaseActivity extends ActionBarActivity {
    public Gojuon mGojunon;
    protected Intent mServiceIntent;
    protected SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentViewId());
        mGojunon = Gojuon.getInstance();
//        if (mGojunon.isMeizuDevice()) {
//            getWindow().setUiOptions(ActivityInfo.UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW);
//        }

        mGojunon.setLanguage();
        mSharedPreferences = mGojunon.getSharedPreferences();
    }

    protected abstract int getContentViewId();

    @Override
    protected void onStart() {
        super.onStart();
        try {
            getSupportActionBar().setIcon(android.R.color.transparent);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_deep));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(mSharedPreferences.getBoolean(Gojuon.KEY_AUTO_ROTATE, false) ?
                ActivityInfo.SCREEN_ORIENTATION_SENSOR :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }
}
