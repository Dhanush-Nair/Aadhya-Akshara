package com.leapsoftware.leap.adapters;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.exerciseItems.VocabWordExerciseItem;

import java.util.ArrayList;


public class VocabWordFragmentCardAdapter extends RecyclerView.Adapter<VocabWordFragmentCardAdapter.VocabWordCardViewHolder> {
    public ArrayList<VocabWordExerciseItem> mVocabWordExerciseItemList;
    public Context mContext;
    public TextToSpeech mTTS;

    public VocabWordFragmentCardAdapter(ArrayList<VocabWordExerciseItem> vocabWordExerciseItemList, Context context, TextToSpeech textToSpeech) {
        this.mVocabWordExerciseItemList = vocabWordExerciseItemList;
        this.mContext = context;
        this.mTTS = textToSpeech;
    }

    public class VocabWordCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView mFrontCardView;
        public CardView mBackCardView;
        public TextView mVocabularyWord;
        public TextView mDefinitionTextView;
        public TextView mBackVocabWord;
        public ImageButton vSpeakerButton;

        public VocabWordCardViewHolder(View view) {
            super(view);
            mFrontCardView = (CardView) view.findViewById(R.id.flashcard_front_cardview);
            mBackCardView = (CardView) view.findViewById(R.id.flashcard_back_cardview);
            mVocabularyWord = (TextView) view.findViewById(R.id.flashcard_term_textview);
            mBackVocabWord = (TextView)view.findViewById(R.id.flashcard_back_term_textview);
            mDefinitionTextView = (TextView)view.findViewById(R.id.flashcard_back_definition_textview);
            vSpeakerButton = (ImageButton) view.findViewById(R.id.flashcard_back_imageButton_speaker);
            view.setTag(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            VocabWordCardViewHolder flashcardViewHolder = (VocabWordCardViewHolder) v.getTag();
            int cardClicked = flashcardViewHolder.getAdapterPosition();

            // Indicates target state to animate to
            mVocabWordExerciseItemList.get(cardClicked).mIsFrontShowing = !mVocabWordExerciseItemList.get(cardClicked).mIsFrontShowing;

            AnimatorSet cardInAnimation =  (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.flip_left_in);
            AnimatorSet cardOutAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.flip_right_out);

            if (mVocabWordExerciseItemList.get(cardClicked).mIsFrontShowing) {
                // ensure the initial visibility is set correctly
                flashcardViewHolder.mFrontCardView.setAlpha(0f);
                flashcardViewHolder.mBackCardView.setAlpha(1f);

                cardInAnimation.setTarget(flashcardViewHolder.mFrontCardView);
                cardOutAnimation.setTarget(flashcardViewHolder.mBackCardView);
            } else {
                // ensure the initial visibility is set correctly
                flashcardViewHolder.mFrontCardView.setAlpha(1f);
                flashcardViewHolder.mBackCardView.setAlpha(0f);

                // set the target view for each animation
                cardInAnimation.setTarget(flashcardViewHolder.mBackCardView);
                cardOutAnimation.setTarget(flashcardViewHolder.mFrontCardView);
            }
            // @formatter:on

            cardInAnimation.start();
            cardOutAnimation.start();
        }
    }

    @Override
    public VocabWordCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_flashcard_item, parent, false);
        return new VocabWordCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VocabWordCardViewHolder holder, final int position) {
        // get VocabWordExerciseItem at position
        final VocabWordExerciseItem item = mVocabWordExerciseItemList.get(position);

        holder.mVocabularyWord.setText(item.getVocabWord());
        holder.mBackVocabWord.setText(item.getVocabWord());
        holder.mDefinitionTextView.setText(item.getTranslatedVocabWord());

        // TODO Set text size in xml and scale to card width
        holder.mVocabularyWord.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
        holder.mDefinitionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
        holder.mBackVocabWord.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);

        // when we bind the data to the viewHolder make sure the front and back of the cards are not rotated
        holder.mFrontCardView.setRotationY(0f);
        holder.mBackCardView.setRotationY(0f);

        // Show the front view of the card if the front is showing
        holder.mFrontCardView.setAlpha(item.isFrontShowing() ? 1f : 0f);
        // Hide the back view of the card if the front is showing
        holder.mBackCardView.setAlpha(item.isFrontShowing() ? 0f : 1f);

        // onClick, google speaks out the vocabulary word
        holder.vSpeakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    googleSpeakOut(item.getVocabWord());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVocabWordExerciseItemList.size();
    }

    public void googleSpeakOut(String textToBeRead) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            mTTS.speak(textToBeRead, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
        } else {
            mTTS.speak(textToBeRead, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

}