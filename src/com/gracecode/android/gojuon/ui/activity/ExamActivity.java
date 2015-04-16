package com.gracecode.android.gojuon.ui.activity;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import com.gracecode.android.common.helper.UIHelper;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.dao.Question;
import com.gracecode.android.gojuon.helper.ExamHelper;
import com.gracecode.android.gojuon.ui.dialog.ExamBeginDialog;
import com.gracecode.android.gojuon.ui.dialog.ExamResultDialog;
import com.gracecode.android.gojuon.ui.fragment.QuestionFragment;

import java.util.List;

public class ExamActivity extends BaseActivity {
    public static final String KEY_EXAM_AUTO_REDO_WRONG_TOPIC = "key_auto_requiz_wrong_topic";

    private static final String TAG_RESULT_DIALOG = "tag_result_dialog";
    private static final String TAG_FRAGMENT_QUESTION = "tag_question_fragment";
    private static final String TAG_START_DIALOG = "tag_start_dialog";

    private Typeface mCustomTypeface;
    private TextView mAnswersProgress;
    private TextView mAnswersTime;
    private ExamHelper mExamHelper;
    private ExamResultDialog mResultDialog;
    private ExamBeginDialog mExamBeginDialog;
    private QuestionFragment mQuestionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mCustomTypeface = Typeface.createFromAsset(getAssets(), Gojuon.CUSTOM_FONT_NAME);
        mAnswersProgress = (TextView) findViewById(R.id.answer_progress);
        mAnswersTime = (TextView) findViewById(R.id.answer_time);

        mExamHelper = new ExamHelper(this);
        mResultDialog = new ExamResultDialog();
        mExamBeginDialog = new ExamBeginDialog();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_toolbar;
    }

    public void startExam() {
        mExamHelper.reset();
        mExamHelper.generateRandomQuestions();
        setNextQuestion();
    }


    public void redoWrongTopic() {
        List<Question> questions = mExamHelper.getWrongQuestions();
        mExamHelper.reset();
        // shuffle the questions for next quiz.
        //ArrayHelper.shuffle(questions);
        mExamHelper.setQuestions(questions);
        setNextQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mAnswersProgress.setTypeface(mCustomTypeface);
//        mAnswersTime.setTypeface(mCustomTypeface);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showStartDialog();
    }

    private void showStartDialog() {
        if (mExamBeginDialog != null)
            mExamBeginDialog.show(getSupportFragmentManager(), TAG_START_DIALOG);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mExamBeginDialog.dismiss();
            mResultDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void markAnswerFinished() {
        boolean autoRedoWrongTopic = mSharedPreferences.getBoolean(KEY_EXAM_AUTO_REDO_WRONG_TOPIC, false);

        if (mExamHelper.getWrongCount() > 0 && autoRedoWrongTopic) {
            UIHelper.showShortToast(this, String.format(
                    getString(R.string.auto_requiz_wrong_topic_notify),
                    mExamHelper.getWrongCount()));
            redoWrongTopic();
            return;
        }

        if (mResultDialog != null) {
            mResultDialog.show(getSupportFragmentManager(), TAG_RESULT_DIALOG);
        }
    }

    public ExamHelper getExamHelper() {
        return mExamHelper;
    }

    public void addAnsweredQuestion(Question question) {
        mExamHelper.addAnsweredQuestion(question);
    }

    public void setNextQuestion() {
        try {
            updateProgress();
            Question question = mExamHelper.getNextQuestion();
            if (question != null) {
                Fragment fragment = getFragmentManager().findFragmentByTag(TAG_FRAGMENT_QUESTION);
                if (fragment != null) {
                    getFragmentManager().beginTransaction()
                            .remove(fragment).commit();
                }

                mQuestionFragment = new QuestionFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_question, mQuestionFragment, TAG_FRAGMENT_QUESTION)
                        .commit();
            }
        } catch (RuntimeException e) {
            markAnswerFinished();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateProgress() {
        mAnswersProgress.setText(String.format("%d / %d",
                mExamHelper.getAnsweredCount(), mExamHelper.getTotalCount()
        ));
    }
}
