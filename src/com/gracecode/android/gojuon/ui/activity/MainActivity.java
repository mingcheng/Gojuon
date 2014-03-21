package com.gracecode.android.gojuon.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import com.gracecode.android.common.helper.IntentHelper;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.adapter.CharactersFragmentAdapter;
import com.gracecode.android.gojuon.service.PronounceService;
import com.viewpagerindicator.TitlePageIndicator;

public class MainActivity extends BaseActivity {
    private Intent mServiceIntent;
    private ViewPager mPager;
    private TitlePageIndicator mIndicator;
    private CharactersFragmentAdapter mCharactersFragmentAdapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mServiceIntent = new Intent(this, PronounceService.class);

        mPager = (ViewPager) findViewById(R.id.pager);

        mCharactersFragmentAdapter = new CharactersFragmentAdapter(this, getSupportFragmentManager());
        mPager.setAdapter(mCharactersFragmentAdapter);

        mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(mServiceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIndicator.setCurrentItem(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(mServiceIntent);
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
            case R.id.action_about:
                break;
            case R.id.action_donate:
                break;
            case R.id.action_feedback:
                IntentHelper.sendMail(this,
                        new String[]{getString(R.string.email)},
                        "Feedback for Gojuon 1.0", "");
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }
}
