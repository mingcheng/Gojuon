package com.gracecode.android.gojuon.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.gracecode.android.gojuon.service.PronounceService;

public class CharactersFragment extends Fragment {
    private static final int DEFAULT_COLUMN_NUM = 5;
    private static final String STAT_COLUMNS = "stat_colums";
    private static final String STAT_CHARACTERS = "stat_characters";

    private String[][] mCharacters;
    private Gojuon mGojuonApp;
    private SharedPreferences mSharedPreferences;
    private int mColumns = DEFAULT_COLUMN_NUM;
    private GridView mGridView;
    private CharactersAdapter mCharactersAdapter;

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(PronounceService.PLAY_PRONOUNCE_NAME);
            try {
                intent.putExtra(PronounceService.EXTRA_ROUMAJI, mCharacters[i][Characters.INDEX_ROUMAJI]);
                getActivity().sendBroadcast(intent);


                if (mSharedPreferences.getBoolean(Gojuon.KEY_AUTO_ROTATE, false)) {
                    View layout = getActivity().findViewById(R.id.layout_item_character);
                    mCharactersAdapter.fillCharacters(layout, i);
                }

                // Mark as selected.
                if (mSharedPreferences.getBoolean(Gojuon.KEY_HIGHLIGHT_SELECTED, true)) {
                    view.setSelected(true);
                }

            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    };

    public CharactersFragment() {

    }

    public CharactersFragment(String[][] characters) {
        this.mCharacters = characters;
    }

    public CharactersFragment(String[][] characters, int columns) {
        this.mCharacters = characters;
        this.mColumns = columns;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mColumns = savedInstanceState.getInt(STAT_COLUMNS, DEFAULT_COLUMN_NUM);
            mCharacters = (String[][]) savedInstanceState.getSerializable(STAT_CHARACTERS);
        }

        mGojuonApp = Gojuon.getInstance();
        mSharedPreferences = mGojuonApp.getSharedPreferences();
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

    @Override
    public void onResume() {
        super.onResume();
        if (mCharacters != null && mCharacters.length > 0) {
            mCharactersAdapter = new CharactersAdapter(getActivity(), mCharacters);
            mGridView.setAdapter(mCharactersAdapter);
            mGridView.setOnItemClickListener(mOnItemClickListener);

        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    mGridView.requestFocusFromTouch();


                    break;
            }
        }
    };

    public void startSlide() {


//        Timer timer = new Timer();
//
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    mHandler.sendEmptyMessage(0);
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 500, mCharacters.length);

//        timer.cancel();
    }

    public void stopSlide() {


    }
}
