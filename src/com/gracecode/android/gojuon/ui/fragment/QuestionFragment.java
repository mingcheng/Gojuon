package com.gracecode.android.gojuon.ui.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import com.gracecode.android.gojuon.Characters;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.adapter.CharactersAdapter;
import com.gracecode.android.gojuon.common.Gojuon;
import com.gracecode.android.gojuon.dao.Question;
import com.gracecode.android.gojuon.ui.activity.ExamActivity;
import com.gracecode.android.gojuon.ui.widget.CharacterLayout;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 14-5-16
 */
public class QuestionFragment extends Fragment {
    private Question mQuestion;
    private ExamActivity mExamActivity;
    private GridView mGridView;
    private CharactersAdapter mCharactersAdapter;
    private View mAnswerTimeRemain;
    private ValueAnimator mCountdownAnimation;
    private boolean mStopedByUser = false;
    private View mButtonPlay;

    public QuestionFragment() {

    }

    public QuestionFragment(ExamActivity activity, Question question) {
        this.mExamActivity = activity;
        this.mQuestion = question;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, null);
        if (view != null) {
            mGridView = (GridView) view.findViewById(R.id.answer_list);
            mGridView.setEnabled(false);

            mAnswerTimeRemain = view.findViewById(R.id.answer_time_remain);
            mAnswerTimeRemain.setAlpha(255 / 5);

            mButtonPlay = view.findViewById(R.id.answer_play);
            mButtonPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pronounce();
                }
            });
        }
        return view;
    }

    private void pronounce() {
        Gojuon.pronounce(mExamActivity, mQuestion.getAnswer()[Characters.INDEX_ROUMAJI]);
    }

    @Override
    public void onResume() {
        super.onResume();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int count = mGridView.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = mGridView.getChildAt(i);
                    view.setMinimumHeight(mGridView.getHeight() / 2);
                    if (view instanceof CharacterLayout) {
                        ((CharacterLayout) view).autoAdjustTextSize();
                        ((CharacterLayout) view).hideOther();
                    }
                }

                mGridView.setEnabled(true);
                mGridView.setVisibility(View.VISIBLE);

                mCountdownAnimation = ValueAnimator.ofInt(0, mGridView.getWidth());
                mCountdownAnimation.setDuration(3000);
                mCountdownAnimation.setInterpolator(new AccelerateInterpolator());
                mCountdownAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ViewGroup.LayoutParams params = mAnswerTimeRemain.getLayoutParams();
                        params.width = (int) animation.getAnimatedValue();
                        mAnswerTimeRemain.setLayoutParams(params);
                    }
                });

                mCountdownAnimation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (!mStopedByUser)
                            markAnswer(null);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                mCountdownAnimation.start();
                pronounce();
            }
        }, 100);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCountdownAnimation != null && mCountdownAnimation.isRunning()) {
            mCountdownAnimation.cancel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mGridView.setSoundEffectsEnabled(false);
        mGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);

        mCharactersAdapter = new CharactersAdapter(getActivity(), mQuestion.getQuestion());

        mGridView.setAdapter(mCharactersAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mGridView.setEnabled(false);
                markAnswer(mQuestion.getQuestion().get(i));
                mStopedByUser = true;
                mCountdownAnimation.cancel();
            }
        });
    }

    public void highlightAnswer() {
        String[] userAnswered = mQuestion.getLastUserAnswered();
        int position = mQuestion.getQuestion().indexOf(userAnswered);
        if (!mQuestion.isCorrect()) {
            View view = mGridView.getChildAt(position);
            if (view != null)
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        position = mQuestion.getQuestion().indexOf(mQuestion.getAnswer());
        View view = mGridView.getChildAt(position);

        if (view != null) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
            animation.setDuration(500);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.start();
        }
    }

    public void markAnswer(String[] selectedAnswer) {
        boolean isCorrect = mQuestion.isCorrect(selectedAnswer);
        boolean showAnswer = true;

        if (!isCorrect && showAnswer) {
            highlightAnswer();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mExamActivity.addAnsweredQuestion(mQuestion);
                mExamActivity.setNextQuestion();
            }
        }, !isCorrect && showAnswer ? 1500 : 200);
    }
}
