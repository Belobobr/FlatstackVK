package com.flatstack.flatstackvk.content.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Genre implements Parcelable {

    public Genre() {
    }

    /* package */ Genre(final Parcel src) {
        mName = src.readString();
    }

    @SerializedName("name")
    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {

        @Override
        public Genre createFromParcel(final Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(final int size) {
            return new Genre[size];
        }

    };
}
