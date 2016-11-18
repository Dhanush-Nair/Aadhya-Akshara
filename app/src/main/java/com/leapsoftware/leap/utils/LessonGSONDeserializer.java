package com.leapsoftware.leap.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.leapsoftware.leap.dataObject.LessonDO;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;


/**
 * Created by vincentrickey on 11/29/15.
 */
public class LessonGSONDeserializer implements JsonDeserializer<LessonDO> {
    @Override
    public LessonDO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject jsonVocabularyLesson = jsonElement.getAsJsonObject();

        // Deserialize LESSONNAME
        JsonElement jsonLessonNameEnglish = jsonVocabularyLesson.get(Constants.JSON_DATA_KEY_LESSON_LESSON_NAME_ENGLISH);
        final String lessonNameEnglish = jsonLessonNameEnglish.toString();
        JsonElement jsonLessonNameTranslated = jsonVocabularyLesson.get(Constants.JSON_DATA_KEY_LESSON_LESSON_NAME_TRANSLATED);
        final String lessonNameTranslated = jsonLessonNameTranslated.toString();

        // Deserialize VOCABMAP
        JsonElement vocabElement = jsonVocabularyLesson.get(Constants.JSON_DATA_KEY_LESSON_VOCAB_MAP);
        JsonObject vocabObject = vocabElement.getAsJsonObject();

        final LinkedHashMap<String, String> vocabHashMap = new LinkedHashMap<>();
        for (LinkedHashMap.Entry<String, JsonElement> vocabEntryItem : vocabObject.entrySet()) {
            String vocabWord = vocabEntryItem.getKey();
            String definition = vocabEntryItem.getValue().getAsString();
            vocabHashMap.put(vocabWord, definition);
        }


        // Deserialize DIALOGMAP
        JsonElement dialogElement = jsonVocabularyLesson.get(Constants.JSON_DATA_KEY_LESSON_DIALOG_MAP);
        JsonObject dialogObject = dialogElement.getAsJsonObject();

        final LinkedHashMap<String, String> dialogHashMap = new LinkedHashMap<>();
        for (LinkedHashMap.Entry<String, JsonElement> dialogEntryItem : dialogObject.entrySet()) {
            String dialogSentence = dialogEntryItem.getKey();
            String dialogTranslation = dialogEntryItem.getValue().getAsString();
            dialogHashMap.put(dialogSentence, dialogTranslation);
        }
        
        // Deserialize exercises
        JsonElement jsonExercises = jsonVocabularyLesson.get(Constants.JSON_DATA_KEY_LESSON_EXERCISES);
        JsonArray exercisesJsonArray = jsonExercises.getAsJsonArray();

        JsonElement jsonExerciseTranslated = jsonVocabularyLesson.get(Constants.JSON_DATA_KEY_LESSON_EXERCISES_TRANSLATED);
        JsonArray exercisesTranslatedArray = jsonExerciseTranslated.getAsJsonArray();
        Gson gson = new Gson();
        String[] exercisesStringArray = gson.fromJson(exercisesJsonArray, String[].class);
        String[] exercisesTranslatedStringArray = gson.fromJson(exercisesTranslatedArray, String[].class);


        final LessonDO lessonDO = new LessonDO();
        lessonDO.setLessonNameEnglish(lessonNameEnglish);
        lessonDO.setLessonNameTranslated(lessonNameTranslated);
        lessonDO.setVocabMap(vocabHashMap);
        lessonDO.setDialogMap(dialogHashMap);
        lessonDO.setExerciseNamesEnglish(exercisesStringArray);
        lessonDO.setExerciseNamesTranslated(exercisesTranslatedStringArray);
        return lessonDO;
    }
}
