package com.gracecode.android.gojuon.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.GridView;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.adapter.CharactersFragmentAdapter;
import com.gracecode.android.gojuon.service.PronounceService;
import com.viewpagerindicator.TitlePageIndicator;

public class MainActivity extends FragmentActivity {
    private TextToSpeech mTextToSpeech;
    private GridView mGridView;
    private Intent mServiceIntent;
    private ViewPager mPager;
    private TitlePageIndicator mIndicator;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mServiceIntent = new Intent(this, PronounceService.class);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new CharactersFragmentAdapter(this, getSupportFragmentManager()));

        mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        getActionBar().setIcon(android.R.color.transparent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(mServiceIntent);
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
}
