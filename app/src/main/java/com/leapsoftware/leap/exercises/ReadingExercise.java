package com.leapsoftware.leap.exercises;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.leapsoftware.leap.exerciseItems.ReadingExerciseItem;
import com.leapsoftware.leap.utils.Attempt;
import com.leapsoftware.leap.utils.Completable;
import com.leapsoftware.leap.utils.Constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by vincentrickey on 3/18/16.
 */
public class ReadingExercise extends Exercise implements Completable, Parcelable {
    private ArrayList<ReadingExerciseItem> mReadingExerciseItemArrayList;
    private String mExerciseName;
    private String mExerciseNameTranslated;
    private String mLessonName;
    private String mExerciseKey;
    protected Attempt mAttempt;
    private String mAttemptKey;


    public ReadingExercise(LinkedHashMap<String, String> vocabHashMap, LinkedHashMap<String, String> dialogHashMap, String exerciseName, String exerciseNameTranslated, String lessonName) {
        super(vocabHashMap, dialogHashMap, exerciseName, exerciseNameTranslated);
        mReadingExerciseItemArrayList = createReadingExerciseItemList(dialogHashMap, exerciseName, lessonName);
        this.mExerciseName = exerciseName;
        this.mExerciseNameTranslated = exerciseNameTranslated;
        this.mLessonName = lessonName;
        this.mExerciseKey = lessonName.replace("\"", "").replace(" ", "") + "_" + exerciseName.replace(" ", "");
        this.mAttempt = Attempt.FIRSTTRY; // Default to first try
        this.mAttemptKey = lessonName.replace("\"", "").replace(" ", "") + "_" + exerciseName.replace(" ", "") + "_" + "AttemptEnum";
    }

    protected ReadingExercise(Parcel in) {
        super(in);
        mReadingExerciseItemArrayList = in.createTypedArrayList(ReadingExerciseItem.CREATOR);
        this.mExerciseName = in.readString();
        this.mExerciseNameTranslated = in.readString();
        this.mLessonName = in.readString();
        this.mExerciseKey = in.readString();
        this.mAttempt = in.readParcelable(Attempt.class.getClassLoader());
        this.mAttemptKey = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(mReadingExerciseItemArrayList);
        dest.writeString(mExerciseName);
        dest.writeString(mExerciseNameTranslated);
        dest.writeString(mLessonName);
        dest.writeString(mExerciseKey);
        dest.writeParcelable(mAttempt, flags);
        dest.writeString(mAttemptKey);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReadingExercise> CREATOR = new Creator<ReadingExercise>() {
        @Override
        public ReadingExercise createFromParcel(Parcel in) {
            return new ReadingExercise(in);
        }

        @Override
        public ReadingExercise[] newArray(int size) {
            return new ReadingExercise[size];
        }
    };

    public ArrayList<ReadingExerciseItem> createReadingExerciseItemList(LinkedHashMap<String, String> dialogHashMap, String exerciseName, String lessonName) {
        ArrayList<ReadingExerciseItem> readingExerciseItemList = new ArrayList<>();

        for (LinkedHashMap.Entry<String, String> readingEntryItem : dialogHashMap.entrySet()) {
            String dialogItem = readingEntryItem.getKey();
            String translatedDialog = readingEntryItem.getValue();
            ReadingExerciseItem readingExerciseItem = new ReadingExerciseItem(dialogItem, translatedDialog, exerciseName, lessonName);
            readingExerciseItemList.add(readingExerciseItem);
        }
        return readingExerciseItemList;
    }

    public ArrayList<ReadingExerciseItem> getReadingExerciseItemArrayList() {
        return mReadingExerciseItemArrayList;
    }

    @Override
    public boolean isComplete(Context context) {
        // loop through every ExerciseItem to determine if it has been completed
        for (ReadingExerciseItem item: mReadingExerciseItemArrayList) {
            if (item.isComplete(context) == false) {
                return false;
            }
        }
        return true;
    }

    public String getExerciseNameEnglish() {
        return mExerciseName;
    }

    @Override
    public String getExerciseNameTranslated() {
        return mExerciseNameTranslated;
    }

    public Attempt getAttempt(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.KEY_SHAREDPREFERENCES_PROGRESS, Context.MODE_PRIVATE);
        String enumValue = preferences.getString(mAttemptKey, "FIRSTTRY");
        if (enumValue.equals(Attempt.FIRSTTRY.toString())) {
            mAttempt = Attempt.FIRSTTRY;
        } else {
            mAttempt = Attempt.STUDYING;
        }
        return mAttempt;
    }

    public void setAttempt(Attempt attempt, Context context) {
        mAttempt = attempt;
        SharedPreferences preferences = context.getSharedPreferences(Constants.KEY_SHAREDPREFERENCES_PROGRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(mAttemptKey, mAttempt.toString());
        editor.commit();
    }
}
