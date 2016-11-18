package com.leapsoftware.leap.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.exerciseItems.VocabWordExerciseItem;
import com.leapsoftware.leap.exercises.VocabWordsExercise;
import com.leapsoftware.leap.utils.Attempt;
import com.leapsoftware.leap.utils.VocabLessonActivityCallback;


public class VocabWordFragmentListAdapter extends RecyclerView.Adapter<VocabWordFragmentListAdapter.ViewHolder> {
    public VocabWordsExercise mVocabWordsExercise;
    public Context mContext;
    public TextToSpeech mTTS;
    public VocabLessonActivityCallback mVocabLessonActivityCallback;
    private static final String TAG = "VocabFragListAdapter";

    public VocabWordFragmentListAdapter(VocabWordsExercise vocabWordsExercise, Context context, TextToSpeech textToSpeech, VocabLessonActivityCallback vocabLessonActivityCallback) {
        this.mVocabWordsExercise = vocabWordsExercise;
        this.mContext = context;
        this.mTTS = textToSpeech;
        this.mVocabLessonActivityCallback = vocabLessonActivityCallback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RelativeLayout vVocabularyRow;
        public TextView vVocabularyWord;
        public TextView vDefinition;
        public String mTextToBeRead;
        public CheckBox mCheckBox;

        public ViewHolder(View view) {
            super(view);
            vVocabularyRow = (RelativeLayout) view.findViewById(R.id.fragment_vocabwords_list_row_relativelayout);
            vVocabularyWord = (TextView) view.findViewById(R.id.fragment_vocabwords_list_textview);
            vDefinition = (TextView) view.findViewById(R.id.fragment_vocabwords_list_definition_textview);
            mCheckBox = (CheckBox) view.findViewById(R.id.fragment_vocabwords_list_checkbox);
            view.setOnClickListener(this);
            view.setTag(this);
        }

        private void googleSpeakOut() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                mTTS.speak(mTextToBeRead, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
            } else {
                mTTS.speak(mTextToBeRead, TextToSpeech.QUEUE_FLUSH, null);
            }
        }

        @Override
        public void onClick(View v) {
            // Get wordClicked by getting instance of viewholder clicked and calling getAdapterPosition
            ViewHolder viewHolder = (ViewHolder) v.getTag();
            int wordClicked = viewHolder.getAdapterPosition();

            // Get vocabWordExerciseItem clicked, setIsItemComplete to true
            VocabWordExerciseItem item = mVocabWordsExercise.getVocabWordExerciseItemArrayList().get(wordClicked);
            item.setIsItemComplete(true, mContext);

            // mTextToBeRead set to vocabWordExerciseItem's getVocabWord
            mTextToBeRead = item.getVocabWord();

            // google speaks the text of the vocabWordExerciseItem
            try {
                googleSpeakOut();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (item.isComplete(mContext)) {
                mCheckBox.setChecked(true);
            }


            if (mVocabWordsExercise.isComplete(mContext)) {
                if (mVocabWordsExercise.getAttempt(mContext) == Attempt.FIRSTTRY) {
                    // Set attempt to STUDYING
                    // This will not show the alert dialog after the exercise has been completed after the FIRSTTRY
                    mVocabWordsExercise.setAttempt(Attempt.STUDYING, mContext);

                    // Show alert dialog after completing the exercise on the FIRSTTRY
                    AlertDialog.Builder exerciseCompleteBuilder = new AlertDialog.Builder(mContext);
                    exerciseCompleteBuilder.setTitle(mContext.getString(R.string.exercise_complete_alert_dialog_title));
                    exerciseCompleteBuilder.setMessage(mContext.getString(R.string.exercise_complete_alert_dialog_message));
                    exerciseCompleteBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // exchange fragment to bring user back to VocabularyLessonMainContentFragment
                            mVocabLessonActivityCallback.exchangeFragment(5);
                        }
                    });
                    AlertDialog exerciseCompleteDialog = exerciseCompleteBuilder.create();
                    exerciseCompleteDialog.show();
                }
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_vocabwords_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        VocabWordExerciseItem item = mVocabWordsExercise.getVocabWordExerciseItemArrayList().get(position);

        if (item.isComplete(mContext) == true) {
            holder.mCheckBox.setChecked(true);
        } else {
            holder.mCheckBox.setChecked(false);
        }

        holder.vVocabularyWord.setText(item.getVocabWord());
        holder.vDefinition.setText(item.getTranslatedVocabWord());
    }

    @Override
    public int getItemCount() {
        return mVocabWordsExercise.getVocabWordExerciseItemArrayList().size();
    }
}