package com.leapsoftware.leap.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.dataObject.Letter;
import com.leapsoftware.leap.ui.AlphabetActivity;
import com.leapsoftware.leap.ui.VocabularyLessonChoicesActivity;
import com.leapsoftware.leap.units.Unit;
import com.leapsoftware.leap.utils.Constants;

import java.util.ArrayList;

/**
 * Created by vincentrickey on 12/24/15.
 */
public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MainActivityItemViewHolder> {
    private Context mContext;
    private ArrayList<Letter> mLetterArrayList;
    private ArrayList<Unit> mUnitArrayList;
    private ArrayList<String> mMasterUnitNames;
    private ArrayList<String> mMasterUnitDescriptions;
    private static final String TAG = "MainActivityAdapter";

    public MainActivityAdapter(Context context, ArrayList<Letter> letterArrayList, ArrayList<Unit> unitArrayList, ArrayList<String> masterUnitNames, ArrayList<String> masterUnitDescriptions) {
        this.mContext = context;
        this.mLetterArrayList = letterArrayList;
        this.mUnitArrayList = unitArrayList;
        this.mMasterUnitNames = masterUnitNames;
        this.mMasterUnitDescriptions = masterUnitDescriptions;
    }

    public class MainActivityItemViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mCardLinearLayout;
        public ImageView mUnitIcon;
        public TextView mTitle;
        public TextView mDescription;
        public ImageView mArrowIcon;
        public View.OnClickListener mMainCardOnClickListener;

        public MainActivityItemViewHolder(View view) {
            super(view);
            mCardLinearLayout = (LinearLayout) view.findViewById(R.id.activity_main_card_item_linearlayout_container);
            mUnitIcon = (ImageView) view.findViewById(R.id.activity_main_card_item_unit_icon);
            mTitle = (TextView) view.findViewById(R.id.activity_main_card_item_title);
            mDescription = (TextView) view.findViewById(R.id.activity_main_card_item_description);
            mArrowIcon = (ImageView) view.findViewById(R.id.activity_main_card_item_arrow_icon);
            view.setTag(this);

            mMainCardOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivityItemViewHolder mainActivityItemViewHolder = (MainActivityItemViewHolder) v.getTag();
                    int position = mainActivityItemViewHolder.getAdapterPosition();

                    if (position == 0) {
                        Intent alphabetIntent = new Intent(mContext, AlphabetActivity.class);
                        alphabetIntent.putParcelableArrayListExtra(Constants.KEY_EXTRAS_LETTERSARRAYLIST, mLetterArrayList);
                        mContext.startActivity(alphabetIntent);
                    } else {
                        Intent vocabularyIntent = new Intent(mContext, VocabularyLessonChoicesActivity.class);
                        Unit unit = mUnitArrayList.get(position - 1);
                        vocabularyIntent.putExtra(Constants.KEY_EXTRAS_UNIT, unit);
                        mContext.startActivity(vocabularyIntent);
                    }
                }
            };
            view.setOnClickListener(mMainCardOnClickListener);
        }
    }

    @Override
    public MainActivityItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_card_item, parent, false);
        return new MainActivityItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MainActivityItemViewHolder holder, int position) {

        switch (position) {
            case 0:
                holder.mUnitIcon.setImageResource(R.drawable.leap_alphabet_icon);
                break;
            case 1:
                holder.mUnitIcon.setImageResource(R.drawable.leap_vocabulary_icon);
                break;
            case 2:
                holder.mUnitIcon.setImageResource(R.drawable.leap_vocabulary_icon);
                break;
            case 3:
                holder.mUnitIcon.setImageResource(R.drawable.leap_vocabulary_icon);
                break;
            default:
                holder.mUnitIcon.setImageResource(R.drawable.leap_vocabulary_icon);
                break;
        }
        holder.mTitle.setText(mMasterUnitNames.get(position));
        holder.mDescription.setText(mMasterUnitDescriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return mMasterUnitNames.size();
    }

}
