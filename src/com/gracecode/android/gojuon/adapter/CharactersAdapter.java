package com.gracecode.android.gojuon.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gracecode.android.gojuon.Characters;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.common.Gojuon;
import com.gracecode.android.gojuon.helper.ViewHelper;

import java.util.List;

public class CharactersAdapter extends BaseCharactersAdapter<String[]> {
    public static final int ANIMATOR_DURATION = 800;

    private View mShadowView;

    public CharactersAdapter(Context context, List<String[]> characters) {
        super(context, characters);
    }

    public void setShadowView(View view) {
        mShadowView = view;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_character, null);
        }

        fillCharacters(Holder.get(view), i);
        if (mShadowView != null && mSharedPreferences.getBoolean(Gojuon.KEY_SHOW_SHADOW, true)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    bindShadowAnimator(getItem(i), view);
                    return false;
                }
            });
        }

        return view;
    }

    public void fillCharacters(View view, int i) {
        fillCharacters(Holder.get(view), i);
    }

    public void fillCharacters(Holder holder, int i) {
        String[] character = getItem(i);

        // Each array element (representing sound) consists of 3 subelements
        // roumaji record, and hiragana and katakana symbols.
        String type = getShowType();
        if (type.equals(TYPE_SHOW_CHARACTER_RANDOM)) {
            type = Math.rint(Math.random()) > 0 ? TYPE_SHOW_CHARACTER_HIRAGANA : TYPE_SHOW_CHARACTER_KATAGANA;
        }

        switch (type) {
            default:
            case TYPE_SHOW_CHARACTER_HIRAGANA:
                holder.setHiragana(character[Characters.INDEX_HIRAGANA]);
                holder.setKatakana(character[Characters.INDEX_KATAKANA]);
                break;

            case TYPE_SHOW_CHARACTER_KATAGANA:
                holder.setHiragana(character[Characters.INDEX_KATAKANA]);
                holder.setKatakana(character[Characters.INDEX_HIRAGANA]);
                break;
        }

        holder.setRoumaji(mCharacters.get(i)[Characters.INDEX_ROUMAJI]);
    }


    protected void bindShadowAnimator(String[] character, View view) {
        if (mShadowView instanceof TextView) {
            switch (getShowType()) {
                case TYPE_SHOW_CHARACTER_HIRAGANA:
                    ((TextView) mShadowView).setText(character[Characters.INDEX_HIRAGANA]);
                    break;
                case TYPE_SHOW_CHARACTER_KATAGANA:
                    ((TextView) mShadowView).setText(character[Characters.INDEX_KATAKANA]);
                    break;
            }
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                getScaleAnimator(view),
                ViewHelper.getFadeOutAnimator(mShadowView, (int) (ANIMATOR_DURATION * .8))
        );
        animatorSet.start();
    }

    private Animator getScaleAnimator(View view) {
        Animator animator = ViewHelper.getScaleAnimator(mShadowView, 0f, 10f, ANIMATOR_DURATION);
        mShadowView.setX(view.getX() + view.getWidth() / 2 - mShadowView.getWidth() / 2);
        mShadowView.setY(view.getY() + view.getHeight() / 2 - mShadowView.getHeight() / 2);
        return animator;
    }
}
