package com.leapsoftware.leap.adapters;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.dataObject.Letter;

import java.util.ArrayList;

/**
 * Created by vincentrickey on 8/23/15.
 */
public class AlphabetCardFragmentAdapter extends RecyclerView.Adapter<AlphabetCardFragmentAdapter.FlashcardViewHolder> {

    private Context mContext;
    public String TAG = AlphabetCardFragmentAdapter.class.getName();
    private ArrayList<Letter> mLetters;

    public AlphabetCardFragmentAdapter(ArrayList<Letter> letters, Context context) {
        this.mLetters = letters;
        this.mContext = context;
    }

    public class FlashcardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView mFrontCardView;
        public CardView mBackCardView;
        public TextView mLetterTextView;
        public TextView mBackLetterTextView;
        public TextView mDefinitionTextView;
        public TextView mTransliteration;
        public ImageButton mSpeakerButton;

        public FlashcardViewHolder(View v) {
            super(v);
            mFrontCardView = (CardView) v.findViewById(R.id.flashcard_front_cardview);
            mBackCardView = (CardView) v.findViewById(R.id.flashcard_back_cardview);
            mLetterTextView = (TextView) v.findViewById(R.id.flashcard_term_textview);
            mBackLetterTextView = (TextView) v.findViewById(R.id.flashcard_back_term_textview);
            mDefinitionTextView = (TextView)v.findViewById(R.id.flashcard_back_definition_textview);
            mTransliteration = (TextView)v.findViewById(R.id.flashcard_back_transliteration_textview);
            mSpeakerButton = (ImageButton)v.findViewById(R.id.flashcard_back_imageButton_speaker);
            v.setTag(this);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            FlashcardViewHolder flashcardViewHolder = (FlashcardViewHolder) v.getTag();

            int cardClicked = flashcardViewHolder.getAdapterPosition();

            // indicates the target state to animate to
            mLetters.get(cardClicked).mIsFrontShowing = !mLetters.get(cardClicked).mIsFrontShowing;

            AnimatorSet cardInAnimation =  (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.flip_left_in);
            AnimatorSet cardOutAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.flip_right_out);

            if (mLetters.get(cardClicked).mIsFrontShowing) {
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
    public FlashcardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_flashcard_item, viewGroup, false);
        FlashcardViewHolder flashcardViewHolder = new FlashcardViewHolder(itemView);
        return flashcardViewHolder;
    }

    @Override
    public void onBindViewHolder(FlashcardViewHolder flashcardViewHolder, final int i) {
        flashcardViewHolder.mLetterTextView.setText(mLetters.get(i).getUpperAndLower());
        flashcardViewHolder.mBackLetterTextView.setText(mLetters.get(i).getUpperAndLower());
        flashcardViewHolder.mDefinitionTextView.setText(mLetters.get(i).getIPAArrayList().toString().replaceAll("\\[", "").replaceAll("\\]", ""));
        flashcardViewHolder.mTransliteration.setText(mLetters.get(i).getTransliterationsArrayList().toString().replaceAll("\\[", "").replaceAll("\\]",""));
        flashcardViewHolder.mSpeakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, mLetters.get(position).getUpperAndLower(), Toast.LENGTH_LONG).show();
                Uri uriPath = Uri.parse("android.resource://com.leapsoftware.leap/raw/" + mLetters.get(i).getAudioFileName());
                MediaPlayer mediaPlayer = MediaPlayer.create(mContext, uriPath);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.start();
            }
        });

        // when we bind the data to the viewHolder make sure the front and back of the cards are not rotated
        flashcardViewHolder.mFrontCardView.setRotationY(0f);
        flashcardViewHolder.mBackCardView.setRotationY(0f);

        // Show the front view of the card if the front is showing
        flashcardViewHolder.mFrontCardView.setAlpha(mLetters.get(i).getIsFrontShowing() ? 1f : 0f);
        // Hide the back view of the card if the front is showing
        flashcardViewHolder.mBackCardView.setAlpha(mLetters.get(i).getIsFrontShowing() ? 0f : 1f);
    }

    @Override
    public int getItemCount() {
        return mLetters.size();
    }
}