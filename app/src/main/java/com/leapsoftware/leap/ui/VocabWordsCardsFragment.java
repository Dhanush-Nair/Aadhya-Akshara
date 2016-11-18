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
import com.leapsoftware.leap.adapters.VocabWordFragmentCardAdapter;
import com.leapsoftware.leap.exerciseItems.VocabWordExerciseItem;
import com.leapsoftware.leap.exercises.VocabWordsExercise;
import com.leapsoftware.leap.utils.VocabLessonActivityCallback;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class VocabWordsCardsFragment extends Fragment implements TextToSpeech.OnInitListener {
    public Context mContext;
    public String mLocaleCode;
    private TextToSpeech mTextToSpeech;
    protected ArrayList<VocabWordExerciseItem> mVocabWordExerciseItemArrayList;
    private VocabLessonActivityCallback mVocabLessonActivityCallback;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_VOCAB_WORD_EXERCISE = "argVocabWordExercise";

    // TODO: Rename and change types of parameters
    public VocabWordsExercise mVocabWordExercise;


    public VocabWordsCardsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param vocabWordExercise Parameter 1.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VocabWordsCardsFragment newInstance(VocabWordsExercise vocabWordExercise) {
        VocabWordsCardsFragment fragment = new VocabWordsCardsFragment();

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

        // TODO: Rename parameters
        if (getArguments() != null) {
            mVocabWordExercise = getArguments().getParcelable(ARG_VOCAB_WORD_EXERCISE);
        }

        mContext = getContext();
        mLocaleCode = Locale.getDefault().toString();
        mTextToSpeech = new TextToSpeech(mContext, this);

        mVocabWordExerciseItemArrayList = mVocabWordExercise.getVocabWordExerciseItemArrayList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_vocabwords_cards, container, false);

        // Set RecyclerView to xml layout
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_vocabwords_cards_recyclerview);
        recyclerView.setHasFixedSize(true);

        // Set LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        // Set VocabularyFragmentAdapter to RecyclerView
        VocabWordFragmentCardAdapter vocabWordFragmentCardAdapter = new VocabWordFragmentCardAdapter(mVocabWordExerciseItemArrayList, getContext(), mTextToSpeech);
        recyclerView.setAdapter(vocabWordFragmentCardAdapter);

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
            Log.d("VocabWordsCardsFrag", "VocabWordsCardsFrag tts shutdown");
        }
        super.onDestroy();
    }

}
