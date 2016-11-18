package com.leapsoftware.leap.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.leapsoftware.leap.exercises.PronunciationExercise;
import com.leapsoftware.leap.ui.PronunciationNestedFragment;

/**
 * Created by vincentrickey on 2/2/16.
 */
public class PronunciationFragmentPagerAdapter extends FragmentPagerAdapter {
    public Context mContext;
    public PronunciationExercise mPronunciationExercise;

    public PronunciationFragmentPagerAdapter(FragmentManager fragmentManager, Context context, PronunciationExercise pronunciationExercise) {
        super(fragmentManager);
        this.mContext = context;
        this.mPronunciationExercise = pronunciationExercise;
    }

    @Override
    public Fragment getItem(int position) {
        PronunciationNestedFragment pronunciationNestedFragment = PronunciationNestedFragment.newInstance(mPronunciationExercise, position);
        return pronunciationNestedFragment;

    }

    @Override
    public int getCount() {
        return mPronunciationExercise.getPronunciationExerciseItemArrayList().size();
    }
}
