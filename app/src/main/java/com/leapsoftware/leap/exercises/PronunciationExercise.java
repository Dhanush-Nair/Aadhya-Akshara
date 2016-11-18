package com.leapsoftware.leap.exercises;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.leapsoftware.leap.exerciseItems.PronunciationExerciseItem;
import com.leapsoftware.leap.utils.Attempt;
import com.leapsoftware.leap.utils.Completable;
import com.leapsoftware.leap.utils.Constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by vincentrickey on 3/18/16.
 */
public class PronunciationExercise extends Exercise implements Completable, Parcelable {
    private ArrayList<PronunciationExerciseItem> mPronunciationExerciseItemArrayList;
    private String mExerciseName;
    private String mExerciseNameTranslated;
    private String mLessonName;
    private String mExerciseKey;
    private Attempt mAttempt;
    private String mAttemptKey;

    public PronunciationExercise(LinkedHashMap<String, String> vocabHashMap, LinkedHashMap<String, String> dialogHashMap, String exerciseName, String exerciseNameTranslated, String lessonName) {
        super(vocabHashMap, dialogHashMap, exerciseName, exerciseNameTranslated);
        mPronunciationExerciseItemArrayList = createPronunciationExerciseItemArrrayList(dialogHashMap, exerciseName, lessonName);
        mExerciseName = exerciseName;
        mExerciseNameTranslated = exerciseNameTranslated;
        mLessonName = lessonName;
        mExerciseKey = lessonName.replace("\"", "").replace(" ", "") + "_" + exerciseName.replace(" ", "");
        mAttempt = Attempt.FIRSTTRY; // Default set to FIRSTTRY
        mAttemptKey = lessonName.replace("\"", "").replace(" ", "") + "_" + exerciseName.replace(" ", "") + "_" + "AttemptEnum";
    }

    protected PronunciationExercise(Parcel in) {
        super(in);
        mPronunciationExerciseItemArrayList = in.createTypedArrayList(PronunciationExerciseItem.CREATOR);
        this.mExerciseName = in.readString();
        this.mExerciseNameTranslated = in.readString();
        this.mLessonName = in.readString();
        this.mExerciseKey = in.readString();
        this.mAttemptKey = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(mPronunciationExerciseItemArrayList);
        dest.writeString(mExerciseName);
        dest.writeString(mExerciseNameTranslated);
        dest.writeString(mLessonName);
        dest.writeString(mExerciseKey);
        dest.writeString(mAttemptKey);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PronunciationExercise> CREATOR = new Creator<PronunciationExercise>() {
        @Override
        public PronunciationExercise createFromParcel(Parcel in) {
            return new PronunciationExercise(in);
        }

        @Override
        public PronunciationExercise[] newArray(int size) {
            return new PronunciationExercise[size];
        }
    };

    public ArrayList<PronunciationExerciseItem> createPronunciationExerciseItemArrrayList(LinkedHashMap<String, String> vocabHashMap, String exerciseName, String lessonName) {
        ArrayList<PronunciationExerciseItem> pronunciationExerciseItemArrayList = new ArrayList<>();

        for (LinkedHashMap.Entry<String, String> pronunciationEntryItem : vocabHashMap.entrySet()) {
            String vocabWord = pronunciationEntryItem.getKey();
            PronunciationExerciseItem pronunciationExerciseItem = new PronunciationExerciseItem(vocabWord, exerciseName, lessonName);
            pronunciationExerciseItemArrayList.add(pronunciationExerciseItem);
        }
        return pronunciationExerciseItemArrayList;
    }

    public ArrayList<PronunciationExerciseItem> getPronunciationExerciseItemArrayList() {
        return mPronunciationExerciseItemArrayList;
    }

    @Override
    public boolean isComplete(Context context) {
        // loop through every ExerciseItem to determine if it has been completed
        for (PronunciationExerciseItem item: mPronunciationExerciseItemArrayList) {
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