package com.leapsoftware.leap.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.adapters.AlphabetActivityFragmentPagerAdapter;
import com.leapsoftware.leap.dataObject.Letter;
import com.leapsoftware.leap.utils.AlphabetActivityCallback;
import com.leapsoftware.leap.utils.Constants;

import java.util.ArrayList;

/**
 * Created by vincentrickey on 1/23/16.
 */
public class AlphabetActivity extends AppCompatActivity implements AlphabetActivityCallback {
    protected Context mContext;
    protected FragmentManager mFragmentManager;
    protected ArrayList<Letter> mLetterArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alphabet);

        mContext = this;

        mFragmentManager = this.getSupportFragmentManager();

        // Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.alphabet_activity_title);
        setSupportActionBar(toolbar);

        // Instantiate new LetterArrayList
        mLetterArrayList = getLetterArrayList();

        // Create fragment pager adapter
        AlphabetActivityFragmentPagerAdapter alphabetActivityFragmentPagerAdapter = new AlphabetActivityFragmentPagerAdapter(mFragmentManager, mLetterArrayList, mContext);

        // Set AlphabetActivityPagerAdapter to viewpager
        ViewPager alphabetViewPager = (ViewPager) findViewById(R.id.alphabet_activity_viewpager);
        alphabetViewPager.setAdapter(alphabetActivityFragmentPagerAdapter);

        // Set tablayout with alphabetviewpager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.alphabet_tabs);
        tabLayout.setupWithViewPager(alphabetViewPager);
    }

    @Override
    public ArrayList<Letter> getLetterArrayList() {
        Intent originIntent = getIntent();
        ArrayList<Letter> letterArrayList = originIntent.getParcelableArrayListExtra(Constants.KEY_EXTRAS_LETTERSARRAYLIST);
        return letterArrayList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AlphabetActivity.this,FirstActivity.class));
    }
}


