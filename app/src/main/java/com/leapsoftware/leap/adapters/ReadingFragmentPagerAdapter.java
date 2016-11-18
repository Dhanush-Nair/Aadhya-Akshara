package com.leapsoftware.leap.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.exercises.ReadingExercise;
import com.leapsoftware.leap.ui.ReadingEnglishFragment;
import com.leapsoftware.leap.ui.ReadingTranslatedFragment;

/**
 * Created by vincentrickey on 1/31/16.
 */
public class ReadingFragmentPagerAdapter extends FragmentPagerAdapter {
    public Context mContext;
    public ReadingExercise mReadingExercise;

    public ReadingFragmentPagerAdapter(FragmentManager fragmentManager, Context context, ReadingExercise readingExercise) {
        super(fragmentManager);
        this.mContext = context;
        this.mReadingExercise = readingExercise;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ReadingEnglishFragment readingEnglishFragment = ReadingEnglishFragment.newInstance(mReadingExercise);
                return readingEnglishFragment;
            case 1:
                ReadingTranslatedFragment readingTranslatedFragment = ReadingTranslatedFragment.newInstance(mReadingExercise);
                return readingTranslatedFragment;
        }
        return null; // Placeholder in case we do not return case 0 or 1.
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.reading_fragment_tab_english_dialog_title);
            case 1:
                return mContext.getString(R.string.reading_fragment_tab_translated_dialog_title);
        }
        return null;
    }
}

