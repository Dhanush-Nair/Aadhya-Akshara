package com.leapsoftware.leap.exerciseItems;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.leapsoftware.leap.utils.Completable;
import com.leapsoftware.leap.utils.Constants;

/**
 * Created by vincentrickey on 2/5/16.
 */
public class ReadingExerciseItem extends ExerciseItem implements Parcelable, Completable {
    protected String mDialog;
    protected String mTranslatedDialog;
    protected boolean mIsItemComplete;
    protected String mExerciseName;
    protected String mLessonName;
    protected String mItemKey;

    public ReadingExerciseItem(String dialog, String translatedDialog, String exerciseName, String lessonName) {
        this.mDialog = dialog;
        this.mTranslatedDialog = translatedDialog;
        this.mExerciseName = exerciseName;
        this.mLessonName = lessonName;
        this.mItemKey = lessonName + exerciseName + dialog;
    }

    protected ReadingExerciseItem(Parcel in) {
        mDialog = in.readString();
        mTranslatedDialog = in.readString();
        mExerciseName = in.readString();
        mLessonName = in.readString();
        mItemKey = in.readString();
    }

    public static final Creator<ReadingExerciseItem> CREATOR = new Creator<ReadingExerciseItem>() {
        @Override
        public ReadingExerciseItem createFromParcel(Parcel in) {
            return new ReadingExerciseItem(in);
        }

        @Override
        public ReadingExerciseItem[] newArray(int size) {
            return new ReadingExerciseItem[size];
        }
    };

    public String getDialog() {
        return mDialog;
    }

    public void setDialog(String dialog) {
        mDialog = dialog;
    }

    public String getTranslatedDialog() {
        return mTranslatedDialog;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDialog);
        dest.writeString(mTranslatedDialog);
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
