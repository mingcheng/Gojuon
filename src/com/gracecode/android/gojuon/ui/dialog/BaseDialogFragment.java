package com.gracecode.android.gojuon.ui.dialog;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Window;

abstract class BaseDialogFragment extends DialogFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        super.onActivityCreated(savedInstanceState);
    }
}
