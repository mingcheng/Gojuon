package com.gracecode.android.gojuon.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import com.gracecode.android.common.helper.UIHelper;
import com.gracecode.android.gojuon.Characters;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.common.Gojuon;
import com.gracecode.android.gojuon.dao.Question;
import com.gracecode.android.gojuon.helper.ExamHelper;
import com.gracecode.android.gojuon.ui.fragment.QuestionFragment;

public class ExamActivity extends BaseActivity {

    private Typeface mCustomTypeface;
    private TextView mAnswersProgress;
    private TextView mAnswersTime;
    private AlertDialog.Builder mDialogBuilder;
    private ExamHelper mExamHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        mCustomTypeface = Typeface.createFromAsset(getAssets(), Gojuon.CUSTOM_FONT_NAME);
        mAnswersProgress = (TextView) findViewById(R.id.answer_progress);
        mAnswersTime = (TextView) findViewById(R.id.answer_time);

        mDialogBuilder = new AlertDialog.Builder(this);

        mExamHelper = new ExamHelper(this);
        mExamHelper.addQuestionScope(Characters.MONOGRAPHS);
    }

    private void showStartDialog() {
        mDialogBuilder.setTitle(getString(R.string.app_name));

        // set dialog message
        mDialogBuilder
                .setMessage("Click to Start Test!")
                .setCancelable(false)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mExamHelper.reset();
                        mExamHelper.generateRandomQuestions();
                        setNextQuestion();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = mDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAnswersProgress.setTypeface(mCustomTypeface);
        mAnswersTime.setTypeface(mCustomTypeface);

        showStartDialog();
    }

    private void markAnswerFinished() {
        UIHelper.showShortToast(this,
                "Finished, total answer " +
                        mExamHelper.getAnsweredCount() + " / " + mExamHelper.getTotalCount()
                        + " wrong " + mExamHelper.getWrongCount()
        );

        showStartDialog();
    }

    public void addAnsweredQuestion(Question question) {
        mExamHelper.addAnsweredQuestion(question);
    }

    public void setNextQuestion() {
        try {
            updateProgress();

            Question question = mExamHelper.getNextQuestion();
            if (question != null)
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_question, new QuestionFragment(this, question))
                        .commit();
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
