package com.gracecode.android.gojuon.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gracecode.android.gojuon.Characters;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.common.Gojuon;

import java.util.List;

public class CharactersAdapter extends BaseAdapter {
    public static final String TYPE_SHOW_CHARACTER_RANDOM = "-1";
    public static final String TYPE_SHOW_CHARACTER_HIRAGANA = "0";
    public static final String TYPE_SHOW_CHARACTER_KATAGANA = "1";

    private List<String[]> mCharacters;
    private Context mContext;
    private Gojuon mGojuon;
    private SharedPreferences mSharedPreferences;
    private String mShowType = TYPE_SHOW_CHARACTER_HIRAGANA;

    public String getShowType() {
        return mShowType;
    }

    public void setShowType(String type) {
        this.mShowType = type;
    }

    private static final class Holder {
        private final TextView mRoumaji;
        private final TextView mHiragana;
        private final TextView mKatakana;

        private Holder(View view) {
            mRoumaji = (TextView) view.findViewById(R.id.roumaji);
            mHiragana = (TextView) view.findViewById(R.id.hiragana);
            mKatakana = (TextView) view.findViewById(R.id.katakana);
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

    public CharactersAdapter(Context context, List<String[]> characters) {
        this.mCharacters = characters;
        this.mContext = context;
        this.mGojuon = Gojuon.getInstance();
        this.mSharedPreferences = mGojuon.getSharedPreferences();
    }

    @Override
    public int getCount() {
        return mCharacters.size();
    }

    @Override
    public String[] getItem(int i) {
        return mCharacters.get(i);
    }

    @Override
    public boolean isEnabled(int position) {
        String roumaji = mCharacters.get(position)[Characters.INDEX_ROUMAJI];
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


    public void fillCharacters(View view, int i) {
        fillCharacters(Holder.get(view), i);
    }
}
