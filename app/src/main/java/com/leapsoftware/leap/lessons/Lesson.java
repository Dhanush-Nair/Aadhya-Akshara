package com.leapsoftware.leap.lessons;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.leapsoftware.leap.dataObject.LessonDO;
import com.leapsoftware.leap.exercises.Exercise;
import com.leapsoftware.leap.exercises.PronunciationExercise;
import com.leapsoftware.leap.exercises.QuizExercise;
import com.leapsoftware.leap.exercises.ReadingExercise;
import com.leapsoftware.leap.exercises.VocabWordsExercise;
import com.leapsoftware.leap.utils.Attempt;
import com.leapsoftware.leap.utils.Completable;
import com.leapsoftware.leap.utils.Constants;

import java.util.ArrayList;

/**
 * Created by vincentrickey on 2/29/16.
 */
public class Lesson implements Completable, Parcelable {
    public ArrayList<Exercise> mExerciseArrayList;
    public String mTitleEnglish;
    public String mTitleTranslated;
    public Attempt mAttempt;
    public String mAttemptKey;

    public Lesson(LessonDO lessonDO) {
        this.mExerciseArrayList = initializeExerciseArrayList(lessonDO);
        this.mTitleEnglish = lessonDO.getLessonNameEnglish();
        this.mTitleTranslated = lessonDO.getLessonNameTranslated();
        this.mAttempt = Attempt.FIRSTTRY; // Default set to FIRSTTRY
        this.mAttemptKey = lessonDO.getLessonNameEnglish() + "attemptEnum";
    }

    protected Lesson(Parcel in) {
        this.mTitleEnglish = in.readString();
        mExerciseArrayList = new ArrayList<Exercise>();
        in.readList(mExerciseArrayList, Exercise.class.getClassLoader());
        this.mTitleTranslated = in.readString();
        this.mAttempt = in.readParcelable(Attempt.class.getClassLoader());
        this.mAttemptKey = in.readString();
    }

    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
        @Override
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        @Override
        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitleEnglish);
        dest.writeList(mExerciseArrayList);
        dest.writeString(mTitleTranslated);
        dest.writeParcelable(mAttempt, flags);
        dest.writeString(mAttemptKey);
    }

    public ArrayList<Exercise> initializeExerciseArrayList(LessonDO lessonDO) {

        ArrayList<Exercise> exerciseArrayList = new ArrayList<Exercise>();

        // Lesson names and exercise names are passed to create unique keys for shared preferences, which will be used to track user progress for each exercise item
        VocabWordsExercise vocabWordsExercise = new VocabWordsExercise(lessonDO.getVocabMap(), lessonDO.getDialogMap(), lessonDO.getExerciseNamesEnglish()[0], lessonDO.getExerciseNamesTranslated()[0], lessonDO.getLessonNameEnglish());
        ReadingExercise readingExercise = new ReadingExercise(lessonDO.getVocabMap(), lessonDO.getDialogMap(), lessonDO.getExerciseNamesEnglish()[1], lessonDO.getExerciseNamesTranslated()[1], lessonDO.getLessonNameEnglish());
        PronunciationExercise pronunciationExercise = new PronunciationExercise(lessonDO.getVocabMap(), lessonDO.getDialogMap(), lessonDO.getExerciseNamesEnglish()[2], lessonDO.getExerciseNamesTranslated()[2], lessonDO.getLessonNameEnglish());
        QuizExercise vocabWordQuizExercise = new QuizExercise(lessonDO.getVocabMap(), lessonDO.getDialogMap(), lessonDO.getExerciseNamesEnglish()[3], lessonDO.getExerciseNamesTranslated()[3], lessonDO.getLessonNameEnglish());

        // Order of Exercises
        exerciseArrayList.add(vocabWordsExercise);
        exerciseArrayList.add(readingExercise);
        exerciseArrayList.add(pronunciationExercise);
        exerciseArrayList.add(vocabWordQuizExercise);

        return exerciseArrayList;
    }

    @Override
    public boolean isComplete(Context context) {
        // loop through every ExerciseItem to determine if it has been completed
        for (Exercise exercise : mExerciseArrayList) {
            if (exercise.isComplete(context) == false) {
                // If an exercise is incomplete, then lesson is incomplete
                return false;
            }
        }
        // If all exercises are complete, then lesson is complete
        return true;
    }

    public ArrayList<Exercise> getExerciseArrayList() {
        return mExerciseArrayList;
    }

    public Exercise getCurrentExercise(Context context) {
        for (Exercise exercise : mExerciseArrayList) {
            if (exercise.isComplete(context) == false) {
                return exercise;
            }
        }
        return null;
    }

    public String getTitleEnglish() {
        return mTitleEnglish;
    }

    public String getTitleTranslated() {
        return mTitleTranslated;
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
