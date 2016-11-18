package com.leapsoftware.leap.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.exercises.VocabWordsExercise;
import com.leapsoftware.leap.ui.VocabWordsCardsFragment;
import com.leapsoftware.leap.ui.VocabWordsListFragment;

/**
 * Created by vincentrickey on 2/1/16.
 */
public class VocabWordsFragmentPagerAdapter extends FragmentPagerAdapter {
    public Context mContext;
    public VocabWordsExercise mVocabWordExercise;

    private VocabWordsListFragment vocabWordsListFragment;
    private VocabWordsCardsFragment vocabWordsCardsFragment ;

    public VocabWordsFragmentPagerAdapter(FragmentManager fragmentManager, Context context, VocabWordsExercise vocabWordExercise) {
        super(fragmentManager);
        this.mContext = context;
        this.mVocabWordExercise = vocabWordExercise;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (vocabWordsListFragment == null) {
                    vocabWordsListFragment = VocabWordsListFragment.newInstance(mVocabWordExercise);
                }
                return vocabWordsListFragment;
            case 1:
                if (vocabWordsCardsFragment == null) {
                    vocabWordsCardsFragment = VocabWordsCardsFragment.newInstance(mVocabWordExercise);
                }
                return vocabWordsCardsFragment;
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
                return mContext.getString(R.string.vocabWordsFragment_tab_list_title);
            case 1:
                return mContext.getString(R.string.vocabWordsFragment_tab_flashcards_title);
        }
        return null;
    }
}
