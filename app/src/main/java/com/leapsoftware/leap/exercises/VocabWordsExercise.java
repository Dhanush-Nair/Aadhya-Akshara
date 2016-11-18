package com.leapsoftware.leap.exercises;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.leapsoftware.leap.exerciseItems.VocabWordExerciseItem;
import com.leapsoftware.leap.utils.Attempt;
import com.leapsoftware.leap.utils.Completable;
import com.leapsoftware.leap.utils.Constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by vincentrickey on 3/18/16.
 */
public class VocabWordsExercise extends Exercise implements Completable, Parcelable  {
    private ArrayList<VocabWordExerciseItem> mVocabWordExerciseItemArrayList;
    private String mExerciseName;
    private String mExerciseNameTranslated;
    private String mLessonName;
    private String mExerciseKey;
    private Attempt mAttempt;
    private String mAttemptKey;
    public static final String TAG = "VocabWordsEx Class";

    public VocabWordsExercise(LinkedHashMap<String, String> vocabHashMap, LinkedHashMap<String, String> dialogHashMap, String exerciseName, String exerciseNameTranslated, String lessonName) {
        super(vocabHashMap, dialogHashMap, exerciseName, exerciseNameTranslated);
        mVocabWordExerciseItemArrayList = createVocabWordExerciseItemArrayList(vocabHashMap, exerciseName, lessonName);
        mExerciseName = exerciseName;
        mExerciseNameTranslated = exerciseNameTranslated;
        mLessonName = lessonName;
        mExerciseKey = lessonName.replace("\"", "").replace(" ", "") + "_" + exerciseName.replace(" ", "");
        mAttempt = Attempt.FIRSTTRY; // Default set to FIRSTTRY
        mAttemptKey = lessonName.replace("\"", "").replace(" ", "") + "_" + exerciseName.replace(" ", "") + "_" + "AttemptEnum";
    }

    protected VocabWordsExercise(Parcel in) {
        super(in);
        mVocabWordExerciseItemArrayList = in.createTypedArrayList(VocabWordExerciseItem.CREATOR);
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
        dest.writeTypedList(mVocabWordExerciseItemArrayList);
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

    public static final Creator<VocabWordsExercise> CREATOR = new Creator<VocabWordsExercise>() {
        @Override
        public VocabWordsExercise createFromParcel(Parcel in) {
            return new VocabWordsExercise(in);
        }

        @Override
        public VocabWordsExercise[] newArray(int size) {
            return new VocabWordsExercise[size];
        }
    };

    public ArrayList<VocabWordExerciseItem> createVocabWordExerciseItemArrayList(LinkedHashMap<String, String> vocabHashMap, String exerciseName, String lessonName) {
        ArrayList<VocabWordExerciseItem> vocabWordExerciseItemArrayList = new ArrayList<>();

        for (LinkedHashMap.Entry<String, String> vocabEntryItem : vocabHashMap.entrySet()) {
            String vocabWord = vocabEntryItem.getKey();
            String vocabTranslation = vocabEntryItem.getValue();
            VocabWordExerciseItem vocabWordExerciseItem = new VocabWordExerciseItem(vocabWord, vocabTranslation, exerciseName, lessonName);
            vocabWordExerciseItemArrayList.add(vocabWordExerciseItem);
        }
        return vocabWordExerciseItemArrayList;
    }

    public ArrayList<VocabWordExerciseItem> getVocabWordExerciseItemArrayList() {
        return mVocabWordExerciseItemArrayList;
    }

    @Override
    public boolean isComplete(Context context) {
        // loop through every ExerciseItem to determine if it has been completed
        for (VocabWordExerciseItem item: mVocabWordExerciseItemArrayList) {
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
