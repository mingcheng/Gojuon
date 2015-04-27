package com.gracecode.android.gojuon.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import com.gracecode.android.gojuon.common.Gojuon;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 15/4/18
 */
public class Stage implements Parcelable {
    private static final String CLASS_NAME = Stage.class.getName();

    public static final int FULL_SCORE = 100;
    public static final int NONE_SCORE = -1;
    public static final int DEFAULT_ANSWER_TIMEOUT = 3000;
    private static final int NONE_LEVEL = -1;
    public static final int SCORE_QUALIFIED = 60;

    private static Gojuon mGojuon;
    private static SharedPreferences mPreferences;

    private int level = NONE_LEVEL;
    private String[] syllabus;
    private int answerTimeout = DEFAULT_ANSWER_TIMEOUT;

    public Stage(int level, String[] syllabus, int timeout) {
        init(level, syllabus, timeout);
    }

    public Stage(int level, String[] syllabus) {
        init(level, syllabus, DEFAULT_ANSWER_TIMEOUT);
    }

    private void init(int level, String[] syllabus, int timeout) {
        setLevel(level);
        setSyllabus(syllabus);
        setAnswerTimeout(timeout);

        mGojuon = Gojuon.getInstance();
        mPreferences = mGojuon.getSharedPreferences(CLASS_NAME, Context.MODE_PRIVATE);
    }

    public String[] getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String[] syllabus) {
        this.syllabus = syllabus;
    }

    public int getAnswerTimeout() {
        return answerTimeout;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setScore(int score) {
        mPreferences.edit()
                .putInt(getFieldNameByLevel(), score)
                .apply();
    }

    public int getScore() {
        return mPreferences.getInt(getFieldNameByLevel(), NONE_SCORE);
    }

    private String getFieldNameByLevel() {
        return CLASS_NAME + ".level." + getLevel();
    }

    public void setAnswerTimeout(int answerTimeout) {
        this.answerTimeout = answerTimeout;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
