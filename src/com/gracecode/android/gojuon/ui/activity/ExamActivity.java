package com.gracecode.android.gojuon.ui.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.common.Gojuon;
import com.gracecode.android.gojuon.dao.Question;
import com.gracecode.android.gojuon.helper.ExamHelper;
import com.gracecode.android.gojuon.ui.dialog.ExamResultDialog;
import com.gracecode.android.gojuon.ui.fragment.QuestionFragment;

import java.util.List;

public class ExamActivity extends BaseActivity {
    private static final String TAG_RESULT_DIALOG = "tag_result_dialog";
    private static final String TAG_FRAGMENT_QUESTION = "tag_question_fragment";

    private Typeface mCustomTypeface;
    private TextView mAnswersProgress;
    private TextView mAnswersTime;
    private AlertDialog.Builder mDialogBuilder;
    private ExamHelper mExamHelper;
    private ExamResultDialog mResultDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        mCustomTypeface = Typeface.createFromAsset(getAssets(), Gojuon.CUSTOM_FONT_NAME);
        mAnswersProgress = (TextView) findViewById(R.id.answer_progress);
        mAnswersTime = (TextView) findViewById(R.id.answer_time);

        mDialogBuilder = new AlertDialog.Builder(this);

        mExamHelper = new ExamHelper(this);

        mResultDialog = new ExamResultDialog(this);
    }

    private void showStartDialog() {
        mDialogBuilder.setTitle(getString(R.string.app_name));

        // set dialog message
        mDialogBuilder
                .setMessage("Click to Start Test!")
                .setCancelable(false)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startExam();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = mDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void startExam() {
        mExamHelper.reset();
        mExamHelper.generateRandomQuestions();
        setNextQuestion();
    }


    public void redoWrongTopic() {
        List<Question> questions = mExamHelper.getWrongQuestions();

        mExamHelper.reset();
        mExamHelper.setQuestions(questions);

        setNextQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAnswersProgress.setTypeface(mCustomTypeface);
        mAnswersTime.setTypeface(mCustomTypeface);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showStartDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mResultDialog = null;
    }

    private void markAnswerFinished() {
        if (mResultDialog != null)
            mResultDialog.show(getSupportFragmentManager(), TAG_RESULT_DIALOG);
    }

    public ExamHelper getExamHelper() {
        return mExamHelper;
    }

    public void addAnsweredQuestion(Question question) {
        mExamHelper.addAnsweredQuestion(question);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_question, new QuestionFragment(this, question), TAG_FRAGMENT_QUESTION)
                        .commit();
            }
        } catch (RuntimeException e) {
            markAnswerFinished();
        }
    }

    private void updateProgress() {
        mAnswersProgress.setText(String.format("%d / %d",
                mExamHelper.getAnsweredCount(), mExamHelper.getTotalCount()
        ));
    }
}
