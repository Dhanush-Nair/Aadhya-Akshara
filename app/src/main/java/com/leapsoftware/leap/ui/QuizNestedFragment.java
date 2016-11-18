package com.leapsoftware.leap.ui;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.exerciseItems.VocabWordQuizExerciseItem;
import com.leapsoftware.leap.exercises.QuizExercise;
import com.leapsoftware.leap.utils.Attempt;
import com.leapsoftware.leap.utils.VocabLessonActivityCallback;

import java.util.ArrayList;

public class QuizNestedFragment extends Fragment {
    protected TextView vQuestionDisplayed;
    protected EditText vEditText;
    protected Button vNotSureButton;
    protected Button vRestartButton;
    protected TextView vQuestionNumber;
    protected ViewPager mViewPager;
    private VocabLessonActivityCallback mVocabLessonActivityCallback;
    protected Context mContext;
    protected VocabWordQuizExerciseItem mVocabWordQuizExerciseItem;
    public static final String TAG = "QuizNestedFrag";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_QUIZEXERCISE = "argQuizExercise";
    private static final String ARG_POSITION = "argPosition";

    // local instance of extra passed from newInstance parameter
    public QuizExercise mQuizExercise;
    public int mPosition;


    public QuizNestedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param quizExercise Parameter 1.
     * @return A new instance of fragment
     */
    public static QuizNestedFragment newInstance(QuizExercise quizExercise, int position) {
        QuizNestedFragment fragment = new QuizNestedFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_QUIZEXERCISE, quizExercise);
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
            mQuizExercise = getArguments().getParcelable(ARG_QUIZEXERCISE);
            mPosition = getArguments().getInt(ARG_POSITION);
        }

        ArrayList<VocabWordQuizExerciseItem> vocabWordQuizExerciseItemArrayList = mQuizExercise.getVocabWordQuizExerciseItemArrayList();
        mVocabWordQuizExerciseItem = vocabWordQuizExerciseItemArrayList.get(mPosition);

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_quiz_nested, container, false);

        mViewPager = (ViewPager) getActivity().findViewById(R.id.fragment_quiz_viewPager);

        // Sets question displayed to the translatedVocabWord (localized content set in json data file)
        vQuestionDisplayed = (TextView) rootView.findViewById(R.id.fragment_quiz_nested_question_textview);
        vQuestionDisplayed.setText(mVocabWordQuizExerciseItem.getTranslatedVocabWord());

        vEditText = (EditText) rootView.findViewById(R.id.fragment_quiz_nested_editText);
        vNotSureButton = (Button) rootView.findViewById(R.id.fragment_quiz_nested_not_sure_button);
        vRestartButton = (Button) rootView.findViewById(R.id.fragment_quiz_nested_restart_button);

        // Formats QuestionNumber string using "Question" + mPosition + "of" + ArrayList.sized()
        vQuestionNumber = (TextView) rootView.findViewById(R.id.fragment_quiz_nested_question_number_textview);
        String questionNum = getResources().getString(R.string.quiz_nested_fragment_question_number,
                mPosition + 1,
                mQuizExercise.getVocabWordQuizExerciseItemArrayList().size());
        vQuestionNumber.setText(questionNum);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Compare user's answer to question. If strings match, display great job snackbar.
        vEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    // Get user's answer from EditText. Trim to ignore accidental whitespace.
                    mVocabWordQuizExerciseItem.setUserAnswer(v.getText().toString().trim());

                    if (mVocabWordQuizExerciseItem.isCorrect()) {
                        Snackbar.make(v, R.string.confidence_great, Snackbar.LENGTH_LONG).show();
                        clearEditText();

                        mVocabWordQuizExerciseItem.setIsItemComplete(true, mContext);
                        mVocabLessonActivityCallback.sendNextVocabQuestion(1);

                        // If QuizExercise is complete, show user exercise complete alert dialog
                        if (mQuizExercise.isComplete(mContext)) {
                            if (mQuizExercise.getAttempt(mContext) == Attempt.FIRSTTRY) {
                                // Check if view has focus. Hide keyboard.
                                View view = getActivity().getCurrentFocus();
                                hideKeyboard(view);

                                mQuizExercise.setAttempt(Attempt.STUDYING, mContext);

                                AlertDialog exerciseCompleteDialog = createExerciseCompleteAlertDialog();
                                exerciseCompleteDialog.show();

                                mViewPager.setCurrentItem(0);
                            } else if (mPosition + 1 == mViewPager.getAdapter().getCount()) {
                                AlertDialog exerciseCompleteDialog = createExerciseCompleteAlertDialog();
                                exerciseCompleteDialog.show();

                                mViewPager.setCurrentItem(0);
                            }
                        }
                        // if user enters nothing, prompt to enter answer
                    } else if (mVocabWordQuizExerciseItem.getUserAnswer().equals("")) {
                        AlertDialog blankAnswerDialog = createBlankAnswerAlertDialog();
                        blankAnswerDialog.show();
                        clearEditText();
                    }
                    // if user is incorrect, show AlertDialog that compares their answer with the correct one
                    else {
                        AlertDialog.Builder incorrectAnswerBuilder = new AlertDialog.Builder(getActivity());
                        String correctMessage = String.format(mContext.getString(R.string.quiz_correct_answer_dialog_message), mVocabWordQuizExerciseItem.getCorrectAnswer());
                        String incorrectMessage = String.format(mContext.getString(R.string.quiz_incorrect_answer_dialog_message), mVocabWordQuizExerciseItem.getUserAnswer());
                        incorrectAnswerBuilder.setMessage(correctMessage +
                                "\n" +
                                "\n" +
                                incorrectMessage);
                        incorrectAnswerBuilder.setTitle(mContext.getString(R.string.quiz_incorrect_answer_title));
                        incorrectAnswerBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mVocabWordQuizExerciseItem.setAttempt(Attempt.RETRY);
                                dialog.dismiss();
                            }
                        });
                        incorrectAnswerBuilder.setNeutralButton(mContext.getString(R.string.quiz_thats_what_I_meant), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (mVocabWordQuizExerciseItem.getAttempt().equals(Attempt.RETRY)) {
                                    mVocabWordQuizExerciseItem.setIsItemComplete(true, mContext);
                                } else {
                                    mVocabWordQuizExerciseItem.setAttempt(Attempt.TYPO);
                                    mVocabWordQuizExerciseItem.setIsItemComplete(true, mContext);
                                }
                                if (mQuizExercise.isComplete(mContext) && mQuizExercise.getAttempt(mContext) == Attempt.FIRSTTRY) {
                                    mQuizExercise.setAttempt(Attempt.STUDYING, mContext);
                                    AlertDialog exerciseCompleteAlertDialog = createExerciseCompleteAlertDialog();
                                    exerciseCompleteAlertDialog.show();
                                    mViewPager.setCurrentItem(0);
                                } else {
                                    mVocabLessonActivityCallback.sendNextVocabQuestion(1);
                                }
                            }
                        });
                        AlertDialog incorrectAnswerdialog = incorrectAnswerBuilder.create();
                        incorrectAnswerdialog.show();

                        clearEditText();
                    }
                    return true;
                }
                return false;
            }
        });

        vNotSureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder notSureBuilder = new AlertDialog.Builder(getActivity());
                String correctMessage = String.format(mContext.getString(R.string.quiz_correct_answer_dialog_message), mVocabWordQuizExerciseItem.getCorrectAnswer());
                notSureBuilder.setMessage(correctMessage +
                        "\n" +
                        "\n" +
                        getString(R.string.quiz_not_sure_try_again_message));
                notSureBuilder.setTitle(mContext.getString(R.string.quiz_not_sure_dialog_title));
                notSureBuilder.setPositiveButton(mContext.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mVocabWordQuizExerciseItem.setAttempt(Attempt.RETRY);
                        dialog.dismiss();
                    }
                });
                AlertDialog notSureDialog = notSureBuilder.create();
                notSureDialog.show();

                clearEditText();
            }
        });

        vRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart();
            }
        });

    }

    @Override
    public void onDetach() {
        mVocabLessonActivityCallback = null; // set to null to avoid memory leaks
        super.onDetach();
    }

    public void clearEditText() {
        vEditText.setText("");
    }

    private void restart() {
        if (mQuizExercise.getAttempt(mContext) == Attempt.FIRSTTRY) {
            // Set attempt to first try for each item so user's score isn't negatively impacted
            for (VocabWordQuizExerciseItem item : mQuizExercise.getVocabWordQuizExerciseItemArrayList()) {
                item.setAttempt(Attempt.FIRSTTRY);
                item.setIsItemComplete(false, mContext);
            }
        }
        // Bring back to first quiz fragment
        mViewPager.setCurrentItem(0);
    }

    private AlertDialog createExerciseCompleteAlertDialog() {
        AlertDialog.Builder exerciseCompleteBuilder = new AlertDialog.Builder(mContext);
        exerciseCompleteBuilder.setTitle(mContext.getString(R.string.exercise_complete_alert_dialog_title));

        // Calculate score
        String scoreMessage = String.format(mContext.getString(R.string.quiz_exercise_complete_alert_dialog_message), Float.toString(mQuizExercise.calculateScore()));
        exerciseCompleteBuilder.setMessage(scoreMessage);

        // Save high score
        mQuizExercise.saveHighScore(mQuizExercise.calculateScore(), mContext);

        exerciseCompleteBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // exchange fragment to bring user back to VocabularyLessonMainContentFragment
                mVocabLessonActivityCallback.exchangeFragment(5);
            }
        });
        AlertDialog exerciseCompleteDialog = exerciseCompleteBuilder.create();
        return exerciseCompleteDialog;
    }

    private AlertDialog createBlankAnswerAlertDialog() {
        AlertDialog.Builder blankAnswerBuilder = new AlertDialog.Builder(getActivity());
        blankAnswerBuilder.setMessage(mContext.getString(R.string.quiz_blank_answer_dialog_message));
        blankAnswerBuilder.setTitle(mContext.getString(R.string.quiz_blank_answer_dialog_title));
        blankAnswerBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog blankAnswerDialog = blankAnswerBuilder.create();
        return blankAnswerDialog;
    }

    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}