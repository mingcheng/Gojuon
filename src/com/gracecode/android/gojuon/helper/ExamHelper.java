package com.gracecode.android.gojuon.helper;

import android.content.Context;
import android.content.SharedPreferences;
import com.gracecode.android.common.helper.ArrayHelper;
import com.gracecode.android.gojuon.Characters;
import com.gracecode.android.gojuon.common.Gojuon;
import com.gracecode.android.gojuon.dao.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExamHelper {
    private static final int DEFAULT_ANSWER_NUM = 4;

    public static final int DEFAULT_QUESTIONS_NUM = 25;
    public static final String KEY_QUESTION_NUM = "key_question_num";
    public static final String KEY_EXAM_SCOPE = "key_question_scope";

    public static final String EXAM_SCOPE_ALL = "-1";
    public static final String EXAM_SCOPE_MONOGRAPHS = "0";
    public static final String EXAM_SCOPE_MONOGRAPHS_WITH_DIACRITICS = "1";
    public static final String EXAM_SCOPE_DIGRAPHS = "2";
    public static final String EXAM_SCOPE_DIGRAPHS_WITH_DIACRITICS = "3";

    private final Context mContext;
    private final Gojuon mGoJuon;
    private final SharedPreferences mPreferences;

    private int mCurrent = 0;
    private int mQuestionsNumber = DEFAULT_QUESTIONS_NUM;

    private List<Question> mQuestions = new ArrayList<>();
    private List<Question> mAnsweredQuestions = new ArrayList<>();
    private List<String[]> mQuestionsScope = new ArrayList<>();

    public ExamHelper(Context context) {
        mContext = context;
        mGoJuon = Gojuon.getInstance();
        mPreferences = mGoJuon.getSharedPreferences();
        reset();
    }

    private List<String[]> clearEmptyQuestionScope(List<String[]> questions) {
        for (int i = questions.size() - 1; i >= 0; i--) {
            if (questions.get(i)[0].length() == 0) {
                questions.remove(i);
            }
        }

        return questions;
    }

    public int getQuestionsNumber() {
        return mQuestionsNumber;
    }

    public void setQuestionsNumber(int i) {
        this.mQuestionsNumber = i;
    }

    public void setQuestionsNumber() {
        String number = mPreferences.getString(KEY_QUESTION_NUM, String.valueOf(DEFAULT_QUESTIONS_NUM));
        setQuestionsNumber(Integer.parseInt(number));
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
        do {
            int index = (int) (Math.random() * mQuestionsScope.size());
            String[] character = mQuestionsScope.get(index);
            if (!list.contains(character)) {
                list.add(character);
            }
        } while (list.size() < size);
    }

    public void generateRandomQuestions(int num) {
        if (num > mQuestionsScope.size()) {
            num = mQuestionsScope.size();
        }

        reset();

        ArrayList<String[]> answers = new ArrayList<>();
        fillArrays(answers, num);
        for (int i = 0, size = answers.size(); i < size; i++) {
            Question question = generateOneQuestion(answers.get(i));
            mQuestions.add(question);
        }
    }

    public void generateRandomQuestions() {
        generateRandomQuestions(getQuestionsNumber());
    }

    public boolean addAnsweredQuestion(Question question) {
        return mAnsweredQuestions.add(question);
    }

    public Question getNextQuestion() throws RuntimeException {
        return mQuestions.get(mCurrent++);
    }

    public void reset() {
        mCurrent = 0;
        mQuestions.clear();
        mAnsweredQuestions.clear();
        mQuestionsScope.clear();
        setQuestionsNumber();
        setExamScopeFromPreferences();
    }

    private void setExamScopeFromPreferences() {
        String which = mPreferences.getString(KEY_EXAM_SCOPE, EXAM_SCOPE_ALL);

        switch (which) {
            case EXAM_SCOPE_ALL:
                for (String[][] item :
                        new String[][][]{
                                Characters.DIGRAPHS, Characters.DIGRAPHS_WITH_DIACRITICS,
                                Characters.MONOGRAPHS, Characters.MONOGRAPHS_WITH_DIACRITICS}) {
                    addQuestionScope(item);
                }
                break;

            case EXAM_SCOPE_DIGRAPHS:
                addQuestionScope(Characters.DIGRAPHS);
                break;

            case EXAM_SCOPE_DIGRAPHS_WITH_DIACRITICS:
                addQuestionScope(Characters.DIGRAPHS_WITH_DIACRITICS);
                break;

            case EXAM_SCOPE_MONOGRAPHS:
                addQuestionScope(Characters.MONOGRAPHS);
                break;

            case EXAM_SCOPE_MONOGRAPHS_WITH_DIACRITICS:
                addQuestionScope(Characters.MONOGRAPHS_WITH_DIACRITICS);
                break;
        }
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

    public List<Question> getAllQuestions() {
        return mQuestions;
    }
}
