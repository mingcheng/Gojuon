package com.gracecode.android.gojuon.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.gracecode.android.gojuon.ui.fragment.PrefFragment;

public class PrefActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new PrefFragment())
                .commit();
    }
}
