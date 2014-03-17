package com.gracecode.android.gojuon.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.gracecode.android.gojuon.R;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 14-3-17
 */
public class PrefFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }
}
