package com.leapsoftware.leap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leapsoftware.leap.R;

import java.util.List;

/**
 * Created by Aswin on 11/16/2016.
 */

/*public class SpinnerAdapter {
}*/
public class SpinnerAdapter extends BaseAdapter {
    Context context;
    int flags[];
    List<String> spinnervals;
    LayoutInflater inflter;

    public SpinnerAdapter(Context applicationContext, List<String> spinnervals) {
        this.context = applicationContext;
        this.spinnervals = spinnervals;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        TextView names = (TextView) view.findViewById(R.id.spinner_textfield);
        names.setText(spinnervals.get(i));
        return view;
    }
}
