package com.gracecode.android.gojuon.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.adapter.CharactersFragmentAdapter;
import com.gracecode.android.gojuon.common.Gojuon;
import com.gracecode.android.gojuon.service.PronounceService;
import com.viewpagerindicator.TitlePageIndicator;

public class MainActivity extends BaseActivity {
    private Intent mServiceIntent;
    private ViewPager mViewPager;
    private TitlePageIndicator mIndicator;
    private CharactersFragmentAdapter mCharactersFragmentAdapter;
    private SharedPreferences mSharedPreferences;

    private static String KEY_AUTO_RESUME_SAVED = "key_auto_resume_saved";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mServiceIntent = new Intent(this, PronounceService.class);

        mViewPager = (ViewPager) findViewById(R.id.pager);

        mCharactersFragmentAdapter = new CharactersFragmentAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mCharactersFragmentAdapter);

        mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager);

        mSharedPreferences = mGojunon.getSharedPreferences();
        mGojunon.checkUpdate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(mServiceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIndicator.setCurrentItem(getSavedResumePage());

        if (mSharedPreferences.getBoolean(Gojuon.KEY_KEEP_SCREEN, true)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        setRequestedOrientation(mSharedPreferences.getBoolean(Gojuon.KEY_AUTO_ROTATE, false) ?
                ActivityInfo.SCREEN_ORIENTATION_SENSOR :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setResumePage();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private int getSavedResumePage() {
        String savedIndex = mSharedPreferences.getString(Gojuon.KEY_AUTO_RESUME, Gojuon.DEFAULT_RESUME_INDEX);
        int index = Integer.parseInt(savedIndex);
        if (index == Integer.parseInt(Gojuon.DEFAULT_RESUME_INDEX)) {
            return mSharedPreferences.getInt(KEY_AUTO_RESUME_SAVED, 0);
        }

        return index;
    }

    private void setResumePage() {
        String savedIndex = mSharedPreferences.getString(Gojuon.KEY_AUTO_RESUME, Gojuon.DEFAULT_RESUME_INDEX);
        int index = Integer.parseInt(savedIndex);
        if (index == Integer.parseInt(Gojuon.DEFAULT_RESUME_INDEX)) {
            mGojunon.putSharedPreferencesInt(KEY_AUTO_RESUME_SAVED, mViewPager.getCurrentItem());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceIntent != null) {
            stopService(mServiceIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_prefs:
                startActivity(new Intent(this, PrefActivity.class));
                break;
//            case R.id.action_about:
//                break;
//            case R.id.action_donate:
//                break;
            case R.id.action_feedback:
                mGojunon.sendFeedbackEmail(MainActivity.this);
                break;

//            case R.id.action_slideshow:
//                item.setEnabled(false);
//                item.getIcon().setAlpha(100);
//                mCharactersFragmentAdapter.startSlide(mViewPager.getCurrentItem());
//                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }
}
