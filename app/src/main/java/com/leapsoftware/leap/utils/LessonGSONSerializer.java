package com.leapsoftware.leap.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.leapsoftware.leap.dataObject.LessonDO;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;

/**
 * Created by vincentrickey on 11/27/15.
 */
public class LessonGSONSerializer implements JsonSerializer<LessonDO> {

    @Override
    public JsonElement serialize(LessonDO lessonDOObject, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonVocabularyLessonObject = new JsonObject();
        jsonVocabularyLessonObject.addProperty(Constants.JSON_DATA_KEY_LESSON_LESSON_NAME_ENGLISH, lessonDOObject.getLessonNameEnglish());
        jsonVocabularyLessonObject.addProperty(Constants.JSON_DATA_KEY_LESSON_LESSON_NAME_TRANSLATED, lessonDOObject.getLessonNameTranslated());

        Gson gson = new Gson();
        Type typeOfMap = new TypeToken<LinkedHashMap<String, String>>() {}.getType();

        String vocabMapJsonString = gson.toJson(lessonDOObject.getVocabMap(), typeOfMap);
        JsonElement vocabMapElement = new JsonParser().parse(vocabMapJsonString);
        jsonVocabularyLessonObject.add(Constants.JSON_DATA_KEY_LESSON_VOCAB_MAP, vocabMapElement);

        String dialogMapJsonString = gson.toJson(lessonDOObject.getDialogMap(), typeOfMap);
        JsonElement dialogMapElement = new JsonParser().parse(dialogMapJsonString);
        jsonVocabularyLessonObject.add(Constants.JSON_DATA_KEY_LESSON_DIALOG_MAP, dialogMapElement);

        return jsonVocabularyLessonObject;
    }
}
