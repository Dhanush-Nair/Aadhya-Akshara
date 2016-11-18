package com.leapsoftware.leap.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.utils.Constants;

import java.util.Locale;

public class SelectLanguageActivity extends AppCompatActivity {

    protected static final int TIME_INTERVAL = 2000;
    protected long mBackPressed;
    protected Toast mSelectLangToContinueToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_language);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_select_language_toolbar_title);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        SelectLanguageFragment selectLanguageFragment = SelectLanguageFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.activity_select_language_framelayout, selectLanguageFragment).commit();

        Button finishButton = (Button) findViewById(R.id.activity_select_language_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences(Constants.KEY_SHAREDPREFERENCES_PROGRESS, Context.MODE_PRIVATE);
                if (sharedPref.getBoolean(Constants.KEY_SHAREDPREFERENCES_WIZARD_COMPLETE, false) == false) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(Constants.KEY_SHAREDPREFERENCES_SELECT_LANGUAGE_COMPLETE, true);
                    editor.commit();
                    startStartupWizard();
                } else {
                    startMainActivity();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Create a member variable for the toast so it can be canceled when the app is closed
        mSelectLangToContinueToast = new Toast(this);

        // if user hits back button twice within 2 seconds, close the app or return to MainActivity
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            mSelectLangToContinueToast.cancel();
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.KEY_SHAREDPREFERENCES_PROGRESS, Context.MODE_PRIVATE);
            // if a language has not been selected and the user presses the back button twice within 2 seconds, close the app
            if (sharedPreferences.getBoolean(Constants.KEY_SHAREDPREFERENCES_SELECT_LANGUAGE_COMPLETE, false) == false) {
                this.finishAffinity();
            }
            // if the user has already selected a language and hits the back button twice within 2 seconds
            // use default back button behavior (returns to MainActivity)
            else {
                super.onBackPressed();
            }
        } else { // onBackPressed, toast will appear prompting to select language to continue
            mSelectLangToContinueToast.makeText(this, R.string.select_language_activity_toast, Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    public static class SelectLanguageFragment extends Fragment implements AdapterView.OnItemSelectedListener {

        public SelectLanguageFragment() {
        }

        public static SelectLanguageFragment newInstance() {
            SelectLanguageFragment selectLanguageFragment = new SelectLanguageFragment();
            return selectLanguageFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_select_language, container, false);

            Spinner spinner = (Spinner) rootView.findViewById(R.id.fragment_select_language_spinner);
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.select_language_array, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setOnItemSelectedListener(this);
            spinner.setAdapter(spinnerAdapter);

            return rootView;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            SharedPreferences settings = getActivity().getSharedPreferences(Constants.KEY_SHAREDPREFERENCES_LOCALE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();

            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

            switch (position) {
                case 0:
                    Locale.setDefault(Locale.KOREAN);
                    editor.putString(Constants.KEY_SHAREDPREFERENCES_ISO_6391_Language, Locale.KOREAN.getLanguage());
                    editor.commit();
                    Log.d("SelectLanguageFragment", "shared pref ISO3 language case ko = " + settings.getString(Constants.KEY_SHAREDPREFERENCES_ISO_6391_Language, ""));
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Snackbar.make(parent, "Please select a language", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void startStartupWizard() {
        Intent wizardIntent = new Intent(this, StartupWizardActivity.class);
        startActivity(wizardIntent);
    }

    public void startMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
    }
}
