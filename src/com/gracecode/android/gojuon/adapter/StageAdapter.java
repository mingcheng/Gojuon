package com.gracecode.android.gojuon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.dao.Stage;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 15/4/21
 */
public class StageAdapter extends BaseAdapter {
    private final List<Stage> mStages;
    private final Context mContext;

    public StageAdapter(Context context, List<Stage> stages) {
        mContext = context;
        mStages = stages;
    }

    @Override
    public int getCount() {
        return mStages.size();
    }

    @Override
    public Stage getItem(int i) {
        return mStages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Stage stage = getItem(i);
        int score = stage.getScore();

        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_stage, null);

        TextView scoreView = (TextView) view.findViewById(R.id.score);
        if (score == Stage.NONE_SCORE) {
            scoreView.setText(mContext.getString(R.string.no_score));
            view.setBackgroundResource(R.drawable.stage_nop);
        } else {
            if (score >= Stage.SCORE_QUALIFIED) {
                view.setBackgroundResource(R.drawable.stage_good);
            } else {
                view.setBackgroundResource(R.drawable.stage_bad);
            }
            scoreView.setText(score + "%");
        }


        TextView textView = (TextView) view.findViewById(R.id.stage_level);
        textView.setText(String.valueOf(stage.getLevel()));
        return view;
    }
}
