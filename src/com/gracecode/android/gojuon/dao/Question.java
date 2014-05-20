package com.gracecode.android.gojuon.dao;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Question implements Parcelable {
    private final List<String[]> mQuestion;
    private final String[] mAnswer;
    private String[] mLastUserAnswered;

    public Question(List<String[]> questions, String[] answer) {
        mQuestion = questions;
        mAnswer = answer;
    }

    public List<String[]> getQuestion() {
        return mQuestion;
    }

    public String[] getAnswer() {
        return mAnswer;
    }

    public String[] getLastUserAnswered() {
        return mLastUserAnswered;
    }

    public void setLastUserAnswered(String[] mLastUserAnswered) {
        this.mLastUserAnswered = mLastUserAnswered;
    }

    public boolean isCorrect(String[] answer) {
        mLastUserAnswered = answer;
        return answer == mAnswer;
    }

    public boolean isCorrect() {
        return mLastUserAnswered == mAnswer;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
