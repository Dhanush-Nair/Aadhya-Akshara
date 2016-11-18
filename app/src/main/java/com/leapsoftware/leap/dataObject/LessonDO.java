package com.leapsoftware.leap.dataObject;

import java.util.LinkedHashMap;

/**
 * Created by vincentrickey on 1/15/16.
 */
// This class mirrors a lesson Json object from the Data.json asset
// It is deserialized by GSON in the LessonGSONDeserializer class
public class LessonDO {
    private String mLessonNameEnglish;
    private String mLessonNameTranslated;
    private LinkedHashMap<String, String> mVocabMap;
    private LinkedHashMap<String, String> mDialogMap;
    private String[] mExerciseNamesEnglish;
    private String[] mExerciseNamesTranslated;

    public String getLessonNameEnglish() {
        return mLessonNameEnglish;
    }

    public void setLessonNameEnglish(String lessonNameEnglish) {
        this.mLessonNameEnglish = lessonNameEnglish;
    }

    public String getLessonNameTranslated() {
        return mLessonNameTranslated;
    }

    public void setLessonNameTranslated(String lessonNameTranslated) {
        mLessonNameTranslated = lessonNameTranslated;
    }

    public LinkedHashMap<String, String> getVocabMap() {
        return mVocabMap;
    }

    public void setVocabMap(LinkedHashMap<String, String> vocabMap) {
        this.mVocabMap = vocabMap;
    }

    public LinkedHashMap<String, String> getDialogMap() {
        return mDialogMap;
    }

    public void setDialogMap(LinkedHashMap<String, String> dialogMap) {
        this.mDialogMap = dialogMap;
    }

    public String[] getExerciseNamesEnglish() {
        return mExerciseNamesEnglish;
    }

    public void setExerciseNamesEnglish(String[] exerciseNamesEnglish) {
        this.mExerciseNamesEnglish = exerciseNamesEnglish;
    }

    public String[] getExerciseNamesTranslated() {
        return mExerciseNamesTranslated;
    }

    public void setExerciseNamesTranslated(String[] exerciseNamesTranslated) {
        mExerciseNamesTranslated = exerciseNamesTranslated;
    }
}
