package com.gracecode.android.gojuon.common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import com.gracecode.android.common.Logger;
import com.gracecode.android.common.helper.IntentHelper;
import com.gracecode.android.common.helper.UIHelper;
import com.gracecode.android.gojuon.R;

public class Gojuon extends Application {
    public static final String KEY_ABOUT = "key_about";
    public static final String KEY_FEEDBACK = "key_feedback";
    public static final String KEY_DONATE = "key_donate";
    public static final String KEY_AUTO_RESUME = "key_auto_resume";
    public static final String DEFAULT_RESUME_INDEX = "-1";

    private static Gojuon mInstance;

    public static Gojuon getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = Gojuon.this;
    }

    public SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(Gojuon.this);
    }


    public PackageInfo getPackageInfo() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void sendFeedbackEmail(Context context) {
        String subject = String.format(
                getString(R.string.feedback),
                getString(R.string.app_name), getPackageInfo().versionName);

        try {
            IntentHelper.sendMail(context, new String[]{
                    getString(R.string.email)
            }, subject, "");
        } catch (RuntimeException e) {
            Logger.e(e.getMessage());
            UIHelper.showShortToast(Gojuon.this, getString(R.string.send_email_faild));
        }
    }
}
