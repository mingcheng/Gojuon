package com.gracecode.android.gojuon.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
import com.gracecode.android.gojuon.dao.Stage;
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
    private static Exam2Fragment fragment;
    private final SharedPreferences mSharedPreferences;

    private OnExam2Listener mListener;

    @InjectView(R.id.questions_list)
    GridView mQuestionsGridView;

    @InjectView(R.id.answer_status)
    TextView mAnswerStatusView;

    @InjectView(R.id.time_remain)
    View mTimeRemainView;

    @InjectView(R.id.mask)
    View mMaskView;

    @InjectView(R.id.countdown)
    CountdownTextView mCountdownView;

    private QuestionAdapter mGridViewAdapter;
    private List<String> mSyllabus = new ArrayList<>();

    private int mAmount = DEFAULT_QUESTION_AMOUNT;
    private AnimatorSet mAnimatorSet;
    private List<String> mAnswers = new ArrayList<>();
    private List<String> mAnswered = new ArrayList<>();
    private int mCurrentPosition = 0;
    private long mAnswerDuration = 3000;

    private boolean mStoppedByUser;
    private boolean mSelectedByUser;
    private boolean mRunning;

    public static Exam2Fragment getInstance() {
        if (fragment == null) {
            fragment = new Exam2Fragment();
        }
        return fragment;
    }

    public interface OnExam2Listener {
        abstract public void onExamStart();

        abstract public void onItemAnswered(String answered, int position);

        abstract public void onExamStop();

        abstract public void onExamFinished(int Score, List<String> answers, List<String> answered);
    }

    @SuppressLint("ValidFragment")
    Exam2Fragment() {
        super();
        mSharedPreferences = Gojuon.getInstance().getSharedPreferences();
    }

    @Override
    public void onAnimationStart(Animator animator) {
        mSelectedByUser = false;
        pronounce(getCorrectAnswer());
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        if (!mSelectedByUser) {
            markAnswer(mCurrentPosition, NONE_ANSWER);
        }
    }

    @Override
    public void onAnimationCancel(Animator animator) {
        mSelectedByUser = true;
    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    /**
     * 根据某个字符发音
     *
     * @param charsets
     */
    private void pronounce(String charsets) {
        Gojuon.pronounce(getActivity(), charsets);
    }

    /**
     * 获取回答的超时时间
     *
     * @return
     */
    public long getAnswerDuration() {
        return mAnswerDuration;
    }

    /**
     * 设置回答的超时时间
     *
     * @param duration
     */
    public void setAnswerDuration(long duration) {
        this.mAnswerDuration = duration;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam2, container, false);
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

    /**
     * 设置回调操作，方便外部调用
     *
     * @param listener
     */
    public void setOnExam2Listener(OnExam2Listener listener) {
        this.mListener = listener;
    }

    /**
     * 设置提纲
     *
     * @param syllabus
     * @throws IllegalArgumentException
     */
//    public void setSyllabus(List<String> syllabus) throws IllegalArgumentException {
//        if (isRunning()) {
//            stop();
//        }
//        if (syllabus.size() < QuestionAdapter.MAX_SELECTION_COUNT) {
//            throw new IllegalArgumentException();
//        }
//        mSyllabus.clear();
//        mSyllabus.addAll(syllabus);
//    }

    /**
     * 设置提纲
     *
     * @param syllabus
     * @throws IllegalArgumentException
     */
    public void setSyllabus(String[] syllabus) throws IllegalArgumentException {
        if (isRunning()) {
            stop();
        }
        if (syllabus.length < QuestionAdapter.MAX_SELECTION_COUNT) {
            throw new IllegalArgumentException();
        }
        mSyllabus.clear();
        for (int i = 0, length = syllabus.length; i < length; i++) {
            mSyllabus.add(i, syllabus[i]);
        }

        Collections.shuffle(mSyllabus);
    }

    /**
     * 判断是否正在运行
     *
     * @return
     */
    public boolean isRunning() {
        return mRunning;
    }

    /**
     * 设置题目的数量
     *
     * @param amount
     */
    public void setAmount(int amount) {
        mAmount = amount;
    }

    /**
     * 生成答案列表
     */
    private void generateAnswers() {
        mAnswers.clear();
        setAmount(DEFAULT_QUESTION_AMOUNT);
        if (mSyllabus.size() < mAmount) {
            setAmount(mSyllabus.size());
            mAnswers.addAll(mSyllabus);
            return;
        }

        while (mAnswers.size() < mAmount) {
            String answer = mSyllabus.get((int) (Math.random() * (mSyllabus.size() - 1)));
            if (!mAnswers.contains(answer)) {
                mAnswers.add(answer);
            }
        }
    }

    /**
     * 根据答案生成选择
     *
     * @param answer
     * @return
     */
    private List<String> generateQuestionByAnswer(String answer) {
        ArrayList<String> question = new ArrayList<>();
        question.add(answer); // Add answer first
        while (question.size() < QuestionAdapter.MAX_SELECTION_COUNT) {
            String item = mSyllabus.get((int) (Math.random() * (mSyllabus.size() - 1)));
            if (!question.contains(item)) {
                question.add(item);
            }
        }

        Collections.shuffle(question);    // 打乱选择
        return question;
    }

    @Override
    @OnItemClick(R.id.questions_list)
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mQuestionsGridView.setEnabled(false);
        mSelectedByUser = true;
        stopRemainViewAnimator();
        markAnswer(mCurrentPosition, mGridViewAdapter.getItem(i));
    }

    private void fillNextQuestion() {
        if (mAnswered.size() >= mAnswers.size()) {
            stopRemainViewAnimator();
            return;
        }
        fillQuestion(++mCurrentPosition);
    }

    /**
     * 填充答题的内容
     *
     * @param position
     */
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
        boolean isHighlightAnswer = mSharedPreferences.getBoolean(KEY_EXAM_HIGHLIGHT_RESULT, true);

        if (mListener != null) {
            mListener.onItemAnswered(answer, position);
        }
        mAnswered.add(position, answer);

        if (isHighlightAnswer && !answer.equals(getCorrectAnswer())) {
            pronounce(getCorrectAnswer());
            highlightViewByAnswer(getCorrectAnswer(), new CountdownTextView.CountdownListener() {
                @Override
                public void onRepeat(int repeat) {

                }

                @Override
                public void onCountdownEnd() {
                    if (!isAnswerFinished() && !mStoppedByUser) {
                        fillNextQuestion();
                    }
                }

                @Override
                public void onCancel() {

                }
            });
        } else {
            if (!isAnswerFinished() && !mStoppedByUser) {
                fillNextQuestion();
            }
        }
    }

    private boolean isAnswerFinished() {
        if (mAnswered.size() >= mAnswers.size() || mStoppedByUser) {
            stopRemainViewAnimator();
            mQuestionsGridView.setEnabled(false);

            if (mListener != null) {
                mListener.onExamFinished(getScore(), mAnswers, mAnswered);
            }

            return true;
        }

        return false;
    }

    /**
     * 根据答题情况获取分数
     *
     * @return
     */
    private int getScore() {
        float score = 0f;
        for (int i = 0, size = mAnswers.size(); i < size; i++) {
            try {
                if (mAnswers.get(i).equals(mAnswered.get(i))) {
                    score++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        return (int) (Stage.FULL_SCORE * (score / mAnswers.size()));
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
        mStoppedByUser = false;
        mSelectedByUser = false;
        mAnswered.clear();
        mCurrentPosition = mAnswered.size();
        generateAnswers();
        stopRemainViewAnimator();
        mCountdownView.stopCountdown();
        mMaskView.setVisibility(View.VISIBLE);
    }

    public void start() {
        if (mListener != null) {
            mListener.onExamStart();
        }

        if (mAnswers.size() <= 0) {
            return;
        }

        mCountdownView.setTextColor(getResources().getColor(R.color.primary_dark));
        mCountdownView.setCountdownCharacters(new String[]{"いち", "に", "さん"});
        mCountdownView.setCountdownListener(new CountdownTextView.CountdownListener() {
            @Override
            public void onRepeat(int repeat) {

            }

            @Override
            public void onCountdownEnd() {
                if (!mSelectedByUser) {
                    mRunning = true;
                    mMaskView.setVisibility(View.GONE);
                    fillQuestion(mCurrentPosition);
                } else {
                    toString();
                }
            }

            @Override
            public void onCancel() {

            }
        });

        mMaskView.setVisibility(View.VISIBLE);
        mCountdownView.startCountdown();
    }


    public void stop() {
        if (mListener != null) {
            mListener.onExamStop();
        }

        mStoppedByUser = true;
        mSelectedByUser = true;
        mRunning = false;

        mSyllabus.clear();
        mAnswers.clear();
        mAnswered.clear();
        mCountdownView.stopCountdown();
        stopRemainViewAnimator();
        mMaskView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        stop();
    }
}
