package com.leapsoftware.leap.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.adapters.MainActivityAdapter;
import com.leapsoftware.leap.dataObject.Letter;
import com.leapsoftware.leap.units.Unit;
import com.leapsoftware.leap.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public ArrayList<Letter> mLetterArrayList;
    public ArrayList<Unit> mUnitArrayList;
    public ArrayList<String> mMasterUnitNames;
    public ArrayList<String> mMasterUnitDescriptions;
    public JSONObject mJSONObject;
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If language hasn't been selected, start SelectLanguageActivity
        // SelectLanguageActivity will start StartupWizardActivity if it hasn't been completed
        SharedPreferences preferences = getSharedPreferences(Constants.KEY_SHAREDPREFERENCES_PROGRESS, Context.MODE_PRIVATE);
        /*if (preferences.getBoolean(Constants.KEY_SHAREDPREFERENCES_WIZARD_COMPLETE, false) == false) {
            startSelectLanguageActivity();
        }*/

        // Set locale from savedInstanceState or default locale set by user in SelectLanguageActivity (stored in sharedpreferences in LeapApplication class)
        // Vocabulary words are stored in a localized json file in the assets folder
        // setCustomLocale updates Configuration for localized user-facing strings, such as alert dialog messages and buttons, in resources directory
        setCustomLocale(savedInstanceState);

        // Set a Toolbar to replace the ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        mContext = this;

        // reads JSON data from the assets folder, returns jsonString of localized content depending on Locale of user
        String dataJsonString = loadJSONFromAsset();

        try {
            mJSONObject = initializeJsonObject(dataJsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // initialize ArrayList of Letters. A Letter
        mLetterArrayList = initializeLetterArrayList(mJSONObject);

        // initialize ArrayList of Units. A Unit is made up of Lessons, Exercises, and ExerciseItems
        mUnitArrayList = initializeUnitArrayList(mJSONObject);

        // Technical Debt Below...
        // Since Data.json only includes an array of Letters for the AlphabetActivity
        // (not array of Units that includes unit, lessons, exercises, and exercise items which is only used in VocabularyLessonActivity),
        // an arraylist of the "alphabet unit name" + all of the unit names is pulled from the alphabet json object and all of the units... this is MasterUnitNames
        // A main activity item is created based on the size of the MasterUnitNames arraylist.
        mMasterUnitNames = initializeActivityNames(mJSONObject, mUnitArrayList);
        mMasterUnitDescriptions = initializeActivityDescriptions(mJSONObject, mUnitArrayList);

        // Recyclerview
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_activity_recyclerView);
        recyclerView.setHasFixedSize(true);

        // LayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        // Set adapter to recyclerview
        MainActivityAdapter mainActivityAdapter = new MainActivityAdapter(mContext, mLetterArrayList, mUnitArrayList, mMasterUnitNames, mMasterUnitDescriptions);
        recyclerView.setAdapter(mainActivityAdapter);
        //recyclerView.findViewHolderForAdapterPosition(0).itemView.performClick();

        Intent alphabetIntent = new Intent(mContext, AlphabetActivity.class);
        alphabetIntent.putParcelableArrayListExtra(Constants.KEY_EXTRAS_LETTERSARRAYLIST, mLetterArrayList);
        mContext.startActivity(alphabetIntent);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Locale defaultLocale = Locale.getDefault();
        savedInstanceState.putSerializable(Constants.SELECTED_LOCALE, defaultLocale);
        super.onSaveInstanceState(savedInstanceState);
    }

    public String loadJSONFromAsset() {
        String dataJsonString = null;
        try {
            // Korean locale hard coded for initial soft release
            String dataAssetKey = Constants.DATA_ASSET_FILE_KOREAN;

            // For future releases that require multi-lingual support
            // Check user's Locale to load correct json asset file from assets folder

            /*

            String dataAssetKey;
            if (Locale.getDefault().getLanguage().equals(Locale.KOREAN.getLanguage())) {
                // load Korean assets
                dataAssetKey = Constants.DATA_ASSET_FILE_KOREAN;
            } else if (Locale.getDefault().getLanguage().equals(Locale.JAPAN.getLanguage())) {
                // load Japanese assets
                dataAssetKey = Constants.DATA_ASSET_FILE_JAPANESE;
            } ...

            */

            InputStream is = this.getAssets().open(dataAssetKey);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            dataJsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return dataJsonString;
    }

    // creates JSONObject from JsonString
    public JSONObject initializeJsonObject(String jsonString) throws Exception {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // initializes letters from loadJSONFromAsset() method
    public ArrayList<Letter> initializeLetterArrayList(JSONObject jsonObject) {
        try {
            JSONArray lettersJSONArray = jsonObject.getJSONArray(Constants.JSON_DATA_ARRAY_KEY)
                    .getJSONObject(0)
                    .getJSONArray(Constants.JSON_DATA_KEY_LETTERS);

            ArrayList<Letter> lettersArrayList = new ArrayList<Letter>();

            for (int i = 0; i < lettersJSONArray.length(); i++) {
                JSONObject jo_letters = lettersJSONArray.getJSONObject(i);
                Letter letter = new Letter(jo_letters);
                lettersArrayList.add(letter);
            }
            return lettersArrayList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Unit> initializeUnitArrayList(JSONObject jsonObject) {
        try {
            JSONArray unitsJSONArray = jsonObject.getJSONArray(Constants.JSON_DATA_ARRAY_KEY)
                    .getJSONObject(1)
                    .getJSONArray(Constants.JSON_DATA_KEY_UNITS_ARRAY);
            Log.d(TAG, "unitsJSONArray = " + unitsJSONArray.toString());
            ArrayList<Unit> unitArrayList = new ArrayList<Unit>();

            for (int i = 0; i < unitsJSONArray.length(); i++) {
                JSONObject unitJsonObject = unitsJSONArray.getJSONObject(i);
                Log.d(TAG, "units array for loop position " + i);
                Unit unit = null;
                try {
                    unit = new Unit(unitJsonObject);
                    Log.d(TAG, "unit name = " + unit.getUnitName());
                } catch (JSONException e) {
                    e.printStackTrace();
                    e.getMessage();
                }
                unitArrayList.add(unit);
            }
            return unitArrayList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> initializeActivityNames(JSONObject jsonObject, ArrayList<Unit> unitArrayList) {
        ArrayList<String> activityNames = new ArrayList<String>();
        try {
            String alphabetUnitName = jsonObject.getJSONArray(Constants.JSON_DATA_ARRAY_KEY)
                    .getJSONObject(2)
                    .get(Constants.JSON_DATA_KEY_ALPHABET_UNIT_NAME)
                    .toString();
            Log.d(TAG, "alphabet Unit name = " + alphabetUnitName);
            activityNames.add(alphabetUnitName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (Unit unit : unitArrayList) {
            String unitName = unit.getUnitName();
            activityNames.add(unitName);
        }
        return activityNames;
    }

    public ArrayList<String> initializeActivityDescriptions(JSONObject jsonObject, ArrayList<Unit> unitArrayList) {
        ArrayList<String> activityDescriptions = new ArrayList<String>();
        String alphabetUnitDescription = null;
        try {
            alphabetUnitDescription = jsonObject.getJSONArray(Constants.JSON_DATA_ARRAY_KEY)
                    .getJSONObject(2)
                    .get(Constants.JSON_DATA_KEY_ALPHABET_UNIT_DESCRIPTION)
                    .toString();
            activityDescriptions.add(alphabetUnitDescription);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (Unit unit : unitArrayList) {
            String unitDescription = unit.getUnitDescription();
            activityDescriptions.add(unitDescription);
        }
        return activityDescriptions;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_startup_wizard:
                startStartupWizardActivity();
                return true;
            case R.id.action_select_language:
                startSelectLanguageActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startStartupWizardActivity() {
        Intent wizardIntent = new Intent(this, StartupWizardActivity.class);
        startActivity(wizardIntent);
    }

    public void startSelectLanguageActivity() {
        Intent languageIntent = new Intent(this, SelectLanguageActivity.class);
        startActivity(languageIntent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        newConfig.locale = Locale.getDefault();
        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
    }

    public void setCustomLocale(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Locale savedLocale = (Locale) savedInstanceState.getSerializable(Constants.SELECTED_LOCALE);
            if (savedLocale != null) {
                // Update the configuration. The configuration controls which localized resource folder will be loaded; i.e. values-ko for korean
                Configuration savedInstanceConfig = new Configuration();
                savedInstanceConfig.locale = savedLocale;
                Locale.setDefault(savedLocale);
                onConfigurationChanged(savedInstanceConfig);
            }
        } else {
            Configuration sharedPrefConfig = new Configuration();
            sharedPrefConfig.locale = Locale.getDefault();
            onConfigurationChanged(sharedPrefConfig);
        }
    }
}