package com.gracecode.android.gojuon.ui.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.ui.activity.ExamActivity;

public class ExamBeginDialog extends BaseDialog {
    private ExamActivity mActivity;
    private Button mStartButton;

    public ExamBeginDialog() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam_begin, null);
        if (view != null) {
            mStartButton = (Button) view.findViewById(R.id.exam_start);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCancelable(false);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();
                if (activity instanceof ExamActivity) {
                    ((ExamActivity) activity).startExam();
                }
                dismiss();
            }
        });
    }
}
