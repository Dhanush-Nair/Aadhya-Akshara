package com.leapsoftware.leap.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.adapters.QuizFragmentPagerAdapter;
import com.leapsoftware.leap.exercises.QuizExercise;
import com.leapsoftware.leap.utils.VocabLessonActivityCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizFragment extends Fragment {
    private VocabLessonActivityCallback mVocabLessonActivityCallback;
    protected Context mContext;
    private QuizFragmentPagerAdapter mFragmentPagerAdapter;
    private ViewPager mViewPager;
    public static final String TAG = "QuizFragment2";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_QUIZEXERCISE = "argQuizExercise";

    // local instance of extra passed from newInstance parameter
    private QuizExercise mQuizExercise;

    public QuizFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param quizExercise Parameter 1.
     * @return A new instance of fragment
     */
    public static QuizFragment newInstance(QuizExercise quizExercise) {
        QuizFragment fragment = new QuizFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_QUIZEXERCISE, quizExercise);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mVocabLessonActivityCallback = (VocabLessonActivityCallback) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();

        // getArguments from new instance
        if (getArguments() != null) {
            mQuizExercise = getArguments().getParcelable(ARG_QUIZEXERCISE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);

        // Create the adapter that will return a fragment for each of the primary sections of the activity.
        mFragmentPagerAdapter = new QuizFragmentPagerAdapter(getChildFragmentManager(), mContext, mQuizExercise);

        // Set up the ViewPager with the fragment pager adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.fragment_quiz_viewPager);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.setCurrentItem(0);

        // Disables swiping in the QuizFragmentPagerAdapter. User goes to next fragment by clicking button.
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //showSoftKeyboard();

        return rootView;
    }

    @Override
    public void onDetach() {
        mVocabLessonActivityCallback = null; // set to null to avoid memory leaks
        super.onDetach();
    }

    // goToNextPage(View view)
    public void GoToNextPage(int additionalItem) {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + additionalItem);
    }

    /*public void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }*/

}
