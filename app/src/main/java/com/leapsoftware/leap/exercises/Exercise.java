package com.leapsoftware.leap.exercises;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.leapsoftware.leap.utils.Completable;

import java.util.LinkedHashMap;

/**
 * Created by vincentrickey on 2/24/16.
 */
public class Exercise implements Completable, Parcelable {
    public LinkedHashMap<String, String> mVocabHashMap;
    public LinkedHashMap<String, String> mDialogHashMap;
    public String mExerciseNameEnglish;
    public String mExerciseNameTranslated;

    // TODO: add an assert method to always instantiate subclass or create Abstract Class

    public Exercise(LinkedHashMap<String, String> vocabHashMap, LinkedHashMap<String, String> dialogHashMap, String exerciseNameEnglish, String exerciseNameTranslated) {
        this.mVocabHashMap = vocabHashMap;
        this.mDialogHashMap = dialogHashMap;
        this.mExerciseNameEnglish = exerciseNameEnglish;
    }

    protected Exercise(Parcel in) {
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public boolean isComplete(Context context) {
        // default value of isComplete is false
        Log.d("Exercise class", "isComplete = false");
        return false;
    }

    public String getExerciseNameEnglish() {
        return mExerciseNameEnglish;
    }

    public String getExerciseNameTranslated() {
        return mExerciseNameTranslated;
    }
}
