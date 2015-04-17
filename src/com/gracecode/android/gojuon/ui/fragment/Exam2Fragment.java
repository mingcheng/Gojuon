package com.gracecode.android.gojuon.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
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
import com.gracecode.android.gojuon.common.Gojuon;
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
    public static final String KEY_EXAM_HIGHLIGHT_RESULT = "key_exam_highlight_result";

    private static final String NONE_ANSWER = "";
    private static final int DEFAULT_QUESTION_AMOUNT = 30;
    private final SharedPreferences mSharedPreferences;

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

    private int mAmount = DEFAULT_QUESTION_AMOUNT;
    private AnimatorSet mAnimatorSet;
    private List<String> mAnswers = new ArrayList<>();
    private List<String> mAnswered = new ArrayList<>();
    private int mCurrentPosition = 0;
    private long mAnswerDuration = 3000;
    private boolean mStoppedByUser;

    @Override
    public void onAnimationStart(Animator animator) {
        mStoppedByUser = false;
        pronounce(getCorrectAnswer());
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        if (!mStoppedByUser) {
            markAnswer(mCurrentPosition, NONE_ANSWER);
        }
    }

    @Override
    public void onAnimationCancel(Animator animator) {
        mStoppedByUser = true;
    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    private void pronounce(String charsets) {
        Gojuon.pronounce(getActivity(), charsets);
    }

    public interface OnExam2Listener {
        abstract public void onExamStart();

        abstract public void onItemAnswered(String answered, int position);

        abstract public void onExamStop();

        abstract public void onExamFinished(List<String> answers, List<String> answered);
    }

    public Exam2Fragment() {
        super();
        mSharedPreferences = Gojuon.getInstance().getSharedPreferences();
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
        stopRemainViewAnimator();
        mStoppedByUser = true;
        mQuestionsGridView.setEnabled(false);
        markAnswer(mCurrentPosition, mGridViewAdapter.getItem(i));
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

    private String getCorrectAnswer() {
        try {
            return mAnswers.get(mCurrentPosition);
        } catch (RuntimeException e) {
            return NONE_ANSWER;
        }
    }

    private void markAnswer(int position, String answer) {
        if (mListener != null) {
            mListener.onItemAnswered(answer, position);
        }

        mAnswered.add(position, answer);
        boolean isHighlightAnswer = mSharedPreferences.getBoolean(KEY_EXAM_HIGHLIGHT_RESULT, true);
        if (isHighlightAnswer && !answer.equals(getCorrectAnswer())) {
//            Logger.i("Answer is " + mAnswers.get(mCurrentPosition) + ", you answered " + answer);
            pronounce(getCorrectAnswer());
            highlightViewByAnswer(getCorrectAnswer(), new CountdownTextView.CountdownListener() {
                @Override
                public void onRepeat(int repeat) {

                }

                @Override
                public void onCountdownEnd() {
                    detectAnswerFinished();
                    fillNextQuestion();
                }

                @Override
                public void onCancel() {

                }
            });
        } else {
            detectAnswerFinished();
            fillNextQuestion();
        }
    }

    private void detectAnswerFinished() {
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

    public void highlightViewByAnswer(String answer, CountdownTextView.CountdownListener listener) {
        mCountdownView.setText(answer);
        mCountdownView.setTextColor(getResources().getColor(R.color.red));
        mCountdownView.setCountdownNumber(1);
        mCountdownView.setCountdownCharacters(new String[]{answer});
        mCountdownView.setCountdownListener(listener);
        mCountdownView.startCountdown();
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

        mCountdownView.setTextColor(getResources().getColor(R.color.primary_dark));
        mCountdownView.setCountdownNumber(3);
        mCountdownView.setCountdownCharacters(new String[]{"いち", "に", "さん"});
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
        mCountdownView.stopCountdown();
        stop();
    }
}
