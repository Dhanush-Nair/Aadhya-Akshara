package com.leapsoftware.leap.adapters;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.exercises.ReadingExercise;
import com.leapsoftware.leap.utils.VocabLessonActivityCallback;

/**
 * Created by vincentrickey on 3/13/16.
 */
public class ReadingTranslatedFragmentAdapter extends RecyclerView.Adapter<ReadingTranslatedFragmentAdapter.ViewHolder> {
    private ReadingExercise mReadingExercise;
    private Context mContext;
    private TextToSpeech mTextToSpeech;
    private VocabLessonActivityCallback mVocabLessonActivityCallback;
    public static final String TAG = "ReadingTranFragAd";

    public ReadingTranslatedFragmentAdapter(ReadingExercise readingExercise, Context context, TextToSpeech textToSpeech, VocabLessonActivityCallback vocabLessonActivityCallback) {
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

            // Spit dialog into two arrays, do not speak character's name
            String dialogClicked = mReadingExercise.getReadingExerciseItemArrayList().get(wordClicked).getTranslatedDialog();
            String[] splitDialogArray = dialogClicked.split(" ", 2); // Splits dialog into two arrays. Character name is put into first array.
            String translatedDialogLessName = splitDialogArray[1]; // The second dialog without the character's name, which will be spoken by tts.
            mTextToBeRead = translatedDialogLessName;

            try {
                googleSpeakOut();
            } catch (Exception e) {
                e.printStackTrace();
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
    public ReadingTranslatedFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_reading_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReadingTranslatedFragmentAdapter.ViewHolder holder, int position) {
        holder.mCheckBox.setVisibility(View.INVISIBLE);
        holder.mDialogTextView.setText(mReadingExercise.getReadingExerciseItemArrayList().get(position).getTranslatedDialog());
    }

    @Override
    public int getItemCount() {
        return mReadingExercise.getReadingExerciseItemArrayList().size();
    }
}
