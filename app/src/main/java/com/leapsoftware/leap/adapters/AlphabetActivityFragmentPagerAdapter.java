package com.leapsoftware.leap.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.dataObject.Letter;
import com.leapsoftware.leap.ui.AlphabetCardFragment;
import com.leapsoftware.leap.ui.AlphabetHandwritingChoicesFragment;
import com.leapsoftware.leap.ui.AlphabetListFragment;

import java.util.ArrayList;

/**
 * Created by vincentrickey on 1/23/16.
 */
public class AlphabetActivityFragmentPagerAdapter extends FragmentPagerAdapter {
    public ArrayList<Letter> mLetterArrayList;
    private AlphabetHandwritingChoicesFragment mAlphabetHandwritingChoicesFragment;
    private AlphabetListFragment mAlphabetListFragment;
    private AlphabetCardFragment mAlphabetCardFragment;
    private Context mContext;

    public AlphabetActivityFragmentPagerAdapter(FragmentManager fragmentManager, ArrayList<Letter> letterArrayList, Context context) {
        super(fragmentManager);
        this.mLetterArrayList = letterArrayList;
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (mAlphabetHandwritingChoicesFragment == null) {
                    mAlphabetHandwritingChoicesFragment = AlphabetHandwritingChoicesFragment.newInstance(mLetterArrayList);
                }
                return mAlphabetHandwritingChoicesFragment;
            case 1:
                if (mAlphabetListFragment == null) {
                    mAlphabetListFragment = AlphabetListFragment.newInstance(mLetterArrayList);
                }
                return mAlphabetListFragment;
            case 2:
                if (mAlphabetCardFragment == null) {
                    mAlphabetCardFragment = AlphabetCardFragment.newInstance(mLetterArrayList);
                }
                return mAlphabetCardFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.alphabet_handwriting_choices_fragment_title);
            case 1:
                return mContext.getString(R.string.alphabet_list_fragment_title);
        }
        return null;
    }
}
