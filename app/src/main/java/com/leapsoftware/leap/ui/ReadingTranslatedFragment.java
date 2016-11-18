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
import com.leapsoftware.leap.adapters.ReadingTranslatedFragmentAdapter;
import com.leapsoftware.leap.exercises.ReadingExercise;
import com.leapsoftware.leap.utils.VocabLessonActivityCallback;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadingTranslatedFragment extends Fragment implements TextToSpeech.OnInitListener {
    private VocabLessonActivityCallback mVocabLessonActivityCallback;
    protected Context mContext;
    private TextToSpeech mTextToSpeech;
    private Locale mLocale;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_READINGEXERCISE = "argReadingExercise";

    // local instance of extra passed from newInstance parameter
    private ReadingExercise mReadingExercise;


    public ReadingTranslatedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param readingExercise Parameter 1.
     * @return A new instance of fragment.
     */
    public static ReadingTranslatedFragment newInstance(ReadingExercise readingExercise) {
        ReadingTranslatedFragment fragment = new ReadingTranslatedFragment();

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
        mLocale = Locale.getDefault();
        Log.d("ReadingTransFrag", "Locale = " + mLocale.toString());
        mTextToSpeech = new TextToSpeech(mContext, this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reading_translated, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_reading_translated_recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ReadingTranslatedFragmentAdapter readingTranslatedFragmentAdapter = new ReadingTranslatedFragmentAdapter(mReadingExercise, mContext, mTextToSpeech, mVocabLessonActivityCallback);
        recyclerView.setAdapter(readingTranslatedFragmentAdapter);

        return rootView;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mTextToSpeech.setLanguage(mLocale);
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
            Log.d("ReadingTranslatedFrag", "readingfrag tts shutdown");
        }
        super.onDestroy();
    }

}
