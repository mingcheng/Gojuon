package com.gracecode.android.gojuon.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.dao.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 15/4/24
 */
public class ResultDialogHelper {
    private final Context mContext;
    private final Runnable mRunnable;
    private AlertDialog mResultDialog;

    public ResultDialogHelper(Context context, Runnable runnable) {
        mContext = context;
        mRunnable = runnable;
        init();
    }

    private void init() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fragment_result, null);

        builder.setCancelable(false)
                .setView(view)
                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mRunnable != null) mRunnable.run();
                    }
                });

        mResultDialog = builder.create();
    }

    public void show() {
        if (mResultDialog != null) {
            mResultDialog.show();
        }
    }

    public void dismiss() {
        if (mResultDialog != null) {
            mResultDialog.dismiss();
        }
    }

    public void setScore(int score) {
        TextView mScoreView = (TextView) mResultDialog.findViewById(R.id.score);
        View mPracticeLayoutView = mResultDialog.findViewById(R.id.practice_layout);

        if (score == Stage.NONE_SCORE) {
            mScoreView.setText(mContext.getString(R.string.no_score));
            mPracticeLayoutView.setVisibility(View.GONE);
        } else {
            if (score >= Stage.SCORE_QUALIFIED) {
                mScoreView.setTextColor(mContext.getResources().getColor(R.color.stage_good));
            } else {
                mScoreView.setTextColor(mContext.getResources().getColor(R.color.stage_bad));
            }

            mScoreView.setText(String.valueOf(score));
            mPracticeLayoutView.setVisibility(View.VISIBLE);
        }
    }

    private List<String> getDiffAnswered(List<String> answers, List<String> answered)
            throws ArrayIndexOutOfBoundsException {
        List<String> result = new ArrayList<>();
        for (int i = 0, size = answers.size(); i < size; i++) {
            String tmp = answers.get(i);
            if (!tmp.isEmpty() && !tmp.equals(answered.get(i)) && !result.contains(tmp)) {
                result.add(tmp);
            }
        }

        return result;
    }

    private static String join(Collection<String> s, String delimiter) {
        if (s == null || s.isEmpty()) return "";
        Iterator<String> iter = s.iterator();
        StringBuilder builder = new StringBuilder(iter.next());
        while (iter.hasNext()) {
            builder.append(delimiter).append(iter.next());
        }
        return builder.toString();
    }

    public void setAnswered(List<String> answers, List<String> answered) {
        TextView mPracticeWordsView = (TextView) mResultDialog.findViewById(R.id.practice_words);
        mPracticeWordsView.setText(join(getDiffAnswered(answers, answered), "、"));
    }
}
