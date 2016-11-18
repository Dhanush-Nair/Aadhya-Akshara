package com.leapsoftware.leap.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.adapters.PronunciationFragmentPagerAdapter;
import com.leapsoftware.leap.exercises.PronunciationExercise;
import com.leapsoftware.leap.utils.VocabLessonActivityCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PronunciationFragment# newinstance} factory method to
 * create an instance of this fragment.
 */
public class PronunciationFragment extends Fragment {
    private Context mContext;
    private VocabLessonActivityCallback mVocabLessonActivityCallback;
    protected Button mNextButton;
    protected ViewPager mViewPager;
    public final static String TAG = "PronunciationFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PRONUNCIATION_EXERCISE = "argPronunciationExercise";

    // local instance of extra passed from newInstance parameter
    public PronunciationExercise mPronunciationExercise;

    public PronunciationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pronunciationExercise Parameter 1.
     * @return A new instance of fragment
     */
    public static PronunciationFragment newInstance(PronunciationExercise pronunciationExercise) {
        PronunciationFragment fragment = new PronunciationFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_PRONUNCIATION_EXERCISE, pronunciationExercise);
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

        // getArguments from new instance
        if (getArguments() != null) {
            mPronunciationExercise = getArguments().getParcelable(ARG_PRONUNCIATION_EXERCISE);
        }

        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pronunciation, container, false);

        // Set mViewPager and PronunciationFragmentPagerAdapter
        mViewPager = (ViewPager) rootView.findViewById(R.id.fragment_pronunciation_viewpager);

        PronunciationFragmentPagerAdapter pronunciationFragmentPagerAdapter = new PronunciationFragmentPagerAdapter(getChildFragmentManager(), mContext, mPronunciationExercise);

        mViewPager.setAdapter(pronunciationFragmentPagerAdapter);
        mViewPager.setCurrentItem(0);

        // Disables swiping in the PromunciationFragmentPagerAdapter. User goes to next fragment by clicking button.
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onDetach() {
        mVocabLessonActivityCallback = null; // set to null to avoid memory leaks
        super.onDetach();
    }

    // goToNextPage(View view)
    public void goToNextPage(int additionalItem) {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + additionalItem);
    }

}
