package com.leapsoftware.leap.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vincentrickey on 3/27/16.
 */
public enum Attempt implements Parcelable {
    FIRSTTRY, RETRY, TYPO, STUDYING;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Attempt> CREATOR = new Creator<Attempt>() {
        @Override
        public Attempt createFromParcel(Parcel in) {
            return Attempt.values()[in.readInt()];
        }

        @Override
        public Attempt[] newArray(int size) {
            return new Attempt[size];
        }
    };
}
