package com.leapsoftware.leap.ui;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leapsoftware.leap.R;
import com.leapsoftware.leap.utils.Constants;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class StartupWizardActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private WizardFragmentPagerAdapter mWizardFragmentPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TextToSpeech mTextToSpeech;
    private Button mFinishButton;
    private Context mContext;

    private DeviceArtFragment deviceArtFragment1;
    private DeviceArtFragment deviceArtFragment2;
    private DeviceArtFragment deviceArtFragment3;
    private DeviceArtFragment deviceArtFragment4;
    private DeviceArtFragment deviceArtFragment5;
    private DeviceArtFragment deviceArtFragment6;

    int page = 0; // keep track of fragment position
    private ImageView indicator0, indicator1, indicator2, indicator3, indicator4, indicator5;
    private ImageView[] indicators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_wizard);
        mContext = this;

        // If the wizard has not been complete, update the configuration to use defaultLocale selected in SelectLanguageActivity
        // Localized String Resources may not load when the StartupWizardActivity launched after install
        SharedPreferences sharedPref = getSharedPreferences(Constants.KEY_SHAREDPREFERENCES_PROGRESS, Context.MODE_PRIVATE);
        if (sharedPref.getBoolean(Constants.KEY_SHAREDPREFERENCES_WIZARD_COMPLETE, false) == false) {
            Log.d("StartupWizardActivity", "Locale = " + Locale.getDefault());
            Resources resources = getResources();
            Configuration configuration = resources.getConfiguration();
            Locale defaultLocale = Locale.getDefault();
            configuration.setLocale(defaultLocale);
            resources.updateConfiguration(configuration, null);
        }
        
        // Sets activity to fullscreen. "Stable" means it will overlap satus bar and nav bar, but not transient elements such as input method
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statup_wizard_status_bar));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_startup_wizard_toolbar_title);
        setSupportActionBar(toolbar);

        mWizardFragmentPagerAdapter = new WizardFragmentPagerAdapter(getSupportFragmentManager());

        mFinishButton = (Button) findViewById(R.id.activity_startup_wizard_finish_button);

        // Indicator refers to the circle_status_icon that keeps track of the user's page number
        indicator0 = (ImageView) findViewById(R.id.activity_startup_wizard_indicator_0);
        indicator1 = (ImageView) findViewById(R.id.activity_startup_wizard_indicator_1);
        indicator2 = (ImageView) findViewById(R.id.activity_startup_wizard_indicator_2);
        indicator3 = (ImageView) findViewById(R.id.activity_startup_wizard_indicator_3);
        indicator4 = (ImageView) findViewById(R.id.activity_startup_wizard_indicator_4);
        indicator5 = (ImageView) findViewById(R.id.activity_startup_wizard_indicator_5);

        indicators = new ImageView[]{indicator0, indicator1, indicator2, indicator3, indicator4, indicator5};

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.activity_startup_wizard_viewpager);
        mViewPager.setAdapter(mWizardFragmentPagerAdapter);
        mViewPager.setCurrentItem(page);
        // will hold previously swiped pages in cache
        mViewPager.setOffscreenPageLimit(5);
        updateIndicators(page);

        final int color1 = ContextCompat.getColor(this, R.color.colorPrimary);
        final int color2 = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        final int color3 = ContextCompat.getColor(this, R.color.colorPrimaryLight);
        final int[] colors = new int[]{color1, color2, color3, color1, color2, color3};

        final ArgbEvaluator argbEvaluator = new ArgbEvaluator();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // ArgbEvaluator calculates in-between color values for swipe animation
                int colorUpdate = (Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position == 5 ? position : position + 1]);
                mViewPager.setBackgroundColor(colorUpdate);
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                updateIndicators(page);

                switch (position) {
                    case 0:
                        mViewPager.setBackgroundColor(color1);
                        break;
                    case 1:
                        mViewPager.setBackgroundColor(color2);
                        break;
                    case 2:
                        mViewPager.setBackgroundColor(color3);
                        break;
                    case 3:
                        mViewPager.setBackgroundColor(color1);
                        break;
                    case 4:
                        mViewPager.setBackgroundColor(color2);
                        break;
                    case 5:
                        mViewPager.setBackgroundColor(color3);
                        break;
                }

                mFinishButton.setVisibility(position == 5 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                SharedPreferences sharedPref = getSharedPreferences(Constants.KEY_SHAREDPREFERENCES_PROGRESS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(Constants.KEY_SHAREDPREFERENCES_WIZARD_COMPLETE, true);
                editor.commit();

                Intent mainIntent = new Intent(mContext, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
            }
        });

        // Checks to see if Locale.US is support by device
        // If not, user will be prompted to install TTS engine
        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTextToSpeech.isLanguageAvailable(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Intent installIntent = new Intent();
                        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        startActivity(installIntent);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mTextToSpeech != null) {
            mTextToSpeech.shutdown();
            Log.d("StartupWizardActivity", "tts shutdown");
        }
        super.onDestroy();
    }

    public void updateIndicators(int pagePosition) {
        for (int i = 0; i < indicators.length; i++) {
            // "?" is the conditional operator
            // if it is the indicator at a given position... value if true : value if false
            indicators[i].setBackgroundResource(i == pagePosition ? R.drawable.indicator_selected : R.drawable.indicator_unselected);
        }
    }

    public void loadBitmap(int resId, ImageView imageView) {
        //imageView.setImageResource(R.drawable.device_art_lesson);
        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(resId);
    }

    public static class DeviceArtFragment extends Fragment {

        public static final String ARG_DEVICE_ART_DESCRIPTION = "argDeviceArtDescription";
        public static final String ARG_DEVICE_ART_IMAGE_ID = "argDeviceArtImageID";

        public String mDeviceArtDescription;
        public int mImageID;
        public ImageView mDeviceArtImageView;

        public DeviceArtFragment() {
        }

        public static DeviceArtFragment newInstance(String deviceArtDescription, int imageID) {
            DeviceArtFragment deviceArtFragment = new DeviceArtFragment();
            Bundle args = new Bundle();
            args.putString(ARG_DEVICE_ART_DESCRIPTION, deviceArtDescription);
            args.putInt(ARG_DEVICE_ART_IMAGE_ID, imageID);
            deviceArtFragment.setArguments(args);
            return deviceArtFragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // getArguments from new instance
            if (getArguments() != null) {
                mDeviceArtDescription = getArguments().getString(ARG_DEVICE_ART_DESCRIPTION);
                mImageID = getArguments().getInt(ARG_DEVICE_ART_IMAGE_ID);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_device_art, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.fragment_device_art_textview);
            textView.setText(mDeviceArtDescription);
            mDeviceArtImageView = (ImageView) rootView.findViewById(R.id.fragment_device_art_image);
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            if (StartupWizardActivity.class.isInstance(getActivity())) {
                // Call out to ImageDetailActivity to load the bitmap in a background thread
                ((StartupWizardActivity) getActivity()).loadBitmap(mImageID, mDeviceArtImageView);
                Log.d("DeviceArtFragment", "loadBitmap called. ImageID = " + mImageID);
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class WizardFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public WizardFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    // Alphabet Tutorial
                    if (deviceArtFragment1 == null) {
                        deviceArtFragment1 = DeviceArtFragment.newInstance(getString(R.string.startup_wizard_alphabet_description), R.drawable.device_art_alphabet);
                    }
                    return deviceArtFragment1;
                case 1:
                    // VocabularyLesson Tutorial
                    if (deviceArtFragment2 == null) {
                        deviceArtFragment2 = DeviceArtFragment.newInstance(getString(R.string.startup_wizard_vocabulary_lesson_description), R.drawable.device_art_reading);
                    }
                    return deviceArtFragment2;
                case 2:
                    // VocabWords Tutorial
                    if (deviceArtFragment3 == null) {
                        deviceArtFragment3 = DeviceArtFragment.newInstance(getString(R.string.startup_wizard_vocab_words_description), R.drawable.device_art_vocabulary);
                    }
                    return deviceArtFragment3;
                case 3:
                    // Reading Tutorial
                    if (deviceArtFragment4 == null) {
                        deviceArtFragment4 = DeviceArtFragment.newInstance(getString(R.string.startup_wizard_reading_description), R.drawable.device_art_reading);
                    }
                    return deviceArtFragment4;
                case 4:
                    // Pronunciation Tutorial
                    if (deviceArtFragment5 == null) {
                        deviceArtFragment5 = DeviceArtFragment.newInstance(getString(R.string.startup_wizard_pronunciation_description), R.drawable.device_art_pronunciation);
                    }
                    return deviceArtFragment5;
                case 5:
                    // Quiz Tutorial
                    if (deviceArtFragment6 == null) {
                        deviceArtFragment6 = DeviceArtFragment.newInstance(getString(R.string.startup_wizard_quiz_description), R.drawable.device_art_quiz);
                    }
                    return deviceArtFragment6;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 6;
        }
    }

    public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            return decodeSampledBitmapFromResource(getResources(), data, 500, 500);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

        public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                             int reqWidth, int reqHeight) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        }

        public int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }
    }
}
