package com.leapsoftware.leap.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import com.leapsoftware.leap.utils.Constants;

import java.util.Locale;

/**
 * Created by vincentrickey on 4/20/16.
 */
public class LeapApplication extends Application {
    public static final String TAG = "LeapApplication";
    private Locale locale = null;

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigChanged called");

        newConfig = new Configuration(newConfig);
        if (locale != null)
        {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d(TAG, "Application onCreate locale = " + Locale.getDefault().getISO3Language());
        SharedPreferences settings = getSharedPreferences(Constants.KEY_SHAREDPREFERENCES_LOCALE, Context.MODE_PRIVATE);

        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString(Constants.KEY_SHAREDPREFERENCES_ISO_6391_Language, "");
        Log.d(TAG, "onCreate lang = " + lang);
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang))
        {
            locale = new Locale(lang);
            Log.d(TAG, "onCreate updateConfig locale = " + locale.toString());
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            Log.d(TAG, "BaseContext configuration = " + getBaseContext().getResources().getConfiguration().locale.toString());
        }
    }
}
