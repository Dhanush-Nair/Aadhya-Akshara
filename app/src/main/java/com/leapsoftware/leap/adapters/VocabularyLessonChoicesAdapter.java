package com.leapsoftware.leap.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.lessons.Lesson;
import com.leapsoftware.leap.ui.VocabularyLessonActivity;
import com.leapsoftware.leap.units.Unit;
import com.leapsoftware.leap.utils.Constants;

/**
 * Created by vincentrickey on 11/26/15.
 */
public class VocabularyLessonChoicesAdapter extends RecyclerView.Adapter<VocabularyLessonChoicesAdapter.VocabularyLessonChoicesViewHolder> {
    private Context mContext;
    private Unit mUnit;

    public VocabularyLessonChoicesAdapter(Context context, Unit unit) {
        this.mContext = context;
        this.mUnit = unit;
    }

    public class VocabularyLessonChoicesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView mCardView;
        public RelativeLayout mRow;
        public TextView mLessonNumber;
        public TextView mTitleTranslated;
        public TextView mTitleEnglish;
        public ImageView mStatusIcon;


        public VocabularyLessonChoicesViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.vocabularylessonchoices_list_cardview);
            mRow = (RelativeLayout) itemView.findViewById(R.id.vocabularylessonchoices_list_row);
            mLessonNumber = (TextView) itemView.findViewById(R.id.vocabularylessonchoices_list_lesson_number);
            mTitleTranslated = (TextView) itemView.findViewById(R.id.vocabularylessonchoices_list_title_textview);
            mTitleEnglish = (TextView) itemView.findViewById(R.id.vocabularylessonchoices_list_english_title_textview);
            mStatusIcon = (ImageView) itemView.findViewById(R.id.vocabularylessonchoices_list_status_icon);
            itemView.setTag(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Create instance of viewHolder by calling v.getTag() from the clicked textview
            VocabularyLessonChoicesViewHolder viewHolder = (VocabularyLessonChoicesViewHolder) v.getTag();

            // Use position of viewHolder to get instance of Vocabulary object clicked
            int lessonClicked = viewHolder.getAdapterPosition();
            Lesson lessonSelected = mUnit.getLessonArrayList().get(lessonClicked);

            Intent intent = new Intent(mContext, VocabularyLessonActivity.class);
            intent.putExtra(Constants.KEY_EXTRAS_LESSON_SELECTED, lessonSelected);
            mContext.startActivity(intent);
        }
    }

    @Override
    public VocabularyLessonChoicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate layout and return viewholder
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_vocabularylessonchoices_list_item, parent, false);
        VocabularyLessonChoicesViewHolder vocabularyLessonChoicesViewHolder = new VocabularyLessonChoicesViewHolder(itemView);
        return vocabularyLessonChoicesViewHolder;
    }

    @Override
    public void onBindViewHolder(VocabularyLessonChoicesViewHolder viewHolder, int i) {
        Lesson lesson = mUnit.getLessonArrayList().get(i);

        viewHolder.mTitleTranslated.setText(lesson.getTitleTranslated().replace("\"", ""));
        viewHolder.mTitleEnglish.setText(lesson.getTitleEnglish().replace("\"", ""));
        viewHolder.mLessonNumber.setText(String.valueOf(i + 1));

        // If lesson is complete, add the completed check icon
        if (lesson.isComplete(mContext)) {
            viewHolder.mStatusIcon.setImageResource(R.drawable.leap_check_mark_icon);
            viewHolder.itemView.setOnClickListener(viewHolder);
        }

        // If the current lesson (the first incomplete lesson) is the lesson at a given position, change the background to grey
        if (mUnit.getCurrentLesson(mContext) == lesson) {
            viewHolder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.cardviewCurrentItem));
        }
    }

    @Override
    public int getItemCount() {
        return mUnit.getLessonArrayList().size();
    }
}