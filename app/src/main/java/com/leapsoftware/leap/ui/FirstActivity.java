package com.leapsoftware.leap.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.adapters.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity {
    Spinner spinner;
    ArrayAdapter<String> dataAdapter;
    List<String> categories;
    Intent in;
    SpinnerAdapter spinnerAdapter;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        spinner= (Spinner) findViewById(R.id.spinner);
        categories = new ArrayList<String>();
        categories.add("Select Age");
        categories.add("2-3");
        categories.add("4-5");
        spinner.setSelection(0);
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter = new SpinnerAdapter(getApplicationContext(),categories);
        spinner.setAdapter(dataAdapter);
        in=new Intent(FirstActivity.this,YoutubeVideos.class);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Position", String.valueOf(position));

                if(position==1)
                    startActivity(in);
                else if(position==2)
                {
                    in=new Intent(FirstActivity.this,MainActivity.class);
                    startActivity(in);
                }
                spinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
