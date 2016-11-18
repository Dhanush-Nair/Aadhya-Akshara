package com.leapsoftware.leap.exercises;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.leapsoftware.leap.exerciseItems.VocabWordExerciseItem;
import com.leapsoftware.leap.exerciseItems.VocabWordQuizExerciseItem;
import com.leapsoftware.leap.utils.Attempt;
import com.leapsoftware.leap.utils.Constants;
import com.leapsoftware.leap.utils.Scorable;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by vincentrickey on 3/6/16.
 */
public class QuizExercise extends Exercise implements Scorable, Parcelable {
    protected ArrayList<VocabWordQuizExerciseItem> mVocabWordQuizExerciseItemArrayList;
    private String mExerciseName;
    private String mExerciseNameTranslated;
    private String mLessonName;
    private String mExerciseKey;
    private Attempt mAttempt;
    private String mAttemptKey;

    public QuizExercise(LinkedHashMap<String, String> vocabHashMap, LinkedHashMap<String, String> dialogHashMap, String exerciseName, String exerciseNameTranslated, String lessonName) {
        super(vocabHashMap, dialogHashMap, exerciseName, exerciseNameTranslated);
        this.mVocabWordQuizExerciseItemArrayList = createVocabWordQuizExerciseItemArrayList(vocabHashMap, exerciseName, lessonName);
        this.mExerciseName = exerciseName;
        this.mExerciseNameTranslated = exerciseNameTranslated;
        this.mLessonName = lessonName;
        this.mExerciseKey = lessonName.replace("\"", "").replace(" ", "") + "_" + exerciseName.replace(" ", "");
        this.mAttempt = Attempt.FIRSTTRY;
        this.mAttemptKey = lessonName.replace("\"", "").replace(" ", "") + "_" + exerciseName.replace(" ", "") + "_" + "AttemptEnum";
    }

    protected QuizExercise(Parcel in) {
        super(in);
        mVocabWordQuizExerciseItemArrayList = in.createTypedArrayList(VocabWordQuizExerciseItem.CREATOR);
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
        dest.writeTypedList(mVocabWordQuizExerciseItemArrayList);
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

    public static final Creator<QuizExercise> CREATOR = new Creator<QuizExercise>() {
        @Override
        public QuizExercise createFromParcel(Parcel in) {
            return new QuizExercise(in);
        }

        @Override
        public QuizExercise[] newArray(int size) {
            return new QuizExercise[size];
        }
    };

    @Override
    public float calculateScore() {
        int numberCorrect = 0;
        float score;
        for (VocabWordQuizExerciseItem item : mVocabWordQuizExerciseItemArrayList) {
            if (item.isCorrect() && item.getAttempt().equals(Attempt.FIRSTTRY) || item.getAttempt().equals(Attempt.TYPO)) {
                numberCorrect++;
            }
        }
        score = (float) numberCorrect/mVocabWordQuizExerciseItemArrayList.size() * 100;
        return Math.round(score);
    }

    public void saveHighScore(float score, Context context) {
        if (score > getSavedHighScore(context)) {
            SharedPreferences sharedPref = context.getSharedPreferences(Constants.KEY_SHAREDPREFERENCES_PROGRESS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat(getExerciseKey(), score);
            editor.commit();
        }
    }

    public float getSavedHighScore(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.KEY_SHAREDPREFERENCES_PROGRESS, Context.MODE_PRIVATE);
        float highScore = sharedPref.getFloat(getExerciseKey(), 0); // false set as default value if isComplete has not been set
        return highScore;
    }

    public ArrayList<VocabWordQuizExerciseItem> createVocabWordQuizExerciseItemArrayList(LinkedHashMap<String, String> vocabHashMap, String exerciseName, String lessonName) {
        ArrayList<VocabWordQuizExerciseItem> vocabWordQuizExerciseItemArrayList = new ArrayList<>();
        for (LinkedHashMap.Entry<String, String> vocabEntryItem : vocabHashMap.entrySet()) {
            String vocabWord = vocabEntryItem.getKey();
            String vocabTranslation = vocabEntryItem.getValue();
            VocabWordQuizExerciseItem vocabWordQuizExerciseItem = new VocabWordQuizExerciseItem(vocabWord, vocabTranslation, exerciseName, lessonName);
            vocabWordQuizExerciseItemArrayList.add(vocabWordQuizExerciseItem);
        }
        return vocabWordQuizExerciseItemArrayList;
    }

    public ArrayList<VocabWordQuizExerciseItem> getVocabWordQuizExerciseItemArrayList() {
        return mVocabWordQuizExerciseItemArrayList;
    }

    @Override
    public boolean isComplete(Context context) {
        // loop through every ExerciseItem to determine if it has been completed
        for (VocabWordExerciseItem item: mVocabWordQuizExerciseItemArrayList) {
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

    public String getExerciseKey() {
        return mExerciseKey;
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
