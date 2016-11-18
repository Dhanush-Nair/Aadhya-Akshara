package com.leapsoftware.leap.units;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leapsoftware.leap.dataObject.LessonDO;
import com.leapsoftware.leap.lessons.Lesson;
import com.leapsoftware.leap.utils.Completable;
import com.leapsoftware.leap.utils.Constants;
import com.leapsoftware.leap.utils.LessonGSONDeserializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vincentrickey on 3/14/16.
 */
public class Unit implements Completable, Parcelable {
    public ArrayList<Lesson> mLessonArrayList;
    public String mUnitName;
    public String mUnitDescription;
    public boolean mIsUnitPremium;

    public Unit(JSONObject unitJSONObject) throws JSONException {
        try {
            this.mLessonArrayList = initializeLessonsArrayList(unitJSONObject);
            this.mUnitName = unitJSONObject.getString(Constants.JSON_DATA_KEY_UNIT_NAME);
            this.mUnitDescription = unitJSONObject.getString(Constants.JSON_DATA_KEY_UNIT_DESCRIPTION);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected Unit(Parcel in) {
        mLessonArrayList = new ArrayList<Lesson>();
        in.readTypedList(mLessonArrayList, Lesson.CREATOR);
        this.mUnitName = in.readString();
        this.mUnitDescription = in.readString();
        this.mIsUnitPremium = in.readByte() != 0;

    }

    public static final Creator<Unit> CREATOR = new Creator<Unit>() {
        @Override
        public Unit createFromParcel(Parcel in) {
            return new Unit(in);
        }

        @Override
        public Unit[] newArray(int size) {
            return new Unit[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mLessonArrayList);
        dest.writeString(mUnitName);
        dest.writeString(mUnitDescription);
        dest.writeByte((byte) (mIsUnitPremium ? 1 : 0));
    }

    public ArrayList<Lesson> initializeLessonsArrayList(JSONObject unitJsonObject) throws JSONException {
        try {
            ArrayList<Lesson> lessonArrayList = new ArrayList<Lesson>();
            JSONArray lessonsJsonArray = unitJsonObject.getJSONArray(Constants.JSON_DATA_KEY_LESSONS_ARRAY);
            for (int i = 0; i < lessonsJsonArray.length(); i++) {
                // Create lesonGSON from Json
                JSONObject lessonJSONObject = lessonsJsonArray.getJSONObject(i);
                LessonDO lessonDO = createLessonGSON(lessonJSONObject);

                // Pass lessonGSON to create new Lesson
                Lesson lesson = new Lesson(lessonDO);

                lessonArrayList.add(lesson);
            }
            return lessonArrayList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LessonDO createLessonGSON(JSONObject lessonJSONObject) {
        // Configure Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LessonDO.class, new LessonGSONDeserializer());
        Gson gson = gsonBuilder.create();

        LessonDO lessonDO = gson.fromJson(lessonJSONObject.toString(), LessonDO.class);

        return lessonDO;
    }

    @Override
    public boolean isComplete(Context context) {
        // loop through every lesson to determine if it has been completed
        for (Lesson lesson : mLessonArrayList) {
            if (lesson.isComplete(context) == false) {
                // If a lesson is incomplete, then lesson is incomplete
                return false;
            }
        }
        // If all lessons are complete, then unit is complete
        return true;
    }

    public Lesson getCurrentLesson(Context context) {
        for (Lesson lesson : mLessonArrayList) {
            if (lesson.isComplete(context) == false) {
                return lesson;
            }
        }
        return null;
    }

    public ArrayList<Lesson> getLessonArrayList() {
        return mLessonArrayList;
    }

    public String getUnitName() {
        return mUnitName;
    }

    public String getUnitDescription() {
        return mUnitDescription;
    }

    public boolean isUnitPremium() {
        return mIsUnitPremium;
    }

    public void setIsUnitPremium(boolean isUnitPremium) {
        mIsUnitPremium = isUnitPremium;
    }
}
