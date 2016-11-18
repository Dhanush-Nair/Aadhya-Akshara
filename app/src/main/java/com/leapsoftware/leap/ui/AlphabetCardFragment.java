
package com.leapsoftware.leap.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.adapters.AlphabetCardFragmentAdapter;
import com.leapsoftware.leap.dataObject.Letter;

import java.util.ArrayList;


public class AlphabetCardFragment extends android.support.v4.app.Fragment {

    //private ArrayList<Letter> mLetterArrayList;
    protected LinearLayoutManager mLinearLayoutManager;
    protected RecyclerView mRecyclerView;

    protected Context mContext;
    protected AlphabetCardFragmentAdapter mAlphabetCardFragmentAdapter;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LETTERARRAYLIST = "argLetterArrayList";

    // local instance of extra passed from newInstance parameter
    public static ArrayList<Letter> mLetterArrayList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AlphabetCardFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param letterArrayList Parameter 1.
     * @return A new instance of fragment.
     */
    public static AlphabetCardFragment newInstance(ArrayList<Letter> letterArrayList) {
        AlphabetCardFragment fragment = new AlphabetCardFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_LETTERARRAYLIST, letterArrayList);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // indicate that this fragment should override onOptionsItemSelected(MenuItem item)
        setHasOptionsMenu(true);
        mContext = getContext();

        // getArguments from new instance
        if (getArguments() != null) {
            mLetterArrayList = getArguments().getParcelableArrayList(ARG_LETTERARRAYLIST);
            Log.d("AlphabetStudyFrag", "mLetterArrayList getArguments onCreate = " + mLetterArrayList);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_alphabet_card, container, false);

        // Set RecyclerView to xml layout
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_alphabet_card_recycler_view); // sets mRecyclerView to the recycler_view in fragment_main_activity.xml
        mRecyclerView.setHasFixedSize(true); // use this setting to improve performance if you know that changes in content do not change the layout size of the RecyclerView

        // Set LinearLayoutManager
        mLinearLayoutManager = new LinearLayoutManager(getActivity()); // instantiates mLayoutManager to new LinearLayoutManager in this activity
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager); // sets mLayoutManager as the linearlayoutmanager for mRecyclerView

        // Create new AlphabetStudyFragmentCardAdapter to be used in onOptionsItemSelected
        mAlphabetCardFragmentAdapter = new AlphabetCardFragmentAdapter(mLetterArrayList, getActivity()); // add definitions as parameter
        mRecyclerView.setAdapter(mAlphabetCardFragmentAdapter);

        return rootView;
    }

}

