package com.gracecode.android.gojuon.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.adapter.StageAdapter;
import com.gracecode.android.gojuon.dao.Stage;
import com.gracecode.android.gojuon.ui.activity.Exam2Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 15/4/21
 */
public class StageFragment extends Fragment {
    private static final String KEY_PREFIX = StageFragment.class.getName();
    private static final String KEY_STAGES = KEY_PREFIX + ".key_stages";

    private static List<Stage> mStages = new ArrayList<>();

    @InjectView(R.id.stage_list)
    GridView mStageGridView;

    private StageAdapter mStageAdapter;

    public static StageFragment getInstance(ArrayList<Stage> stages) {
        StageFragment fragment = new StageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_STAGES, stages);
        fragment.setArguments(bundle);
        return fragment;
    }

    public StageFragment() {
        super();
    }

    public void notifyDataSetChanged() {
        try {
            mStageAdapter.notifyDataSetChanged();
            mStageGridView.requestLayout();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void setStages(List<Stage> stages) {
        mStages = stages;
        notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stage, container, false);
        ButterKnife.inject(this, view);
        mStageAdapter = new StageAdapter(getActivity(), mStages);
        mStageGridView.setAdapter(mStageAdapter);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ArrayList<Stage> stages = getArguments().getParcelableArrayList(KEY_STAGES);
            setStages(stages);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() instanceof Exam2Activity) {
            mStageGridView.setOnItemClickListener((Exam2Activity) getActivity());
        }
    }
}
