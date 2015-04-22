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

        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_stage, null);

        TextView textView = (TextView) view.findViewById(R.id.stage_level);
        textView.setText(String.valueOf(getItem(i).getLevel()));
        return view;
    }
}
