package com.gracecode.android.gojuon.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import com.gracecode.android.gojuon.R;

public class StrokeFragment extends DialogFragment {

    private ImageView mImageCharacter;
    private ImageView mImageStroke;

    public StrokeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // request a window without the title
        // @see https://stackoverflow.com/questions/15277460/how-to-create-a-dialogfragment-without-title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_stroke, container);

        mImageCharacter = (ImageView) view.findViewById(R.id.img_character);
        mImageStroke = (ImageView) view.findViewById(R.id.img_stroke);

        return view;
    }

    public void setStrokeDrawable(Drawable drawable) {
        mImageStroke.setImageDrawable(drawable);
    }

    public void setCharacterDrawable(Drawable drawable) {
        mImageCharacter.setImageDrawable(drawable);
        mImageCharacter.setImageAlpha((int) (255 * .25));
    }
}
