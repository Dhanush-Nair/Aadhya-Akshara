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
public class VocabWordExerciseItem extends ExerciseItem implements Completable, Parcelable {
    protected String mVocabWord;
    protected String mTranslatedVocabWord;
    protected String mExerciseName;
    protected String mLessonName;
    protected String mItemKey;
    protected boolean mIsItemComplete;
    public Boolean mIsFrontShowing; // Used to keep track of flashcard flipped state

    public VocabWordExerciseItem(String vocabWord, String vocabTranslation, String exerciseName, String lessonName) {
        this.mVocabWord = vocabWord;
        this.mTranslatedVocabWord = vocabTranslation;
        this.mExerciseName = exerciseName;
        this.mLessonName = lessonName;
        this.mItemKey = lessonName + exerciseName + vocabWord;
        mIsFrontShowing = true;
    }

    protected VocabWordExerciseItem(Parcel in) {
        mVocabWord = in.readString();
        mTranslatedVocabWord = in.readString();
        mExerciseName = in.readString();
        mLessonName = in.readString();
        mItemKey = in.readString();
        mIsFrontShowing = in.readByte() != 0;
    }

    public static final Creator<VocabWordExerciseItem> CREATOR = new Creator<VocabWordExerciseItem>() {
        @Override
        public VocabWordExerciseItem createFromParcel(Parcel in) {
            return new VocabWordExerciseItem(in);
        }

        @Override
        public VocabWordExerciseItem[] newArray(int size) {
            return new VocabWordExerciseItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mVocabWord);
        dest.writeString(mTranslatedVocabWord);
        dest.writeString(mExerciseName);
        dest.writeString(mLessonName);
        dest.writeString(mItemKey);
        dest.writeByte((byte) (mIsFrontShowing ? 1 : 0));
    }

    public String getVocabWord() {
        return mVocabWord;
    }

    public String getTranslatedVocabWord() {
        return mTranslatedVocabWord;
    }

    public String getItemKey() {
        return mItemKey;
    }

    public boolean isFrontShowing() {
        return mIsFrontShowing;
    }

    public void setIsItemComplete(boolean isItemComplete, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.KEY_SHAREDPREFERENCES_PROGRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getItemKey(), isItemComplete);
        editor.commit();
    }

    @Override
    public boolean isComplete(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.KEY_SHAREDPREFERENCES_PROGRESS, Context.MODE_PRIVATE);
        mIsItemComplete = sharedPref.getBoolean(getItemKey(), false); // false set as default value if isComplete has not been set
        return mIsItemComplete;
    }

}
