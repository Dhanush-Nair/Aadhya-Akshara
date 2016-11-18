package com.leapsoftware.leap.exerciseItems;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.leapsoftware.leap.utils.Completable;
import com.leapsoftware.leap.utils.Constants;

/**
 * Created by vincentrickey on 2/24/16.
 */
public class PronunciationExerciseItem extends ExerciseItem implements Completable, Parcelable {
    protected String mDialog;
    protected boolean mIsItemComplete;
    protected String mExerciseName;
    protected String mLessonName;
    protected  String mItemKey;

    public PronunciationExerciseItem(String dialog, String exerciseName, String lessonName) {
        this.mDialog = dialog;
        this.mExerciseName = exerciseName;
        this.mLessonName = lessonName;
        this.mItemKey = lessonName + exerciseName + dialog;
    }

    protected PronunciationExerciseItem(Parcel in) {
        mDialog = in.readString();
        mExerciseName = in.readString();
        mLessonName = in.readString();
        mItemKey = in.readString();
    }

    public static final Creator<PronunciationExerciseItem> CREATOR = new Creator<PronunciationExerciseItem>() {
        @Override
        public PronunciationExerciseItem createFromParcel(Parcel in) {
            return new PronunciationExerciseItem(in);
        }

        @Override
        public PronunciationExerciseItem[] newArray(int size) {
            return new PronunciationExerciseItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDialog);
        dest.writeString(mExerciseName);
        dest.writeString(mLessonName);
        dest.writeString(mItemKey);
    }

    public void setIsItemComplete(boolean isItemComplete, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.KEY_SHAREDPREFERENCES_PROGRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getItemKey(), isItemComplete);
        editor.commit();
    }

    public String getDialog() {
        return mDialog;
    }

    public String getItemKey() {
        return mItemKey;
    }

    @Override
    public boolean isComplete(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.KEY_SHAREDPREFERENCES_PROGRESS, Context.MODE_PRIVATE);
        mIsItemComplete = sharedPref.getBoolean(getItemKey(), false); // false set as default value if isComplete has not been set
        return mIsItemComplete;
    }
}
