package com.leapsoftware.leap.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.exercises.Exercise;
import com.leapsoftware.leap.exercises.QuizExercise;
import com.leapsoftware.leap.lessons.Lesson;
import com.leapsoftware.leap.utils.VocabLessonActivityCallback;

/**
 * Created by vincentrickey on 1/31/16.
 */
public class VocabularyLessonMCFragmentAdapter
        extends RecyclerView.Adapter<VocabularyLessonMCFragmentAdapter.VocabularyLessonMCFragmentViewHolder> {
    public static final String TAG = "VLessonMCFragAdapter";
    public Context mContext;
    public Lesson mLesson;
    public VocabLessonActivityCallback mVocabLessonActivityCallback;

    public VocabularyLessonMCFragmentAdapter(Context context, VocabLessonActivityCallback vocabLessonActivityCallback, Lesson lesson) {
        this.mContext = context;
        this.mVocabLessonActivityCallback = vocabLessonActivityCallback;
        this.mLesson = lesson;
    }

    public class VocabularyLessonMCFragmentViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mRelativeLayout;
        public CardView mCardView;
        public TextView mExerciseTitleTranslated;
        public TextView mExerciseTitleEnglish;
        public ImageView mStatusIcon;
        public ImageView mCurrentExerciseIcon;
        public Button mStartButton;
        public TextView mHighScoreText;
        public View mVerticalLineTop;

        public VocabularyLessonMCFragmentViewHolder(final View itemView) {
            super(itemView);
            this.mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.exercise_cardview_relativelayout);
            this.mCardView = (CardView) itemView.findViewById(R.id.exercise_cardview);
            this.mExerciseTitleTranslated = (TextView) itemView.findViewById(R.id.exercise_card_title_translated);
            this.mExerciseTitleEnglish = (TextView) itemView.findViewById(R.id.exercise_card_title_english);
            this.mStatusIcon = (ImageView) itemView.findViewById(R.id.exercise_card_status_icon);
            this.mCurrentExerciseIcon = (ImageView) itemView.findViewById(R.id.exercise_card_current_exercise_icon);
            this.mStartButton = (Button) itemView.findViewById(R.id.exercise_card_start_button);
            this.mHighScoreText = (TextView) itemView.findViewById(R.id.exercise_card_high_score_textview);
            this.mVerticalLineTop = itemView.findViewById(R.id.exercise_card_vertical_line_top);
            itemView.setTag(R.id.TAG_VIEWHOLDER, this);
            mStartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create instance of viewHolder by calling itemView.getTag()
                    VocabularyLessonMCFragmentViewHolder viewHolder = (VocabularyLessonMCFragmentViewHolder) itemView.getTag(R.id.TAG_VIEWHOLDER);
                    // get position of viewHolder to pass into the exchangeFragment method in the parent activity
                    int position = viewHolder.getAdapterPosition();
                    mVocabLessonActivityCallback.exchangeFragment(position);
                }
            });
        }
    }

    @Override
    public VocabularyLessonMCFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_card, parent, false);
        VocabularyLessonMCFragmentViewHolder vocabularyLessonMCFragmentViewHolder = new VocabularyLessonMCFragmentViewHolder(itemView);

        return vocabularyLessonMCFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(VocabularyLessonMCFragmentViewHolder holder, int position) {
        Exercise exercise = mLesson.getExerciseArrayList().get(position);

        holder.mExerciseTitleEnglish.setText(exercise.getExerciseNameEnglish());
        holder.mExerciseTitleTranslated.setText(exercise.getExerciseNameTranslated());

        switch (position) {
            case 0:
                holder.mCurrentExerciseIcon.setImageResource(R.drawable.leap_pronunciation_icon);
                holder.mVerticalLineTop.setVisibility(View.INVISIBLE);
                break;
            case 1:
                holder.mCurrentExerciseIcon.setImageResource(R.drawable.leap_study_icon);
                break;
            case 2:
                holder.mCurrentExerciseIcon.setImageResource(R.drawable.leap_microphone_icon);
                break;
            case 3:
                holder.mCurrentExerciseIcon.setImageResource(R.drawable.leap_lesson_icon);
                QuizExercise quizExercise = (QuizExercise) exercise;
                float highScore = quizExercise.getSavedHighScore(mContext);
                if (highScore > 0) {
                    String highScoreText = String.format(mContext.getString(R.string.quiz_exercise_high_score_equals), Float.toString(highScore));
                    holder.mHighScoreText.setText(highScoreText);
                }
                break;
        }

        // If the current exercise (the first incomplete exercise) is the exercise at a given position, change the background to grey
        if (mLesson.getCurrentExercise(mContext) == exercise) {
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.cardviewCurrentItem));
            holder.mStatusIcon.setImageResource(R.drawable.ic_keyboard_arrow_right_white_24dp);
            holder.mStatusIcon.setBackgroundResource(R.drawable.circle_status_arrow_button_background);
            holder.mStartButton.setBackgroundResource(R.drawable.rounded_button_primary);
        }

        // If the current exercise is not the exercise at a given position, disable the start button
        // Users must start with the first exercise, not the second or third etc
        /*if (mLesson.getCurrentExercise(mContext) != exercise) {
            holder.mStartButton.setEnabled(false);
        }*/

        // If an exercise is complete, change the background color to green
        if (exercise.isComplete(mContext)) {
            holder.mStatusIcon.setImageResource(R.drawable.leap_check_mark_icon);
            //holder.mStartButton.setEnabled(true);
            holder.mStartButton.setBackgroundResource(R.drawable.rounded_button_primary_light);
            holder.mExerciseTitleTranslated.setTypeface(null, Typeface.ITALIC);
            holder.mExerciseTitleTranslated.setTextColor(ContextCompat.getColor(mContext, R.color.textColorSecondary));
        }

    }

    @Override
    public int getItemCount() {
        return mLesson.getExerciseArrayList().size();
    }
}
