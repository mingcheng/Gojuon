package com.gracecode.android.gojuon.ui.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import butterknife.ButterKnife;
import com.gracecode.android.common.Logger;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.common.Gojuon;
import com.gracecode.android.gojuon.dao.Stage;
import com.gracecode.android.gojuon.helper.StageHelper;
import com.gracecode.android.gojuon.ui.fragment.Exam2Fragment;
import com.gracecode.android.gojuon.ui.fragment.StageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 15/4/16
 */
public class Exam2Activity extends SlideActivity
        implements Exam2Fragment.OnExam2Listener, AdapterView.OnItemClickListener, SlideActivity.OnPanelStatusChangeListener {

    private Exam2Fragment mExam2Fragment;
    private StageHelper mStageHelper;
    private ArrayList<Stage> mStages = new ArrayList<>();
    private StageFragment mStageFragment;
    private ValueAnimator animator;
    private boolean mEndedByUser;
    private Stage mCurrentStage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        mExam2Fragment = Exam2Fragment.getInstance();
        mExam2Fragment.setOnExam2Listener(this);

        mStageHelper = StageHelper.getInstance(Gojuon.getInstance());
        mStageFragment = new StageFragment();

        mStages.clear();
        mStages.addAll(mStageHelper.getAllStages());
        mStageFragment.setStages(mStages);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mStageFragment)
                .replace(R.id.panel, mExam2Fragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setOnPanelStatusChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.exam));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_slide;
    }

    @Override
    public void onExamStart() {
        Logger.i("Exam Start");
    }

    @Override
    public void onItemAnswered(String answered, int position) {
        Logger.i("Answer " + position + " :" + answered);
    }

    @Override
    public void onExamStop() {
        Logger.i("Exam Stop");
    }

    @Override
    public void onExamFinished(List<String> answers, List<String> answered) {
        Logger.i(answers.size() + ", " + answers.toString());
        Logger.w(answered.size() + ", " + answered.toString());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        mCurrentStage = mStages.get(i);
        mExam2Fragment.setSyllabus(mCurrentStage.getSyllabus());
        setTitle(getString(R.string.exam) + " - " + mCurrentStage.getLevel());
        open();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isOpened() && item.getItemId() == android.R.id.home) {
            close();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnPanelOpened() {
        mExam2Fragment.init();
        mExam2Fragment.start();
    }

    @Override
    public void OnPanelClosed() {
        setTitle(getString(R.string.exam));
        mExam2Fragment.stop();
    }
}
