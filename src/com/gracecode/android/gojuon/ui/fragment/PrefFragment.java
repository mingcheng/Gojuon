package com.gracecode.android.gojuon.ui.fragment;

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
    private Gojuon mGojuon;
    private SharedPreferences mSharedPreferences;
    private PackageInfo mPackageInfo;

    public PrefFragment() {
        super();
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
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, final Preference preference) {

        try {
            switch (preference.getKey()) {
                case Gojuon.KEY_ABOUT:
                    break;

                case Gojuon.KEY_FEEDBACK:
                    mGojuon.sendEmail(getActivity(), getString(R.string.app_name));
                    break;

                case Gojuon.KEY_DONATE:
                    break;

                case Gojuon.KEY_CLEAR_RECORD:
                    mGojuon.getStageHelper().confirmToClearAllRecord(getActivity(), new Runnable() {
                        @Override
                        public void run() {
                            preference.setEnabled(false);
                        }
                    });
                    break;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
