package com.leapsoftware.leap.adapters;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.dataObject.Letter;

import java.util.ArrayList;


public class AlphabetListFragmentAdapter extends RecyclerView.Adapter<AlphabetListFragmentAdapter.ViewHolder> {
    private ArrayList<Letter> mLetters;
    private Context mContext;

    public AlphabetListFragmentAdapter(ArrayList<Letter> letters, Context context) {
        this.mLetters = letters;
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout vVocabularyRow;
        public TextView vVocabularyWord;
        public TextView vDefinition;
        public ImageButton vSpeakerButton;


        public ViewHolder(View view) {
            super(view);
            vVocabularyRow = (RelativeLayout) view.findViewById(R.id.fragment_flashcards_list_row);
            vVocabularyWord = (TextView) view.findViewById(R.id.fragment_flashcards_list_textview);
            vDefinition = (TextView) view.findViewById(R.id.fragment_flashcards_list_definition_textview);
            vSpeakerButton = (ImageButton) view.findViewById(R.id.fragment_flashcards_list_imageButton_speaker);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_alphabet_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.vVocabularyWord.setText(mLetters.get(position).getUpperAndLower());
        holder.vDefinition.setText(mLetters.get(position).getTransliterationsArrayList().toString().replaceAll("\\[|\\]", ""));
        holder.vSpeakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, mLetters.get(position).getUpperAndLower(), Toast.LENGTH_LONG).show();
                Uri uriPath = Uri.parse("android.resource://com.leapsoftware.leap/raw/" + mLetters.get(position).getAudioFileName());
                MediaPlayer mediaPlayer = MediaPlayer.create(mContext, uriPath);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 26;
    }
}
