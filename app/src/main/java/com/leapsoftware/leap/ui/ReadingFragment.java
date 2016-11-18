package com.leapsoftware.leap.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.adapters.ReadingFragmentPagerAdapter;
import com.leapsoftware.leap.exercises.ReadingExercise;
import com.leapsoftware.leap.utils.VocabLessonActivityCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadingFragment extends Fragment {
    private VocabLessonActivityCallback mVocabLessonActivityCallback;
    protected Context mContext;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private ViewPager mViewPager;
    public static final String TAG = "ReadingFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_READINGEXERCISE = "argReadingExercise";

    // local instance of extra passed from newInstance parameter
    private ReadingExercise mReadingExercise;

    public ReadingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param readingExercise Parameter 1.
     * @return A new instance of fragment
     */
    public static ReadingFragment newInstance(ReadingExercise readingExercise) {
        ReadingFragment fragment = new ReadingFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_READINGEXERCISE, readingExercise);
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
            mReadingExercise = getArguments().getParcelable(ARG_READINGEXERCISE);
        }

        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reading, container, false);

        // Create the adapter that will return a fragment for each of the primary sections of the activity.
        mFragmentPagerAdapter = new ReadingFragmentPagerAdapter(getChildFragmentManager(), mContext, mReadingExercise);

        // Set up the ViewPager with the fragment pager adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.fragment_reading_viewPager_container);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.fragment_reading_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }

    @Override
    public void onDetach() {
        mVocabLessonActivityCallback = null; // set to null to avoid memory leaks
        super.onDetach();
    }

}
