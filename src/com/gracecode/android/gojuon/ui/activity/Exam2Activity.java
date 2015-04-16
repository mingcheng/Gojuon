package com.gracecode.android.gojuon.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.gracecode.android.common.Logger;
import com.gracecode.android.gojuon.Characters;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.helper.ExamHelper;
import com.gracecode.android.gojuon.ui.fragment.Exam2Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 15/4/16
 */
public class Exam2Activity extends BaseActivity implements Exam2Fragment.OnExam2Listener {
    @InjectView(R.id.toolbar)
    Toolbar mToolbarView;
    private Exam2Fragment mExam2Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbarView);

        mExam2Fragment = new Exam2Fragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mExam2Fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ExamHelper helper = new ExamHelper(this);
        helper.generateRandomQuestions(10);

        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < Characters.MONOGRAPHS.length; i++) {
            String item = Characters.MONOGRAPHS[i][1];
            if (!item.isEmpty()) {
                items.add(item);
            }
        }

        mExam2Fragment.setSyllabus(items);
        mExam2Fragment.setOnExam2Listener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mExam2Fragment.start();
            }
        }, 1000);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.exam));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_toolbar;
    }


    @Override
    public void onExamStart() {
        Logger.i("Exam Start");
    }

    @Override
    public void onItemAnswered(String answered, View view) {
        Logger.i("Answer:" + answered);
    }

    @Override
    public void onExamStop() {

    }

    @Override
    public void onExamFinished(List<String> answers, List<String> answered) {
        Logger.i(answers.size() + ", " + answers.toString());
        Logger.w(answered.size() + ", " + answered.toString());
    }
}
