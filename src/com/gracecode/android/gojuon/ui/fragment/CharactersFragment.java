package com.gracecode.android.gojuon.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.gracecode.android.gojuon.Characters;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.adapter.CharactersAdapter;
import com.gracecode.android.gojuon.common.Gojuon;
import com.gracecode.android.gojuon.ui.dialog.StrokeDialog;

import java.util.Arrays;

public class CharactersFragment extends Fragment {
    private static final int DEFAULT_COLUMN_NUM = 5;
    private static final String STAT_COLUMNS = "stat_colums";
    private static final String STAT_CHARACTERS = "stat_characters";
    private static final String STROKE_DIALOG_TAG = "stroke_dialog_tag";

    private String[][] mCharacters;
    private SharedPreferences mSharedPreferences;
    private int mColumns = DEFAULT_COLUMN_NUM;
    private GridView mGridView;
    private CharactersAdapter mCharactersAdapter;
    private StrokeDialog mStrokeDialog;

    /**
     * 点击每个字符触发的操作
     */
    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            try {
                // Pronounce the character
                Gojuon.pronounce(getActivity(), mCharacters[i][Characters.INDEX_ROUMAJI]);

                // Mark as selected.
                if (mSharedPreferences.getBoolean(Gojuon.KEY_HIGHLIGHT_SELECTED, true)) {
                    view.setSelected(true);
                }

            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 长按事件，弹出对话框
     */
    AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            mStrokeDialog.setCharacter(mCharacters[i]);
            mStrokeDialog.show(getFragmentManager(), STROKE_DIALOG_TAG);
            return false;
        }
    };

    public CharactersFragment() {
        super();
    }

    public void setCharactersAndColumns(String[][] characters, int columns) {
        setCharacters(characters);
        setColumns(columns);
    }

    public void setCharacters(String[][] mCharacters) {
        this.mCharacters = mCharacters;
    }

    public String[][] getCharacters() {
        return mCharacters;
    }

    public void setColumns(int mColumns) {
        this.mColumns = mColumns;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mColumns = savedInstanceState.getInt(STAT_COLUMNS, DEFAULT_COLUMN_NUM);
            mCharacters = (String[][]) savedInstanceState.getSerializable(STAT_CHARACTERS);
        }

        mSharedPreferences = Gojuon.getInstance().getSharedPreferences();
        mStrokeDialog = new StrokeDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_characters, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGridView = (GridView) getView().findViewById(R.id.list);
        mGridView.setNumColumns(mColumns);
        mGridView.setSoundEffectsEnabled(false);
        mGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STAT_COLUMNS, mColumns);
        outState.putSerializable(STAT_CHARACTERS, mCharacters);
    }

    public String getPreferenceShowType() {
        return mSharedPreferences.getString(Gojuon.KEY_SHOW_CHARACTER_TYPE, CharactersAdapter.TYPE_SHOW_CHARACTER_HIRAGANA);
    }

    public CharactersAdapter getCharactersAdapter() {
        return mCharactersAdapter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCharacters != null && mCharacters.length > 0) {

            mCharactersAdapter = new CharactersAdapter(getActivity(), Arrays.asList(mCharacters));

            // 根据配置项设置显示类型
            mCharactersAdapter.setShowType(getPreferenceShowType());

            mGridView.setAdapter(mCharactersAdapter);
            mGridView.setOnItemClickListener(mOnItemClickListener);

            // 如果是平假名，则长按可以显示笔画对话框
            if (getCharacters() == Characters.MONOGRAPHS) {
                mGridView.setOnItemLongClickListener(mOnItemLongClickListener);
            }
        }
    }
}
