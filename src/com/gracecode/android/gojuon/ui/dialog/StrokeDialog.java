package com.gracecode.android.gojuon.ui.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.gracecode.android.gojuon.R;

public class StrokeDialog extends BaseDialog {
    private ImageView mImageCharacter;
    private ImageView mImageStroke;

    public StrokeDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // request a window without the title
        // @see https://stackoverflow.com/questions/15277460/how-to-create-a-dialogfragment-without-title
        View view = inflater.inflate(R.layout.fragment_stroke, null);
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
