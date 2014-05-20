package com.gracecode.android.gojuon.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.common.Gojuon;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 14-3-17
 */
public class PrefFragment extends PreferenceFragment {
    private final Context mContext;
    private final Gojuon mGojuon;
    private final SharedPreferences mSharedPreferences;
    private final PackageInfo mPackageInfo;

    public PrefFragment(Context context) {
        mContext = context;
        mGojuon = Gojuon.getInstance();

        mSharedPreferences = mGojuon.getSharedPreferences();
        mPackageInfo = mGojuon.getPackageInfo();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }

    @Override
    public void onStart() {
        super.onStart();
        markVersionNumber();
    }

    private void markVersionNumber() {
        Preference aboutPref = findPreference(Gojuon.KEY_ABOUT);
        if (aboutPref != null) {
            String versions = String.format(getString(R.string.about_summary), mPackageInfo.versionName,
                    mPackageInfo.versionCode);
            aboutPref.setSummary(versions);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        try {
            switch (preference.getKey()) {
                case Gojuon.KEY_ABOUT:
                    break;

                case Gojuon.KEY_FEEDBACK:
                    mGojuon.sendFeedbackEmail(mContext);
                    break;

                case Gojuon.KEY_DONATE:

                    break;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
