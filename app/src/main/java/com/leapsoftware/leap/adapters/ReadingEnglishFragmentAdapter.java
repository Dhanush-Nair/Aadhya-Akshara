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
import com.leapsoftware.leap.exerciseItems.ReadingExerciseItem;
import com.leapsoftware.leap.exercises.ReadingExercise;
import com.leapsoftware.leap.utils.Attempt;
import com.leapsoftware.leap.utils.VocabLessonActivityCallback;

/**
 * Created by vincentrickey on 3/13/16.
 */
public class ReadingEnglishFragmentAdapter extends RecyclerView.Adapter<ReadingEnglishFragmentAdapter.ViewHolder> {
    private ReadingExercise mReadingExercise;
    private Context mContext;
    private TextToSpeech mTextToSpeech;
    private VocabLessonActivityCallback mVocabLessonActivityCallback;
    public static final String TAG = "ReadingEngFragAd";

    public ReadingEnglishFragmentAdapter(ReadingExercise readingExercise, Context context, TextToSpeech textToSpeech, VocabLessonActivityCallback vocabLessonActivityCallback) {
        this.mReadingExercise = readingExercise;
        this.mContext = context;
        this.mTextToSpeech = textToSpeech;
        this.mVocabLessonActivityCallback = vocabLessonActivityCallback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RelativeLayout mRow;
        public TextView mDialogTextView;
        public CheckBox mCheckBox;
        public String mTextToBeRead;

        public ViewHolder(View itemView) {
            super(itemView);
            mRow = (RelativeLayout) itemView.findViewById(R.id.fragment_reading_item_row_relativelayout);
            mDialogTextView = (TextView) itemView.findViewById(R.id.fragment_reading_item_textview);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.fragment_reading_item_checkbox);
            itemView.setOnClickListener(this);
            itemView.setTag(this);
        }

        @Override
        public void onClick(View v) {
            // Get wordClicked by getting instance of viewholder clicked and calling getAdapterPosition
            ViewHolder viewHolder = (ViewHolder) v.getTag();
            int wordClicked = viewHolder.getAdapterPosition();

            mReadingExercise.getReadingExerciseItemArrayList().get(wordClicked).setIsItemComplete(true, mContext);

            // Spit dialog into two arrays, do not speak character's name
            String dialogClicked = mReadingExercise.getReadingExerciseItemArrayList().get(wordClicked).getDialog();
            String[] splitDialogArray = dialogClicked.split(" ", 2); // Splits dialog into two arrays. Character name is put into first array.
            String dialogLessName = splitDialogArray[1]; // The second dialog without the character's name, which will be spoken by tts.
            mTextToBeRead = dialogLessName;

            try {
                googleSpeakOut();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mReadingExercise.getReadingExerciseItemArrayList().get(wordClicked).isComplete(mContext)) {
                mCheckBox.setChecked(true);
            }

            if (mReadingExercise.isComplete(mContext)) {
                if (mReadingExercise.getAttempt(mContext) == Attempt.FIRSTTRY) {
                    // Set attempt to STUDYING
                    // This will not show the alert dialog after the exercise has been completed after the FIRSTTRY
                    mReadingExercise.setAttempt(Attempt.STUDYING, mContext);

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

        private void googleSpeakOut() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                mTextToSpeech.speak(mTextToBeRead, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
            } else {
                mTextToSpeech.speak(mTextToBeRead, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    @Override
    public ReadingEnglishFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_reading_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReadingEnglishFragmentAdapter.ViewHolder holder, int position) {
        ReadingExerciseItem item = mReadingExercise.getReadingExerciseItemArrayList().get(position);

        if (item.isComplete(mContext) == true) {
            holder.mCheckBox.setChecked(true);
        } else {
            holder.mCheckBox.setChecked(false);
        }

        holder.mDialogTextView.setText(mReadingExercise.getReadingExerciseItemArrayList().get(position).getDialog());
    }

    @Override
    public int getItemCount() {
        return mReadingExercise.getReadingExerciseItemArrayList().size();
    }
}
