package com.leapsoftware.leap.exerciseItems;

import android.content.Context;
import android.util.Log;

import com.leapsoftware.leap.utils.Completable;

/**
 * Created by vincentrickey on 2/24/16.
 */
public class ExerciseItem implements Completable {

    @Override
    public boolean isComplete(Context context) {
        // default value of isComplete is false
        Log.d("ExerciseItem class", "isComplete() = false");
        return false;
    }
}
