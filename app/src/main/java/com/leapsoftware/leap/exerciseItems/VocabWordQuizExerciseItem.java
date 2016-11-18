package com.leapsoftware.leap.exerciseItems;

import android.os.Parcel;
import android.os.Parcelable;

import com.leapsoftware.leap.utils.Answerable;
import com.leapsoftware.leap.utils.Attempt;
import com.leapsoftware.leap.utils.Completable;

/**
 * Created by vincentrickey on 2/24/16.
 */
public class VocabWordQuizExerciseItem extends VocabWordExerciseItem implements Answerable, Parcelable, Completable {
    public String mCorrectAnswer;
    protected String mUserAnswer;
    protected boolean mIsCorrect;
    protected Attempt mAttempt;

    public VocabWordQuizExerciseItem(String vocabWord, String vocabTranslation, String exerciseName, String lessonName) {
        super(vocabWord, vocabTranslation, exerciseName, lessonName);
        this.mCorrectAnswer = vocabWord;
        this.mAttempt = Attempt.FIRSTTRY; // Default set to FIRSTTRY
        this.mExerciseName = exerciseName;
        this.mLessonName = lessonName;
        this.mItemKey = lessonName + exerciseName + vocabWord;
    }

    public VocabWordQuizExerciseItem(Parcel in) {
        super(in);
        this.mCorrectAnswer = in.readString();
        this.mIsCorrect = in.readByte() != 0;
        this.mAttempt = in.readParcelable(Attempt.class.getClassLoader());
        this.mExerciseName = in.readString();
        this.mLessonName = in.readString();
        this.mItemKey = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mCorrectAnswer);
        dest.writeByte((byte) (mIsCorrect ? 1 : 0));
        dest.writeParcelable(mAttempt, flags);
        dest.writeString(mExerciseName);
        dest.writeString(mLessonName);
        dest.writeString(mItemKey);
    }

    @Override
    public String getCorrectAnswer() {
        return mCorrectAnswer;
    }

    @Override
    public void setUserAnswer(String userAnswer) {
        this.mUserAnswer = userAnswer;
    }

    @Override
    public String getUserAnswer() {
        return mUserAnswer;
    }

    @Override
    public boolean isCorrect() {
        if (mCorrectAnswer.equalsIgnoreCase(mUserAnswer)) {
            mIsCorrect = true;
        } else if (mAttempt.equals(Attempt.TYPO)) {
            mIsCorrect = true;
        } else {
            mIsCorrect = false;
        }
        return mIsCorrect;
    }

    public static final Creator<VocabWordQuizExerciseItem> CREATOR = new Creator<VocabWordQuizExerciseItem>() {

        @Override
        public VocabWordQuizExerciseItem createFromParcel(Parcel in) {
            return new VocabWordQuizExerciseItem(in);
        }

        @Override
        public VocabWordQuizExerciseItem[] newArray(int size) {
            return new VocabWordQuizExerciseItem[size];
        }
    };

    public Attempt getAttempt() {
        return mAttempt;
    }

    public void setAttempt(Attempt attempt) {
        mAttempt = attempt;
    }

}
