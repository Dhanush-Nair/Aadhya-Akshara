package com.leapsoftware.leap.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.adapters.VocabularyLessonMCFragmentAdapter;
import com.leapsoftware.leap.lessons.Lesson;
import com.leapsoftware.leap.utils.VocabLessonActivityCallback;

/**
 * A placeholder fragment containing a simple view.
 */
public class VocabularyLessonMainContentFragment extends Fragment {
    private VocabLessonActivityCallback mVocabLessonActivityCallback;
    private static final String TAG = "VLessMainContFrag";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LESSON = "argLesson";

    // local instance of extra passed from newInstance parameter
    public Lesson mLesson;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VocabularyLessonMainContentFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param lesson Parameter 1.
     * @return A new instance of fragment.
     */
    public static VocabularyLessonMainContentFragment newInstance(Lesson lesson) {
        VocabularyLessonMainContentFragment fragment = new VocabularyLessonMainContentFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_LESSON, lesson);
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
            mLesson = getArguments().getParcelable(ARG_LESSON);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vocabulary_lesson_main_content, container, false);

        // Set recyclerview to xml layout
        RecyclerView vocabularyLessonRecyclerView = (RecyclerView) rootView.findViewById(R.id.vocabulary_lesson_recycler_view);

        // Set layoutmanager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        vocabularyLessonRecyclerView.setLayoutManager(layoutManager);

        // Set VocabularyLessonMCFragmentAdapter to recyclerview
        VocabularyLessonMCFragmentAdapter vocabularyLessonMCFragmentAdapter = new VocabularyLessonMCFragmentAdapter(getActivity(), mVocabLessonActivityCallback, mLesson);
        vocabularyLessonRecyclerView.setAdapter(vocabularyLessonMCFragmentAdapter);

        return rootView;
    }

    @Override
    public void onDetach() {
        mVocabLessonActivityCallback = null; // set to null to avoid memory leaks
        super.onDetach();
    }

}
