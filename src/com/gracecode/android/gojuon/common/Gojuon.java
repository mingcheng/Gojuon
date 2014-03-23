package com.gracecode.android.gojuon.common;

import android.content.Context;
import com.gracecode.android.common.CustemApplication;
import com.gracecode.android.common.Logger;
import com.gracecode.android.common.helper.IntentHelper;
import com.gracecode.android.common.helper.UIHelper;
import com.gracecode.android.gojuon.R;
import com.xiaomi.market.sdk.XiaomiUpdateAgent;

public class Gojuon extends CustemApplication {
    public static final String KEY_ABOUT = "key_about";
    public static final String KEY_FEEDBACK = "key_feedback";
    public static final String KEY_DONATE = "key_donate";
    public static final String KEY_AUTO_RESUME = "key_auto_resume";
    public static final String KEY_KEEP_SCREEN = "key_screen_on";
    public static final String KEY_KATAKANA_FIRST = "key_katakana_first";

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

    public void checkUpdate() {
        XiaomiUpdateAgent.setCheckUpdateOnlyWifi(true);
        XiaomiUpdateAgent.update(this);
    }

    public void sendFeedbackEmail(Context context) {
        String subject = String.format(
                getString(R.string.feedback_subject),
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
