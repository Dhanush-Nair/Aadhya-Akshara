package com.leapsoftware.leap.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.leapsoftware.leap.exercises.QuizExercise;
import com.leapsoftware.leap.ui.QuizNestedFragment;

/**
 * Created by vincentrickey on 2/9/16.
 */
public class QuizFragmentPagerAdapter extends FragmentPagerAdapter {
    public Context mContext;
    public QuizExercise mQuizExercise;

    public QuizFragmentPagerAdapter(FragmentManager fragmentManager, Context context, QuizExercise quizExercise) {
        super(fragmentManager);
        this.mContext = context;
        this.mQuizExercise = quizExercise;
    }


    @Override
    public Fragment getItem(int position) {

        // To return a pronunciation quiz or vocabwords quiz...
        // Make java factory for abstract genericQuizNestedFragment
        // vocabWordsQuizNestedFragment and pronunciationQuizeNestedFragment will implement same interface
        // Factory will take vocabWordExerciseItem and create genericQuizNestedFragment (can be either pronunciationQuiz or vocabWordQuiz)

        QuizNestedFragment quizNestedFragment = QuizNestedFragment.newInstance(mQuizExercise, position);

        return quizNestedFragment;
    }

    @Override
    public int getCount() {
        return mQuizExercise.getVocabWordQuizExerciseItemArrayList().size();
    }
}
