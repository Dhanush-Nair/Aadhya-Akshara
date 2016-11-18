package com.leapsoftware.leap.dataObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vincentrickey on 9/27/15.
 */
public class Example implements Parcelable{
    String mExample;

    public Example(String example) {
        this.mExample = example;
    }

    protected Example(Parcel in) {
        mExample = in.readString();
    }

    public static final Creator<Example> CREATOR = new Creator<Example>() {
        @Override
        public Example createFromParcel(Parcel in) {
            return new Example(in);
        }

        @Override
        public Example[] newArray(int size) {
            return new Example[size];
        }
    };

    public String getExample() {
        return mExample;
    }

    public void setExample(String example) {
        mExample = example;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mExample);
    }

    // We must override the toString() method so the IPA class knows how to display itself as a string.
    // Otherwise a type and hash would be returned; i.e. com.vincentrickey.IPA@234d345
    @Override
    public String toString() {
        return " " + this.getExample();
    }
}
