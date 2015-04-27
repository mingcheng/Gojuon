package com.gracecode.android.gojuon.ui.dialog;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.gracecode.android.gojuon.Characters;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.adapter.CharactersAdapter;
import com.gracecode.android.gojuon.common.Gojuon;
import com.gracecode.android.gojuon.ui.widget.StrokeView;

import java.io.IOException;
import java.io.InputStream;

public class StrokeDialog extends BaseDialog {
    @InjectView(R.id.img_character)
    ImageView mImageCharacter;

    @InjectView(R.id.img_stroke)
    ImageView mImageStroke;

    @InjectView(R.id.stroke_view)
    StrokeView mStrokeView;

    private String[] mCharacter;

    private StrokeView.OnStockListener mOnStockListener = new StrokeView.OnStockListener() {
        @Override
        public void onStockStart(View view) {

        }

        @Override
        public void OnStock(View view) {

        }

        @Override
        public void onStockFinish(View view) {
            if (getCharacter() != null) {
                Gojuon.pronounce(getActivity(), getCharacter()[Characters.INDEX_ROUMAJI]);
            }
        }
    };

    public StrokeDialog() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // request a window without the title
        // @see https://stackoverflow.com/questions/15277460/how-to-create-a-dialogfragment-without-title
        View view = inflater.inflate(R.layout.fragment_stroke, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mStrokeView.setOnStockListener(mOnStockListener);

        // 显示内容
        if (getCharacter() != null) {
            try {
                int position = getCharacterPosition();
                String strokeResourceName = getStrokeResourceNameByPosition(position);
                String characterResourceName = getCharacterResourceNameByPosition(position);
                mImageStroke.setImageDrawable(getDrawableFromAssets(strokeResourceName));
                mImageCharacter.setImageDrawable(getDrawableFromAssets(characterResourceName));
            } catch (IOException e) {
                dismiss();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

    /**
     * 设置需要显示的字符
     *
     * @param character
     */
    public void setCharacter(String[] character) {
        mCharacter = character;
    }

    public String[] getCharacter() {
        return mCharacter;
    }

    private Drawable getDrawableFromAssets(String path) throws IOException {
        InputStream inputStream = getActivity().getAssets().open(path);
        return Drawable.createFromStream(inputStream, null);
    }


    private String getStrokeResourceNameByPosition(int position) {
        return String.format("stroke/%s%sstroke.png",
                isShowKatakana() ? "k" : "h", getResourceNameByPosition(position));
    }

    private String getCharacterResourceNameByPosition(int position) {
        return String.format("stroke/%s%s.png",
                isShowKatakana() ? "k" : "h", getResourceNameByPosition(position));
    }


    private String getResourceNameByPosition(int position) {
        return String.format("%s%d", (position / 5 == 0) ? "" : (position / 5) + "", position % 5);
    }

    public boolean isShowKatakana() {
        return getPreferencedShowType().equals(CharactersAdapter.TYPE_SHOW_CHARACTER_KATAGANA);
    }

    /**
     * 显示平假名还是片假名
     *
     * @return
     */
    public String getPreferencedShowType() {
        return mSharedPreferences.getString(Gojuon.KEY_SHOW_CHARACTER_TYPE, CharactersAdapter.TYPE_SHOW_CHARACTER_HIRAGANA);
    }

    public void setStrokeDrawable(Drawable drawable) {
        mImageStroke.setImageDrawable(drawable);
    }

    public void setCharacterDrawable(Drawable drawable) {
        mImageCharacter.setImageDrawable(drawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mImageCharacter.setImageAlpha((int) (255 * .25));
        }
    }

    /**
     * 获取当前字符在对应数组的位置
     *
     * @return
     */
    public int getCharacterPosition() {
        String[] character = getCharacter();
        for (int i = 0, size = Characters.MONOGRAPHS.length; i < size; i++) {
            if (Characters.MONOGRAPHS[i][Characters.INDEX_ROUMAJI].equals(character[Characters.INDEX_ROUMAJI])) {
                return i;
            }
        }

        return 0;
    }
}
