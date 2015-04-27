package com.gracecode.android.gojuon.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.gracecode.android.gojuon.Characters;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.common.Gojuon;
import com.gracecode.android.gojuon.ui.fragment.CharactersFragment;

import java.util.HashMap;

public class CharactersFragmentAdapter extends FragmentPagerAdapter  {
    protected static final int[] TITLES = new int[]{
            R.string.monographs,
            R.string.monographs_with_diacritics,
            R.string.digraphs,
            R.string.digraphs_with_diacritics
    };
    protected static final HashMap<Integer, CharactersFragment> mFragments =
            new HashMap<Integer, CharactersFragment>();

    private final Context mContext;
    private final SharedPreferences mSharedPreferences;

    public CharactersFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
        this.mSharedPreferences = Gojuon.getInstance().getSharedPreferences();
    }

    @Override
    public Fragment getItem(int position) {
        final CharactersFragment fragment;

        if (mFragments.containsKey(position)) {
            fragment = mFragments.get(position);
        } else {
            fragment = new CharactersFragment();
            switch (TITLES[position]) {
                case R.string.digraphs:
                    fragment.setCharactersAndColumns(Characters.DIGRAPHS, 3);
                    break;

                case R.string.monographs_with_diacritics:
                    fragment.setCharacters(Characters.MONOGRAPHS_WITH_DIACRITICS);
                    break;

                case R.string.digraphs_with_diacritics:
                    fragment.setCharactersAndColumns(Characters.DIGRAPHS_WITH_DIACRITICS, 3);
                    break;

                default:
                    fragment.setCharacters(Characters.MONOGRAPHS);
            }

            mFragments.put(position, fragment);
        }

        return fragment;
    }


    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(TITLES[position]);
    }

    public CharactersFragment getFragmentByTitle(int monographs) {
        int position = getTitlePosition(monographs);
        if (position != -1) {
            return (CharactersFragment) getItem(position);
        }

        return null;
    }

    private int getTitlePosition(int monographs) {
        for (int i = 0; i < TITLES.length; i++) {
            if (monographs == TITLES[i]) {
                return i;
            }
        }

        return -1;
    }

//    public void startSlide(int item) {
//        CharactersFragment fragment = mFragments.get(item);
//        fragment.startSlide();
//    }
}
