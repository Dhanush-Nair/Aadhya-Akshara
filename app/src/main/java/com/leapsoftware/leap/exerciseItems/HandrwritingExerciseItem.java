package com.leapsoftware.leap.exerciseItems;

import android.content.Context;

import com.leapsoftware.leap.dataObject.Letter;
import com.leapsoftware.leap.utils.Completable;

/**
 * Created by vincentrickey on 2/11/16.
 */
public class HandrwritingExerciseItem implements Completable {
    protected Letter mLetterSelected;

    public HandrwritingExerciseItem(Letter letterSelected) {
        this.mLetterSelected = letterSelected;
    }

    public Letter getLetterSelected() {
        return mLetterSelected;
    }

    public void setLetterSelected(Letter letterSelected) {
        mLetterSelected = letterSelected;
    }

    @Override
    public boolean isComplete(Context context) {
        return false;
    }
}
