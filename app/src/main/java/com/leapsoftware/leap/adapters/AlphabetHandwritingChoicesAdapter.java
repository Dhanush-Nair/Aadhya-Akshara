package com.leapsoftware.leap.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.dataObject.Letter;
import com.leapsoftware.leap.ui.HandwritingActivity;
import com.leapsoftware.leap.utils.AlphabetActivityCallback;
import com.leapsoftware.leap.utils.Constants;

import java.util.ArrayList;

/**
 * Created by vincentrickey on 10/17/15.
 */
public class AlphabetHandwritingChoicesAdapter extends RecyclerView.Adapter<AlphabetHandwritingChoicesAdapter.HandwritingGridViewHolder> {

    private Context mContext;
    private ArrayList<Letter> mLettersArrayList;
    private AlphabetActivityCallback mAlphabetActivityCallback;


    public AlphabetHandwritingChoicesAdapter(Context context, ArrayList<Letter> lettersArrayList, AlphabetActivityCallback alphabetActivityCallback) {
        this.mContext = context;
        this.mLettersArrayList = lettersArrayList;
        this.mAlphabetActivityCallback = alphabetActivityCallback;
    }

    public class HandwritingGridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener { // // Provide a direct reference to each of the views within a data item. Used to cache the views within the item layout for fast access.
        public Letter mLetter; // instance of letter (data)
        public TextView vGridItemTextView;

        public HandwritingGridViewHolder(View itemView) {
            super(itemView); // Stores itemView in public final member variable that can be used to access the context from any ViewHolder instance

            vGridItemTextView = (TextView) itemView.findViewById(R.id.fragment_alphabet_handwriting_choices_item_textview);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, HandwritingActivity.class);
            intent.putExtra(Constants.KEY_EXTRAS_LETTER_SELECTED, mLetter);
            mContext.startActivity(intent); //starts without transitions
        }
    }

    @Override
    public HandwritingGridViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) { // Inflate layout and return viewHolder
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_alphabet_handwriting_choices_item, viewGroup, false);
        HandwritingGridViewHolder handwritingGridViewHolder = new HandwritingGridViewHolder(itemView);

        return handwritingGridViewHolder;
    }

    @Override
    public void onBindViewHolder(final HandwritingGridViewHolder handwritingGridViewHolder, int i) {
        handwritingGridViewHolder.mLetter = mLettersArrayList.get(i);
        handwritingGridViewHolder.vGridItemTextView.setText(mLettersArrayList.get(i).getUpperAndLower());

    }

    @Override
    public int getItemCount() {
        return 26;
    }
}