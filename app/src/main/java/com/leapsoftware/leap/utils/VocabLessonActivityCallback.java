package com.leapsoftware.leap.utils;

import android.support.v4.app.Fragment;

/**
 * Created by vincentrickey on 1/31/16.
 */
public interface VocabLessonActivityCallback {
    public Fragment exchangeFragment(int position);
    public void sendNextVocabQuestion(int additionalItem);
    public void sendNextPronunciationQuestion(int additionalItem);
}
