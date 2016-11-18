package com.leapsoftware.leap.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.exercises.PronunciationExercise;
import com.leapsoftware.leap.exercises.QuizExercise;
import com.leapsoftware.leap.exercises.ReadingExercise;
import com.leapsoftware.leap.exercises.VocabWordsExercise;
import com.leapsoftware.leap.lessons.Lesson;
import com.leapsoftware.leap.utils.Attempt;
import com.leapsoftware.leap.utils.Constants;
import com.leapsoftware.leap.utils.VocabLessonActivityCallback;

import java.util.ArrayList;
import java.util.Arrays;

public class VocabularyLessonActivity extends AppCompatActivity implements VocabLessonActivityCallback {
    public static final String TAG = "VocabLessonActivity";
    public Context mContext;
    public FragmentManager mFragmentManager;
    // Correct Pronunciation Exercise Phrase
    public String mCorrectPhrase;
    // Instance of fragments for transactions in interface
    public VocabularyLessonMainContentFragment mVocabularyLessonMainContentFragment;
    public VocabWordsFragment mVocabWordsFragment;
    public ReadingFragment mReadingFragment;
    public PronunciationFragment mPronunciationFragment;
    public QuizFragment mQuizFragment;
    // Lesson
    protected Lesson mLesson;
    // Exercises
    protected VocabWordsExercise mVocabWordExercise;
    protected PronunciationExercise mPronunciationExercise;
    protected ReadingExercise mReadingExercise;
    protected QuizExercise mVocabWordQuizExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_lesson);

        // Get JSON data string from intent
        Intent originIntent = getIntent();
        Log.d(TAG, "intent has lesson extra = " + originIntent.hasExtra(Constants.KEY_EXTRAS_LESSON_SELECTED));
        mLesson = originIntent.getParcelableExtra(Constants.KEY_EXTRAS_LESSON_SELECTED);

        // Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mLesson.getTitleTranslated().replace("\"", ""));
        setSupportActionBar(toolbar);
        mContext = this;

        // Create fragment manager and new instance of main content fragment
        mFragmentManager = this.getSupportFragmentManager();
        mVocabularyLessonMainContentFragment = VocabularyLessonMainContentFragment.newInstance(mLesson);

        mVocabWordExercise = (VocabWordsExercise) mLesson.getExerciseArrayList().get(0);
        mReadingExercise = (ReadingExercise) mLesson.getExerciseArrayList().get(1);
        mPronunciationExercise = (PronunciationExercise) mLesson.getExerciseArrayList().get(2);
        mVocabWordQuizExercise = (QuizExercise) mLesson.getExerciseArrayList().get(3);

        // Create new instance of fragments using Lesson
        mVocabWordsFragment = VocabWordsFragment.newInstance(mVocabWordExercise);
        mPronunciationFragment = PronunciationFragment.newInstance(mPronunciationExercise);
        mReadingFragment = ReadingFragment.newInstance(mReadingExercise);
        mQuizFragment = QuizFragment.newInstance(mVocabWordQuizExercise);

        // Commit fragment transaction: Load main content from VocabularyLessonMainContentFragment
        mFragmentManager.beginTransaction()
                .replace(R.id.vocabulary_lesson_main_content_framelayout, mVocabularyLessonMainContentFragment)
                .commit();
    }

    @Override
    public Fragment exchangeFragment(int position) {
        switch (position) {
            case 0:
                // Vocab Words Exercise
                mFragmentManager.beginTransaction()
                        .replace(R.id.vocabulary_lesson_main_content_framelayout, mVocabWordsFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case 1:
                // Reading Exercise
                mFragmentManager.beginTransaction()
                        .replace(R.id.vocabulary_lesson_main_content_framelayout, mReadingFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case 2:
                // Pronunciation Exercise
                mFragmentManager.beginTransaction()
                        .replace(R.id.vocabulary_lesson_main_content_framelayout, mPronunciationFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case 3:
                // Quiz Exercise
                mFragmentManager.beginTransaction()
                        .replace(R.id.vocabulary_lesson_main_content_framelayout, mQuizFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case 4:
                // Complete. Show stats.
                Toast.makeText(mContext, "Completed score stats", Toast.LENGTH_LONG).show();
                break;
            case 5:
                // After completing exercise, pop the last fragment transaction off the back stack to return to the main content frag
                mFragmentManager.popBackStackImmediate();

                // If vocabWordExercise is complete, show user alert dialog with results
                if (mLesson.isComplete(mContext) && mLesson.getAttempt(mContext) == Attempt.FIRSTTRY) {
                    mLesson.setAttempt(Attempt.STUDYING, mContext);
                    AlertDialog.Builder lessonCompleteBuilder = new AlertDialog.Builder(mContext);
                    lessonCompleteBuilder.setTitle(mContext.getString(R.string.lesson_complete_alert_dialog_title));
                    lessonCompleteBuilder.setMessage(mContext.getString(R.string.lesson_complete_alert_dialog_message));
                    lessonCompleteBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    AlertDialog lessonCompleteDialog = lessonCompleteBuilder.create();
                    lessonCompleteDialog.show();
                }
        }
        return null;
    }

    @Override
    public void sendNextVocabQuestion(int additionalItem) {
        mQuizFragment.GoToNextPage(additionalItem);
    }

    @Override
    public void sendNextPronunciationQuestion(int additionalItem) {
        mPronunciationFragment.goToNextPage(additionalItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Evaluate speechResults
        switch (requestCode) {
            case Constants.REQ_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK && null != data) {

                    // speechResults only present when RESULT_OK is returned in an activity result
                    ArrayList<String> speechResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d(TAG, "speechResults = " + speechResults);

                    // Make a list of lowercase speech results, which will be used in the analysis
                    ArrayList<String> speechResultsLowerCase = new ArrayList<String>();
                    for (int i = 0; i <= speechResults.size(); i++) {
                        String lowercaseResult = speechResults.iterator().next().toLowerCase();
                        speechResultsLowerCase.add(lowercaseResult);
                    }
                    Log.d(TAG, "speechResultsLowerCase = " + speechResultsLowerCase);

                    // Float array of confidence scores
                    float[] confidenceFloatArray = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);
                    Log.d(TAG, "confidenceFloatArray = " + Arrays.toString(confidenceFloatArray));

                    // Finds highest value in confidenceFloatArray
                    float highestConfidenceScore = 0;
                    for (int i = 0; i < confidenceFloatArray.length; i++) {
                        if (confidenceFloatArray[i] > highestConfidenceScore) {
                            highestConfidenceScore = confidenceFloatArray[i];
                        }
                    }
                    Log.d(TAG, "highestConfidenceScore = " + highestConfidenceScore);

                    Log.d(TAG, "contains mCorrectPhrase = " + speechResultsLowerCase.contains(mCorrectPhrase));

                    // If highestConfidenceScore > 0 and the speechResults contain the vocabulary word
                    // Snackbar will display great job, close, or incorrect based on max confidence score
                    if (speechResultsLowerCase.contains(mCorrectPhrase) && highestConfidenceScore >= 0.8) {
                        snackbarGreat();
                        sendNextPronunciationQuestion(1);
                        Log.d(TAG, "contains and confidence, great");
                    } else if (speechResultsLowerCase.contains(mCorrectPhrase) && ((highestConfidenceScore >= 0.5) && (highestConfidenceScore < 0.8))) {
                        snackbarClose();
                        Log.d(TAG, "contains and confidence, close");
                    } else {
                        snackbarFail();
                        Log.d(TAG, "else block contains mCorrectPhrase = " + speechResultsLowerCase.contains(mCorrectPhrase));
                        Log.d(TAG, "contains and confidence, fail");
                    }
                }
            }
            break;
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);

        mCorrectPhrase = options.getString(Constants.PRONUNCIATION_CORRECT_PHRASE)
                .replaceAll("[\\-\\+\\.\\^:,?!;]", "")
                .toLowerCase();
        Log.d(TAG, "correct phrase = " + mCorrectPhrase);
    }

    private void snackbarGreat() {
        Snackbar.make(getWindow().getDecorView().getRootView(), R.string.confidence_great, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void snackbarClose() {
        Snackbar.make(getWindow().getDecorView().getRootView(), R.string.confidence_close, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void snackbarFail() {
        Snackbar.make(getWindow().getDecorView().getRootView(), R.string.confidence_fail, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
