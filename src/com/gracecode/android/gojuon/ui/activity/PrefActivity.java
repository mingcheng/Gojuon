package com.gracecode.android.gojuon.ui.activity;

import android.os.Bundle;
import com.gracecode.android.gojuon.ui.fragment.PrefFragment;

public class PrefActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new PrefFragment(PrefActivity.this))
                .commit();
    }
}
