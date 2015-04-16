package com.gracecode.android.gojuon.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.adapter.QuestionAdapter;
import com.gracecode.android.gojuon.helper.ViewHelper;
import com.gracecode.android.gojuon.ui.widget.CountdownTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 15/4/16
 */
public class Exam2Fragment extends Fragment implements AdapterView.OnItemClickListener, Animator.AnimatorListener {
    private static final String NONE_ANSWER = "";

    private OnExam2Listener mListener;

    @InjectView(R.id.questions_list)
    GridView mQuestionsGridView;

    @InjectView(R.id.answer_status)
    TextView mAnswerStatusView;

    @InjectView(R.id.time_remain)
    View mTimeRemainView;

    @InjectView(R.id.countdown)
    CountdownTextView mCountdownView;

    private QuestionAdapter mGridViewAdapter;
    private List<String> mSyllabus;

    private int mAmount = 10;
    private AnimatorSet mAnimatorSet;
    private List<String> mAnswers = new ArrayList<>();
    private List<String> mAnswered = new ArrayList<>();
    private int mCurrentPosition = 0;
    private long mAnswerDuration = 3000;
    private boolean mStoppedByUser;

    @Override
    public void onAnimationStart(Animator animator) {
        // pronounce
        mStoppedByUser = false;
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        if (!mStoppedByUser) {
            markAnswer(mCurrentPosition, NONE_ANSWER);
            fillNextQuestion();
        }
    }

    @Override
    public void onAnimationCancel(Animator animator) {
        mStoppedByUser = true;
    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    public interface OnExam2Listener {
        abstract public void onExamStart();

        abstract public void onItemAnswered(String answered, View view);

        abstract public void onExamStop();

        abstract public void onExamFinished(List<String> answers, List<String> answered);
    }

    public Exam2Fragment() {
        super();
    }

    public long getAnswerDuration() {
        return mAnswerDuration;
    }

    public void setAnswerDuration(long duration) {
        this.mAnswerDuration = duration;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam2, null);
        ButterKnife.inject(this, view);
        mQuestionsGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                for (int i = 0, size = mQuestionsGridView.getChildCount(); i < size; i++) {
                    View view = mQuestionsGridView.getChildAt(i);
                    view.getLayoutParams().height = mQuestionsGridView.getHeight() / (QuestionAdapter.MAX_SELECTION_COUNT / 2);
                }
            }
        });

        mGridViewAdapter = new QuestionAdapter(getActivity(), new ArrayList<String>());
        mQuestionsGridView.setAdapter(mGridViewAdapter);
        return view;
    }

    public void setOnExam2Listener(OnExam2Listener listener) {
        this.mListener = listener;
    }

    public void setSyllabus(List<String> syllabus) throws IllegalArgumentException {
        if (syllabus.size() < QuestionAdapter.MAX_SELECTION_COUNT) {
            throw new IllegalArgumentException();
        }

        mSyllabus = syllabus;
        generateAnswers();
    }

    public void setAmount(int amount) {
        mAmount = amount;
    }

    private void generateAnswers() {
        mAnswers.clear();
        while (mAnswers.size() < mAmount) {
            String answer = mSyllabus.get((int) (Math.random() * (mSyllabus.size() - 1)));
            if (!mAnswers.contains(answer)) {
                mAnswers.add(answer);
            }
        }
    }

    private List<String> generateQuestionByAnswer(String answer) {
        ArrayList<String> question = new ArrayList<>();
        question.add(answer); // Add answer first
        while (question.size() < QuestionAdapter.MAX_SELECTION_COUNT) {
            String item = mSyllabus.get((int) (Math.random() * (mSyllabus.size() - 1)));
            if (!question.contains(item)) {
                question.add(item);
            }
        }
        Collections.shuffle(question);
        return question;
    }

    @Override
    @OnItemClick(R.id.questions_list)
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mStoppedByUser = true;
        mQuestionsGridView.setEnabled(false);

        if (mAnswered.size() >= mAnswers.size()) {
            return;
        }
        stopRemainViewAnimator();
        markAnswer(mCurrentPosition, mGridViewAdapter.getItem(i));
        fillNextQuestion();
    }

    private void fillNextQuestion() {
        if (mAnswered.size() >= mAnswers.size()) {
            stopRemainViewAnimator();
            return;
        }
        fillQuestion(++mCurrentPosition);
    }

    private void fillQuestion(int position) {
        String answer = NONE_ANSWER;
        try {
            answer = mAnswers.get(position);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }

        List<String> question = generateQuestionByAnswer(answer);
        mGridViewAdapter.setQuestion(question);
        mQuestionsGridView.setEnabled(true);

        setAnswerProgress(position);
        startRemainViewAnimator();
    }


    private void startRemainViewAnimator() {
        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            stopRemainViewAnimator();
        }

        ValueAnimator animator = ValueAnimator.ofInt(0, mQuestionsGridView.getWidth());
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mTimeRemainView.getLayoutParams().width = (int) valueAnimator.getAnimatedValue();
                mTimeRemainView.requestLayout();
            }
        });

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animator,
                ViewHelper.getFadeInAnimator(mTimeRemainView, (int) (getAnswerDuration() * .8))
        );
        mAnimatorSet.setDuration(getAnswerDuration());
        mAnimatorSet.addListener(this);
        mAnimatorSet.start();
    }

    private void stopRemainViewAnimator() {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
            mAnimatorSet = null;
        }
    }

    private void markAnswer(int position, String answer) {
        mAnswered.add(position, answer);
        if (mListener != null) {
            mListener.onItemAnswered(answer, null);
        }
        if (mAnswered.size() >= mAnswers.size()) {
            stopRemainViewAnimator();
            mQuestionsGridView.setEnabled(false);

            if (mListener != null) {
                mListener.onExamFinished(mAnswers, mAnswered);
            }
        }
    }

    private void setAnswerProgress(int i) {
        mAnswerStatusView.setText(++i + "/" + mAmount);
    }

    public void init() {
        mCurrentPosition = 0;
        mAnswered.clear();
        generateAnswers();
        stopRemainViewAnimator();
    }

    public void start() {
        if (mListener != null) {
            mListener.onExamStart();
        }
        init();

        mCountdownView.setCountdownNumber(3);
        mCountdownView.setCountdownListener(new CountdownTextView.CountdownListener() {
            @Override
            public void onRepeat(int repeat) {

            }

            @Override
            public void onCountdownEnd() {
                fillQuestion(mCurrentPosition);
            }

            @Override
            public void onCancel() {

            }
        });

        mCountdownView.startCountdown();
    }


    public void stop() {
        if (mListener != null) {
            mListener.onExamStop();
        }
        stopRemainViewAnimator();
    }

    @Override
    public void onStop() {
        super.onStop();
        stop();
    }
}
