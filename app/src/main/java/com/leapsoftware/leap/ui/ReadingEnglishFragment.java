package com.leapsoftware.leap.ui;


import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.adapters.ReadingEnglishFragmentAdapter;
import com.leapsoftware.leap.exercises.ReadingExercise;
import com.leapsoftware.leap.utils.VocabLessonActivityCallback;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadingEnglishFragment extends Fragment implements TextToSpeech.OnInitListener {
    private VocabLessonActivityCallback mVocabLessonActivityCallback;
    protected Context mContext;
    private TextToSpeech mTextToSpeech;
    public static final String TAG = "ReadingEnglishFrag";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_READINGEXERCISE = "argReadingExercise";

    // local instance of extra passed from newInstance parameter
    private ReadingExercise mReadingExercise;

    public ReadingEnglishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param readingExercise Parameter 1.
     * @return A new instance of fragment.
     */
    public static ReadingEnglishFragment newInstance(ReadingExercise readingExercise) {
        ReadingEnglishFragment fragment = new ReadingEnglishFragment();

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
        mTextToSpeech = new TextToSpeech(mContext, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reading_english, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_reading_english_recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ReadingEnglishFragmentAdapter readingEnglishFragmentAdapter = new ReadingEnglishFragmentAdapter(mReadingExercise, mContext, mTextToSpeech, mVocabLessonActivityCallback);
        recyclerView.setAdapter(readingEnglishFragmentAdapter);

        return rootView;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mTextToSpeech.setLanguage(Locale.US);
        }
    }

    @Override
    public void onDetach() {
        mVocabLessonActivityCallback = null; // set to null to avoid memory leaks
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        if (mTextToSpeech != null) {
            mTextToSpeech.shutdown();
            Log.d("ReadingEnglishFrag", "readingfrag tts shutdown");
        }
        super.onDestroy();
    }

}
