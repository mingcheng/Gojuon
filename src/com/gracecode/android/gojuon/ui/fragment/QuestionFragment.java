package com.gracecode.android.gojuon.ui.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import com.gracecode.android.common.Logger;
import com.gracecode.android.gojuon.Characters;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.adapter.CharactersAdapter;
import com.gracecode.android.gojuon.common.Gojuon;
import com.gracecode.android.gojuon.dao.Question;
import com.gracecode.android.gojuon.ui.activity.ExamActivity;
import com.gracecode.android.gojuon.ui.widget.CharacterLayout;

import java.util.Arrays;

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
    private ValueAnimator animation;
    private boolean stopedByUser = false;

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
            mAnswerTimeRemain = view.findViewById(R.id.answer_time_remain);
            mAnswerTimeRemain.setAlpha(255 / 5);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Gojuon.pronounce(mExamActivity, mQuestion.getAnswer()[Characters.INDEX_ROUMAJI]);

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

                mGridView.setVisibility(View.VISIBLE);

                animation = ValueAnimator.ofInt(0, mGridView.getWidth());
                animation.setDuration(3000);
                animation.setInterpolator(new AccelerateInterpolator());
                animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ViewGroup.LayoutParams params = mAnswerTimeRemain.getLayoutParams();
                        params.width = (int) animation.getAnimatedValue();
                        mAnswerTimeRemain.setLayoutParams(params);
                    }
                });

                animation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (!stopedByUser)
                            markAnswer(null);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                animation.start();
            }
        }, 100);

    }

    @Override
    public void onStop() {
        super.onStop();

        if (animation.isRunning()) {
            animation.cancel();
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
                stopedByUser = true;
                markAnswer(mQuestion.getQuestion().get(i));
            }
        });
    }

    public void markAnswer(String[] selectedAnswer) {
        boolean isCorrect = mQuestion.isCorrect(selectedAnswer);
        if (isCorrect) {
            Logger.e("Correct");
        } else {
            Logger.e("Wrong, Answer is " + Arrays.toString(mQuestion.getAnswer()));
        }
        mGridView.setEnabled(false);
        mExamActivity.addAnsweredQuestion(mQuestion);
        mExamActivity.setNextQuestion();
    }
}
