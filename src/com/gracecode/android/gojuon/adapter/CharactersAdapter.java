package com.gracecode.android.gojuon.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gracecode.android.gojuon.Characters;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.common.Gojuon;

public class CharactersAdapter extends BaseAdapter {
    private final String[][] mCharacters;
    private final Context mContext;
    private final Gojuon mGojuonApp;
    private final SharedPreferences mSharedPreferences;

    private static final class Holder {
        private final TextView mRoumaji;
        private final TextView mHiragana;
        private final TextView mKatakana;
        private static Typeface mHannariFace;

        private Holder(View view) {
            mRoumaji = (TextView) view.findViewById(R.id.roumaji);
            mHiragana = (TextView) view.findViewById(R.id.hiragana);
            mKatakana = (TextView) view.findViewById(R.id.katakana);
//            mHannariFace = Typeface.createFromAsset(context.getAssets(), "hannari.otf");
//
//            mRoumaji.setTypeface(mHannariFace);
//            mHiragana.setTypeface(mHannariFace);
//            mKatakana.setTypeface(mHannariFace);

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


    public CharactersAdapter(Context context, String[][] characters) {
        this.mCharacters = characters;
        this.mContext = context;
        this.mGojuonApp = Gojuon.getInstance();
        this.mSharedPreferences = mGojuonApp.getSharedPreferences();
    }

    @Override
    public int getCount() {
        return mCharacters.length;
    }

    @Override
    public String[] getItem(int i) {
        return mCharacters[i];
    }

    @Override
    public boolean isEnabled(int position) {
        String roumaji = mCharacters[position][Characters.INDEX_ROUMAJI];
        return roumaji.length() > 0;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.item_character, null);
        }

        fillCharacters(Holder.get(view), i);
        return view;
    }

    public void fillCharacters(Holder holder, int i) {
        // Each array element (representing sound) consists of 3 subelements
        // roumaji record, and hiragana and katakana symbols.
        if (mSharedPreferences.getBoolean(Gojuon.KEY_KATAKANA_FIRST, false)) {
            holder.setHiragana(mCharacters[i][Characters.INDEX_KATAKANA]);
            holder.setKatakana(mCharacters[i][Characters.INDEX_HIRAGANA]);
        } else {
            holder.setHiragana(mCharacters[i][Characters.INDEX_HIRAGANA]);
            holder.setKatakana(mCharacters[i][Characters.INDEX_KATAKANA]);
        }

        holder.setRoumaji(mCharacters[i][Characters.INDEX_ROUMAJI]);
    }

    public void fillCharacters(View view, int i) {
        fillCharacters(Holder.get(view), i);
    }
}
