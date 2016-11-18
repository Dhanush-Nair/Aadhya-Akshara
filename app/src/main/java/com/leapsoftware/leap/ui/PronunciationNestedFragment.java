package com.leapsoftware.leap.ui;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.exerciseItems.PronunciationExerciseItem;
import com.leapsoftware.leap.exercises.PronunciationExercise;
import com.leapsoftware.leap.utils.Attempt;
import com.leapsoftware.leap.utils.Constants;
import com.leapsoftware.leap.utils.VocabLessonActivityCallback;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PronunciationNestedFragment extends Fragment {
    private Context mContext;
    private VocabLessonActivityCallback mVocabLessonActivityCallback;
    protected ImageButton mMicButton;
    protected TextView mPromptTextView;
    protected TextView mPhraseTextView;
    protected Button mPreviousButton;
    protected Button mNextButton;
    protected Intent mSpeechIntent;
    protected String mTextToBeRead;
    protected ViewPager mViewPager;
    protected VocabularyLessonActivity mVocabularyLessonActivity;
    protected PronunciationExerciseItem mPronunciationExerciseItem;
    public static final String TAG = "ProNestedFrag";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PRONUNCIATION_EX = "argPronunciationEx";
    private static final String ARG_POSITION = "argPosition";

    // local instance of extra passed from newInstance parameter
    protected PronunciationExercise mPronunciationExercise;
    protected int mPosition;

    public PronunciationNestedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pronunciationExercise Parameter 1.
     * @return A new instance of fragment
     */
    public static PronunciationNestedFragment newInstance(PronunciationExercise pronunciationExercise, int position) {
        PronunciationNestedFragment fragment = new PronunciationNestedFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_PRONUNCIATION_EX, pronunciationExercise);
        args.putInt(ARG_POSITION, position);
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
            mPronunciationExercise = getArguments().getParcelable(ARG_PRONUNCIATION_EX);
            mPosition = getArguments().getInt(ARG_POSITION);
            // TODO
            // mVocabWordExerciseItem= savedInstanceState.getParcelable(ARG_VOCABWORDEXITEM);
            // change to FragmentStatePagerAdapter
        }

        // TODO: make interface method to getActivity() instead of cast
        mVocabularyLessonActivity = ((VocabularyLessonActivity) getActivity());

        ArrayList<PronunciationExerciseItem> pronunciationExerciseItemArrayList = mPronunciationExercise.getPronunciationExerciseItemArrayList();
        mPronunciationExerciseItem = pronunciationExerciseItemArrayList.get(mPosition);

        // Spit dialog into two arrays, do not speak character's name
        String dialogClicked = mPronunciationExerciseItem.getDialog().trim();
        String[] splitDialogArray = dialogClicked.split(" ", 2); // Splits dialog into two arrays. Character name is put into first array.
        String dialogLessName = splitDialogArray[1]; // The second dialog without the character's name, which will be spoken by tts.
        mTextToBeRead = dialogLessName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pronunciation_nested, container, false);

        mMicButton = (ImageButton) rootView.findViewById(R.id.fragment_pronunciation_nested_mic_imagebutton);
        mPromptTextView = (TextView) rootView.findViewById(R.id.fragment_pronunciation_nested_prompt_textview);
        mPhraseTextView = (TextView) rootView.findViewById(R.id.fragment_pronunciation_nested_phrase_textview);
        mPreviousButton = (Button) rootView.findViewById(R.id.fragment_pronunciation_nested_previous_button);
        mNextButton = (Button) rootView.findViewById(R.id.fragment_pronunciation_nested_next_button);

        mPhraseTextView.setText(mTextToBeRead);

        //Speech to text
        mSpeechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        return rootView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String speechDirections = String.format(mContext.getString(R.string.pronunciation_google_voice_dialog_box), mTextToBeRead);
                    mSpeechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, speechDirections);
                    Log.d(TAG, "mTextToBeRead = " + mTextToBeRead);

                    Bundle extras = new Bundle();
                    extras.putString(Constants.PRONUNCIATION_CORRECT_PHRASE, mTextToBeRead);

                    mVocabularyLessonActivity.startActivityForResult(mSpeechIntent, Constants.REQ_CODE_SPEECH_INPUT, extras);
                    mPronunciationExerciseItem.setIsItemComplete(true, mContext);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // If PronunciationExercise is complete, show user alert dialog
                if (mPronunciationExercise.isComplete(mContext)) {
                    if (mPronunciationExercise.getAttempt(mContext) == Attempt.FIRSTTRY) {
                        mPronunciationExercise.setAttempt(Attempt.STUDYING, mContext);
                        AlertDialog.Builder exerciseCompleteBuilder = new AlertDialog.Builder(mContext);
                        exerciseCompleteBuilder.setTitle(mContext.getString(R.string.exercise_complete_alert_dialog_title));
                        exerciseCompleteBuilder.setMessage(mContext.getString(R.string.exercise_complete_alert_dialog_message));
                        exerciseCompleteBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // exchange fragment to bring user back to VocabularyLessonMainContentFragment
                                mViewPager.setCurrentItem(0);
                                mVocabLessonActivityCallback.exchangeFragment(5);
                            }
                        });
                        AlertDialog exerciseCompleteDialog = exerciseCompleteBuilder.create();
                        exerciseCompleteDialog.show();
                    }
                }
            }
        });

        mViewPager = (ViewPager) getActivity().findViewById(R.id.fragment_pronunciation_viewpager);

        if (mPosition + 1 == mViewPager.getAdapter().getCount()) {
            mNextButton.setText(R.string.finish_button);
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // exchange fragment to bring user back to VocabularyLessonMainContentFragment
                    mViewPager.setCurrentItem(0);
                    mVocabLessonActivityCallback.exchangeFragment(5);

                }
            });
        } else {
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }
            });
        }

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            }
        });
    }

    @Override
    public void onDetach() {
        mVocabLessonActivityCallback = null; // set to null to avoid memory leaks
        super.onDetach();
    }
}
