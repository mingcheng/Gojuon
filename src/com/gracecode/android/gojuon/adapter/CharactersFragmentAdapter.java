package com.gracecode.android.gojuon.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.gracecode.android.gojuon.Characters;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.ui.fragment.CharactersFragment;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.HashMap;

public class CharactersFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    protected static final int[] TITLES = new int[]{
            R.string.monographs,
            R.string.digraphs,
            R.string.monographs_with_diacritics,
            R.string.digraphs_with_diacritics
    };
    protected static final HashMap<Integer, Fragment> mFragments = new HashMap<Integer, Fragment>();

    private final Context mContext;

    public CharactersFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (mFragments.containsKey(position)) {
            return mFragments.get(position);
        }

        Fragment fragment;
        switch (TITLES[position]) {
            case R.string.digraphs:
                fragment = new CharactersFragment(Characters.DIGRAPHS, 3);
                break;

            case R.string.monographs_with_diacritics:
                fragment = new CharactersFragment(Characters.MONOGRAPHS_WITH_DIACRITICS);
                break;

            case R.string.digraphs_with_diacritics:
                fragment = new CharactersFragment(Characters.DIGRAPHS_WITH_DIACRITICS, 3);
                break;

            default:
                fragment = new CharactersFragment(Characters.MONOGRAPHS);
        }

        mFragments.put(position, fragment);
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

    @Override
    public int getIconResId(int index) {
        return android.R.color.transparent;
    }
}
