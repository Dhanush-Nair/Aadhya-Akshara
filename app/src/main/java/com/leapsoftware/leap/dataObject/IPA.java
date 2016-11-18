package com.leapsoftware.leap.dataObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.leapsoftware.leap.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vincentrickey on 9/27/15.
 */
public class IPA implements Parcelable {
    private String mIPA;
    private ArrayList<Example> mExamples;

    public IPA(JSONObject object) throws JSONException {
        try {
            this.mIPA = object.getString(Constants.JSON_DATA_KEY_LETTER_IPA);
            this.mExamples = new ArrayList<Example>();

            JSONArray exampleJSONArray = object.getJSONArray(Constants.JSON_DATA_KEY_LETTER_EXAMPLES);
            for (int i = 0; i < exampleJSONArray.length(); i++) {
                String exstring = exampleJSONArray.getString(i);
                Example example = new Example(exstring);
                this.mExamples.add(example);
            }
        } catch(JSONException ex) {
            ex.printStackTrace();
        }
    }

    protected IPA(Parcel in) {
        mIPA = in.readString();
        mExamples = in.createTypedArrayList(Example.CREATOR);

    }

    public static final Creator<IPA> CREATOR = new Creator<IPA>() {
        @Override
        public IPA createFromParcel(Parcel in) {
            return new IPA(in);
        }

        @Override
        public IPA[] newArray(int size) {
            return new IPA[size];
        }
    };

    public String getIPA() {
        return mIPA;
    }

    public void setIPA(String ipa) {
        mIPA = ipa;
    }

    public ArrayList<Example> getExamples() {
        return mExamples;
    }

    public void setExamples(ArrayList<Example> examples) {
        mExamples = examples;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mIPA);
        //dest.writeList(mExamples);
        dest.writeTypedList(mExamples);
    }

    // We must override the toString() method so the IPA class knows how to display itself as a string.
    // Otherwise a type and hash would be returned; i.e. com.vincentrickey.IPA@234d345
    @Override
    public String toString() {
        return " " + this.getIPA();
    }
}










