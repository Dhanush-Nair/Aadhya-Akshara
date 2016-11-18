package com.leapsoftware.leap.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.adapters.VocabularyLessonChoicesAdapter;
import com.leapsoftware.leap.units.Unit;
import com.leapsoftware.leap.utils.Constants;

/**
 * Created by vincentrickey on 11/26/15.
 */
public class VocabularyLessonChoicesActivity extends AppCompatActivity {
    private Context mContext;
    public static final String TAG = "VocabLessonChoicesActy";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_lesson_choices);

        mContext = this;

        Intent originIntent = getIntent();
        Unit unit = originIntent.getParcelableExtra(Constants.KEY_EXTRAS_UNIT);
        Log.d(TAG, "unit = " + unit);

        // Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(unit.getUnitName());
        setSupportActionBar(toolbar);

        // Set the recyclerview to the xml layout
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.vocabulary_lesson_choices_recyclerview);
        recyclerView.setHasFixedSize(true);

        // Set LayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        // Set VocabularySectionChoicesAdapter to recyclerView
        VocabularyLessonChoicesAdapter vocabularyLessonChoicesAdapter = new VocabularyLessonChoicesAdapter(mContext, unit);
        recyclerView.setAdapter(vocabularyLessonChoicesAdapter);
    }
}
