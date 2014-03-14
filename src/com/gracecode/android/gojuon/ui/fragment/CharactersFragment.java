package com.gracecode.android.gojuon.ui.fragment;

import android.content.Intent;
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
import com.gracecode.android.gojuon.service.PronounceService;

public class CharactersFragment extends Fragment {
    private static final int DEFAULT_COLUMN_NUM = 5;
    private final String[][] mCharacters;
    private final int mColumns;
    private GridView mGridView;

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(PronounceService.PLAY_PRONOUNCE_NAME);
            intent.putExtra(PronounceService.EXTRA_ROUMAJI, mCharacters[i][Characters.INDEX_ROUMAJI]);
            getActivity().sendBroadcast(intent);
        }
    };


    public CharactersFragment(String[][] characters) {
        this.mCharacters = characters;
        this.mColumns = DEFAULT_COLUMN_NUM;
    }

    public CharactersFragment(String[][] characters, int columns) {
        this.mCharacters = characters;
        this.mColumns = columns;
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
    }

    @Override
    public void onResume() {
        super.onResume();
        mGridView.setAdapter(new CharactersAdapter(getActivity(), mCharacters));
        mGridView.setOnItemClickListener(mOnItemClickListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
