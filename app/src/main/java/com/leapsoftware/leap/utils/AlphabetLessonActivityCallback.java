package com.leapsoftware.leap.utils;

import android.support.v4.app.Fragment;

import com.leapsoftware.leap.exerciseItems.HandrwritingExerciseItem;
import com.leapsoftware.leap.dataObject.Letter;

/**
 * Created by vincentrickey on 2/11/16.
 */
public interface AlphabetLessonActivityCallback {
    public Fragment exchangeFragment(int position);
    public HandrwritingExerciseItem createHandwritingExerciseItem(Letter letter);
}
