package com.gracecode.android.gojuon.common;

import android.content.res.Configuration;
import com.gracecode.android.common.CustomApplication;
import com.xiaomi.market.sdk.XiaomiUpdateAgent;

import java.util.Locale;

public class Gojuon extends CustomApplication {
    public static final String KEY_ABOUT = "key_about";
    public static final String KEY_FEEDBACK = "key_feedback";
    public static final String KEY_DONATE = "key_donate";
    public static final String KEY_AUTO_RESUME = "key_auto_resume";
    public static final String KEY_KEEP_SCREEN = "key_screen_on";
    public static final String KEY_KATAKANA_FIRST = "key_katakana_first";
    public static final String KEY_HIGHLIGHT_SELECTED = "key_highlight_selected";
    public static final String KEY_AUTO_ROTATE = "key_auto_rotate";
    public static final String KEY_LANGUAGE = "key_language";

    public static final String LANGUAGE_AUTO = "-1";
    public static final String LANGUAGE_CHINESE = "1";
    public static final String LANGUAGE_ENGLISH = "0";

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

    public void setLanguage() {
        String language = getSharedPreferences().getString(KEY_LANGUAGE, LANGUAGE_AUTO);

        Configuration configuration = getResources().getConfiguration();
        switch (language) {
            case LANGUAGE_AUTO:
                configuration.locale = Locale.getDefault();
                break;

            case LANGUAGE_ENGLISH:
                configuration.locale = Locale.ENGLISH;
                break;

            case LANGUAGE_CHINESE:
                configuration.locale = Locale.CHINA;
                break;
        }

        getBaseContext().getResources().updateConfiguration(configuration, null);
    }

    public void checkUpdate() {
        XiaomiUpdateAgent.setCheckUpdateOnlyWifi(true);
        XiaomiUpdateAgent.update(this);
    }


}
