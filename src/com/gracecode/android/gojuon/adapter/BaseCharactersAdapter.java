package com.gracecode.android.gojuon.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.common.Gojuon;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 15/4/16
 */
abstract class BaseCharactersAdapter<S> extends BaseAdapter {
    public static final String TYPE_SHOW_CHARACTER_RANDOM = "-1";
    public static final String TYPE_SHOW_CHARACTER_HIRAGANA = "0";
    public static final String TYPE_SHOW_CHARACTER_KATAGANA = "1";

    protected final List<S> mCharacters;
    protected final Context mContext;
    protected final Gojuon mGojuon;
    protected SharedPreferences mSharedPreferences;

    public static final class Holder {
        @InjectView(R.id.roumaji)
        TextView mRoumaji;

        @InjectView(R.id.hiragana)
        TextView mHiragana;

        @InjectView(R.id.katakana)
        TextView mKatakana;

        private Holder(View view) {
            ButterKnife.inject(this, view);
            if (Gojuon.useHandwritingFonts()) {
                Gojuon gojuon = Gojuon.getInstance();
                mHiragana.setTypeface(gojuon.getCustomTypeface());
                mKatakana.setTypeface(gojuon.getCustomTypeface());
                mRoumaji.setTypeface(gojuon.getCustomTypeface());
            }
            view.setTag(this);
        }

        public static Holder get(View view) {
            if (view.getTag() instanceof Holder) {
                return (Holder) view.getTag();
            }

            return new Holder(view);
        }

        public void setHiragana(String hiragana) {
            this.mHiragana.setText(hiragana);
        }

        public void setKatakana(String katakana) {
            this.mKatakana.setText(katakana);
        }

        public void setRoumaji(String roumaji) {
            this.mRoumaji.setText(roumaji);
        }
    }

    public BaseCharactersAdapter(Context context, List<S> characters) {
        this.mCharacters = characters;
        this.mContext = context;
        this.mGojuon = Gojuon.getInstance();
        this.mSharedPreferences = mGojuon.getSharedPreferences();
    }

    private String mShowType = TYPE_SHOW_CHARACTER_HIRAGANA;

    public String getShowType() {
        return mShowType;
    }

    public void setShowType(String type) {
        mShowType = type;
    }

    @Override
    public int getCount() {
        return mCharacters.size();
    }

    @Override
    public S getItem(int i) {
        return mCharacters.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setItem(List<S> items) {
        mCharacters.clear();
        mCharacters.addAll(items);
    }

    @Override
    public abstract View getView(int i, View view, ViewGroup viewGroup);
}
