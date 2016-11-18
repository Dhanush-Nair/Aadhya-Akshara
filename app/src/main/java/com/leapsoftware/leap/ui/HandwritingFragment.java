package com.leapsoftware.leap.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.dataObject.Letter;
import com.leapsoftware.leap.utils.Constants;
import com.leapsoftware.leap.utils.SimpleDrawingView;


public class HandwritingFragment extends android.support.v4.app.Fragment {
    protected TextView mTextView;
    public  Button              mEraseButton;
    public  SimpleDrawingView   mSimpleDrawingView;
    public Letter mLetterSelected;

    // Mandatory blank constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
    public HandwritingFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getArguments();
        mLetterSelected = extras.getParcelable(Constants.KEY_EXTRAS_LETTER_SELECTED);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_handwriting, container, false);
        mEraseButton = (Button) view.findViewById(R.id.fragment_handwriting_erase_button);

        mTextView = (TextView) view.findViewById(R.id.fragment_handwriting_textview);
        mTextView.setText(mLetterSelected.getUpperAndLower());

        mSimpleDrawingView = (SimpleDrawingView) view.findViewById(R.id.handwriting_fragment_simpledrawingview);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eraseDrawing();
            }
        });
    }

    private void eraseDrawing() {
        mSimpleDrawingView.onErase();
        mSimpleDrawingView.invalidate();
    }

}





