package com.gracecode.android.gojuon.helper;

import android.content.Context;
import com.gracecode.android.common.helper.ArrayHelper;
import com.gracecode.android.gojuon.dao.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExamHelper {
    private static final int DEFAULT_ANSWER_NUM = 4;
    public static int DEFAULT_QUESTIONS_NUM = 50;
    private final Context mContext;

    private int mCurrent = 0;
    private List<Question> mQuestions = new ArrayList<>();
    private List<Question> mAnsweredQuestions = new ArrayList<>();

    private List<String[]> mQuestionsScope = new ArrayList<>();


    public ExamHelper(Context context) {
        mContext = context;
    }

    private List<String[]> clearEmptyQuestionScope(List<String[]> questions) {
        for (int i = questions.size() - 1; i >= 0; i--) {
            if (questions.get(i)[0].length() == 0) {
                questions.remove(i);
            }
        }

        return questions;
    }


    private Question generateOneQuestion(String[] answer) {
        List<String[]> question = new ArrayList<>();
        question.add(answer);
        fillArrays(question, DEFAULT_ANSWER_NUM);
        ArrayHelper.shuffle(question);
        return new Question(question, answer);
    }

    public void addQuestionScope(String[][] characters) {
        mQuestionsScope.addAll(Arrays.asList(characters));
        clearEmptyQuestionScope(mQuestionsScope);
    }


    private void fillArrays(List<String[]> list, int size) {
        for (int i = list.size(); list.size() < size; i++) {
            int index = (int) (Math.random() * mQuestionsScope.size());
            String[] character = mQuestionsScope.get(index);
            if (list.contains(character)) {
                i--;
            } else {
                list.add(i, character);
            }
        }
    }

    public void generateRandomQuestions(int num) {
        ArrayList<String[]> answers = new ArrayList<>();
        fillArrays(answers, num);
        for (int i = 0; i < answers.size(); i++) {
            Question question = generateOneQuestion(answers.get(i));
            mQuestions.add(question);
        }
    }

    public void generateRandomQuestions() {
        generateRandomQuestions(DEFAULT_QUESTIONS_NUM);
    }

    public boolean addAnsweredQuestion(Question question) {
        return mAnsweredQuestions.add(question);
    }

    public Question seekToQuestion(int position) {
        mCurrent = position;
        return mQuestions.get(position);
    }

    public Question getNextQuestion() throws RuntimeException {
        return mQuestions.get(mCurrent++);
    }

    public void reset() {
        mCurrent = 0;
        mQuestions.clear();
        mAnsweredQuestions.clear();
    }

    public List<Question> getWrongQuestions() {
        List<Question> result = new ArrayList<>();

        for (int i = 0; i < mQuestions.size(); i++) {
            try {
                Question question = mAnsweredQuestions.get(i);
                if (!question.isCorrect()) {
                    result.add(question);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                result.add(mQuestions.get(i));
            }
        }

        return result;
    }

    public int getWrongCount() {
        return getWrongQuestions().size();
    }

    public int getAnsweredCount() {
        return mAnsweredQuestions.size();
    }

    public int getTotalCount() {
        return mQuestions.size();
    }

    public List<Question> getAnsweredQuestions() {
        return mAnsweredQuestions;
    }

    public void setQuestions(List<Question> questions) {
        this.mQuestions = questions;
    }
}
