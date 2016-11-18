package com.leapsoftware.leap.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.adapters.AlphabetHandwritingChoicesAdapter;
import com.leapsoftware.leap.dataObject.Letter;
import com.leapsoftware.leap.utils.AlphabetActivityCallback;

import java.util.ArrayList;

public class AlphabetHandwritingChoicesFragment extends android.support.v4.app.Fragment {
    protected AlphabetActivityCallback mAlphabetActivityCallback;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LETTERARRAYLIST = "argLetterArrayList";

    // local instance of extra passed from newInstance parameter
    public ArrayList<Letter> mLetterArrayList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AlphabetHandwritingChoicesFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param letterArrayList Parameter 1.
     * @return A new instance of fragment.
     */
    public static AlphabetHandwritingChoicesFragment newInstance(ArrayList<Letter> letterArrayList) {
        AlphabetHandwritingChoicesFragment fragment = new AlphabetHandwritingChoicesFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_LETTERARRAYLIST, letterArrayList);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mAlphabetActivityCallback = (AlphabetActivityCallback) context;
        } catch (ClassCastException castException) {
            //The activity does not implement the listener.
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getArguments from new instance
        if (getArguments() != null) {
            mLetterArrayList = getArguments().getParcelableArrayList(ARG_LETTERARRAYLIST);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_alphabet_handwriting_choices, container, false);

        // Set the recyclerview to the xml layout
        RecyclerView recyclerView = (RecyclerView) rootview.findViewById(R.id.fragment_alphabet_handwriting_choices_recyclerview);
        recyclerView.setHasFixedSize(true);

        // Set LayoutManager
        android.support.v7.widget.GridLayoutManager layoutManager = new android.support.v7.widget.GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        // Set PronunciationAdapter to recycle view
        AlphabetHandwritingChoicesAdapter pronunciationAdapter = new AlphabetHandwritingChoicesAdapter(getActivity(), mLetterArrayList, mAlphabetActivityCallback);
        recyclerView.setAdapter(pronunciationAdapter);

        return rootview;
    }

    @Override
    public void onDetach() {
        mAlphabetActivityCallback = null; // set to null to avoid memory leaks
        super.onDetach();
    }
}
