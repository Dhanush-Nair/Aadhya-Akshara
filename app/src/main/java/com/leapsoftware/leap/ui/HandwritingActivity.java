package com.leapsoftware.leap.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.dataObject.Letter;
import com.leapsoftware.leap.utils.Constants;

public class HandwritingActivity extends AppCompatActivity {
    protected Letter mLetterSelected;
    protected FragmentManager mFragmentManager;
    protected HandwritingFragment mHandwritingFragment;
    public static final String TAG = "HandwritingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handwriting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_handwriting_toolbar_title);
        setSupportActionBar(toolbar);

        // Get fragment manager to exchange framelayout with HandwritingFragment
        mFragmentManager = getSupportFragmentManager();

        // Get mLetterSelected from the intent
        Intent extras = getIntent();
        mLetterSelected = extras.getParcelableExtra(Constants.KEY_EXTRAS_LETTER_SELECTED);
        Log.d(TAG, "letter selected = " + mLetterSelected.getUpperAndLower());

        // Create new instance of HandrwritingFragment and pass mLetterSelected using a bundle
        mHandwritingFragment = new HandwritingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_EXTRAS_LETTER_SELECTED, mLetterSelected);
        mHandwritingFragment.setArguments(bundle);

        // Exchange the framelaout with mHandwritingFragment
        mFragmentManager.beginTransaction().replace(R.id.activity_handwriting_framelayout, mHandwritingFragment).commit();

    }

}
