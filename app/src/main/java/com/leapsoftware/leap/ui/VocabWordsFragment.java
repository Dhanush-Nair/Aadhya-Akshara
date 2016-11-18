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
import com.leapsoftware.leap.adapters.VocabWordsFragmentPagerAdapter;
import com.leapsoftware.leap.exercises.VocabWordsExercise;
import com.leapsoftware.leap.utils.VocabLessonActivityCallback;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link }
 * interface.
 */
public class VocabWordsFragment extends Fragment {
    protected VocabLessonActivityCallback mVocabLessonActivityCallback;
    public FragmentPagerAdapter mFragmentPagerAdapter;
    public Context mContext;
    public ViewPager mViewPager;
    public static final String TAG = "VocabWordsListFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_VOCAB_WORD_EXERCISE = "argVocabWordExercise";

    // local instance of extra passed from newInstance parameter
    public VocabWordsExercise mVocabWordExercise;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VocabWordsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param vocabWordExercise Parameter 1.
     * @return A new instance of fragment.
     */
    public static VocabWordsFragment newInstance(VocabWordsExercise vocabWordExercise) {
        VocabWordsFragment fragment = new VocabWordsFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_VOCAB_WORD_EXERCISE, vocabWordExercise);
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
            mVocabWordExercise = getArguments().getParcelable(ARG_VOCAB_WORD_EXERCISE);
        }

        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vocabwords, container, false);

        // Create new fragment pager to return a new fragment for VocabWordsCardsFragment, VocabWordsListFragment, and PronunciationFragment
        mFragmentPagerAdapter = new VocabWordsFragmentPagerAdapter(getChildFragmentManager(), mContext, mVocabWordExercise);

        // Set up the ViewPager with the fragment pager adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.fragment_vocab_words_viewPager_container);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.fragment_vocab_words_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }

    @Override
    public void onDetach() {
        mVocabLessonActivityCallback = null; // set to null to avoid memory leaks
        super.onDetach();
    }

}
