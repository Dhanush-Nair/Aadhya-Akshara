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

public class Letter implements Parcelable {

    private String mLowercase;
    private String mUppercase;
    private String mUpperAndLower;
    public Boolean mIsFrontShowing;
    private ArrayList<IPA> mIPAArrayList;
    private ArrayList<String> mTransliterationsArrayList;
    private String mAudioFileName;
    private static final String TAG = "Letter";


    public Letter(JSONObject object) throws JSONException {
        try {
            this.mLowercase = object.getString(Constants.JSON_DATA_KEY_LETTER_LOWERCASE);
            this.mUppercase = object.getString(Constants.JSON_DATA_KEY_LETTER_UPPERCASE);
            this.mUpperAndLower = object.getString(Constants.JSON_DATA_KEY_LETTER_UPPERANDLOWER);
            this.mIsFrontShowing = true;
            this.mIPAArrayList = new ArrayList<IPA>();

            JSONArray ipaJSONArray = object.getJSONArray(Constants.JSON_DATA_KEY_LETTER_IPAS);
            for (int i = 0; i < ipaJSONArray.length(); i++) {
                JSONObject jo_ipa = ipaJSONArray.getJSONObject(i);
                IPA ipa = new IPA(jo_ipa);
                this.mIPAArrayList.add(ipa);
            }

            JSONArray transliterationsJSONArray = object.getJSONArray(Constants.JSON_DATA_KEY_LETTER_TRANSLITERATIONS);
            for (int i = 0; i < transliterationsJSONArray.length(); i++) {
                String transliteration = transliterationsJSONArray.getString(i);
                mTransliterationsArrayList = new ArrayList<>();
                this.mTransliterationsArrayList.add(transliteration);
            }

            this.mAudioFileName = object.getString(Constants.JSON_DATA_KEY_LETTER_AUDIO_FILE_NAME);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) { // This method writes (packages up) the member variables of the Letter class to the Parcel that we can send to another activity. Member variables are written to a parcel named dest, which is set as the parameter.
        dest.writeString(mLowercase);
        dest.writeString(mUppercase);
        dest.writeString(mUpperAndLower);
        dest.writeTypedList(mIPAArrayList);
        dest.writeStringList(mTransliterationsArrayList);
        dest.writeByte((byte) (mIsFrontShowing ? 1 : 0));
        dest.writeString(mAudioFileName);
    }

    protected Letter(Parcel in) { // This constructor object will unwrap the package to be read.
        mLowercase = in.readString();
        mUppercase = in.readString();
        mUpperAndLower = in.readString();
        mIPAArrayList = in.createTypedArrayList(IPA.CREATOR);
        mTransliterationsArrayList = in.createStringArrayList();
        mIsFrontShowing = in.readByte() != 0;
        mAudioFileName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0; // We are not going to use this method. Left auto-generated code unchanged.
    }

    public static final Creator<Letter> CREATOR = new Creator<Letter>() {
        @Override
        public Letter createFromParcel(Parcel in) {
            return new Letter(in); // returns a new Letter object with the parcel source
        }

        @Override
        public Letter[] newArray(int size) {
            return new Letter[size]; // creates new Letter of specified size
        }
    };

    public String displayName() {
        return this.getUppercase() + this.getLowercase();
    }

    public String getLowercase() {
        return mLowercase;
    }

    public void setLowercase(String lowercase) {
        mLowercase = lowercase;
    }

    public String getUppercase() {
        return mUppercase;
    }

    public void setUppercase(String uppercase) {
        mUppercase = uppercase;
    }

    public String getUpperAndLower() {return mUpperAndLower;}

    public void setUpperAndLower(String upperAndLower) {mUpperAndLower = upperAndLower;}

    public ArrayList<IPA> getIPAArrayList() {
        return mIPAArrayList;
    }

    public void setIPAArrayList(ArrayList<IPA> IPAArrayList) {
        mIPAArrayList = IPAArrayList;
    }

    public Boolean getIsFrontShowing() {return mIsFrontShowing;}

    public void setIsFrontShowing(Boolean isFrontShowing) {mIsFrontShowing = isFrontShowing;}

    public ArrayList<String> getTransliterationsArrayList() {return  mTransliterationsArrayList;}

    public void setTransliterationsArrayList(ArrayList<String> transliterationsArrayList) {
        this.mTransliterationsArrayList = transliterationsArrayList;
    }

    public String getAudioFileName() {
        return mAudioFileName;
    }
}








