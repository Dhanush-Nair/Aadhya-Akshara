package com.leapsoftware.leap.utils;

/**
 * Created by vincentrickey on 10/24/15.
 */
public class Constants {
    public static final int REQ_CODE_SPEECH_INPUT = 100;
    public static final int REQ_CODE_CHECK_TTS_INSTALLED = 101;
    public static final int REQ_CODE_IAB = 10001;
    public static final String PRONUNCIATION_CORRECT_PHRASE = "pronunciationCorrectPhrase";

    // Data Asset Keys
    public static final String DATA_ASSET_FILE_KOREAN = "Data-ko.json";
    public static final String DATA_ASSET_FILE_THAI = "Data-th.json";
    public static final String DATA_ASSET_FILE_JAPANESE = "Data-ja.json";
    public static final String DATA_ASSET_FILE_ARABIC = "Data-ar.json";
    public static final String DATA_ASSET_FILE_CHINESE = "Data-zh.json";
    public static final String DATA_ASSET_FILE_VIETNAMESE = "Data-vi.json";


    // Extras
    public static final String KEY_EXTRAS_LETTERSARRAYLIST = "lettersArrayList";
    public static final String KEY_EXTRAS_LETTER_SELECTED = "letterSelectedExtra";
    public static final String KEY_EXTRAS_UNIT = "unitExtra";
    public static final String KEY_EXTRAS_LESSON_SELECTED = "lessonSelected";

    // Data Array
    public static final String JSON_DATA_ARRAY_KEY = "Data";
    // Letters
    public static final String JSON_DATA_KEY_LETTERS = "Letters";
    public static final String JSON_DATA_KEY_LETTER_LOWERCASE = "lowercase";
    public static final String JSON_DATA_KEY_LETTER_UPPERCASE = "uppercase";
    public static final String JSON_DATA_KEY_LETTER_UPPERANDLOWER = "upperAndLower";
    public static final String JSON_DATA_KEY_LETTER_IPAS = "ipas";
    public static final String JSON_DATA_KEY_LETTER_IPA = "ipa";
    public static final String JSON_DATA_KEY_LETTER_EXAMPLES = "examples";
    public static final String JSON_DATA_KEY_LETTER_TRANSLITERATIONS = "transliterations";
    public static final String JSON_DATA_KEY_LETTER_AUDIO_FILE_NAME = "audio_file_name";
    // Units
    public static final String JSON_DATA_KEY_UNITS_ARRAY = "units";
    public static final String JSON_DATA_KEY_UNIT_NAME = "unitname";
    public static final String JSON_DATA_KEY_UNIT_DESCRIPTION = "unitdescription";
    public static final String JSON_DATA_KEY_LESSONS_ARRAY = "lessons";
    public static final String JSON_DATA_KEY_LESSON_LESSON_NAME_ENGLISH = "lesson_name_english";
    public static final String JSON_DATA_KEY_LESSON_LESSON_NAME_TRANSLATED = "lesson_name_translated";
    public static final String JSON_DATA_KEY_LESSON_VOCAB_MAP = "vocab_map";
    public static final String JSON_DATA_KEY_LESSON_DIALOG_MAP = "dialog_map";
    public static final String JSON_DATA_KEY_LESSON_EXERCISES = "exercises";
    public static final String JSON_DATA_KEY_LESSON_EXERCISES_TRANSLATED = "exercises_translated";

    public static final String JSON_DATA_KEY_ALPHABET_UNIT_NAME = "alphabetUnitName";
    public static final String JSON_DATA_KEY_ALPHABET_UNIT_DESCRIPTION = "alphabetUnitDescription";

    // Shared Preferences
    public static final String KEY_SHAREDPREFERENCES_PROGRESS = "sharedPreferencesProgress";
    public static final String KEY_SHAREDPREFERENCES_WIZARD_COMPLETE = "wizardComplete";
    public static final String KEY_SHAREDPREFERENCES_SELECT_LANGUAGE_COMPLETE = "selectLanguageComplete";
    public static final String KEY_SHAREDPREFERENCES_LOCALE = "sharedPreferencesLocale";
    public static final String KEY_SHAREDPREFERENCES_ISO_6391_Language = "sharedPreferencesISO6391Language";
    public static final String KEY_SHAREDPREFERENCES_PURCHASES = "sharedPreferencesPurchases";
    public static final String KEY_SHAREDPREFERENCES_PREMIUM = "sharedPreferencesPremium";

    // Locale
    public static final String SELECTED_LOCALE = "selectedLocale";
}
