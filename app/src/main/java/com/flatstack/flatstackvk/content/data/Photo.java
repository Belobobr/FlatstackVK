package com.flatstack.flatstackvk.content.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Photo implements Parcelable {

    public Photo() {
    }

    @SerializedName("id")
    private int mId;

    @SerializedName("photo_604")
    private String mPhoto604;

    @SerializedName("photo_130")
    private String mPhoto130;

    @SerializedName("photo_75")
    private String mPhoto75;

    @SerializedName("album_id")
    private int mAlbumId;

    @SerializedName("text")
    private String mText;

//    @SerializedName("height")
//    private int mHeight;
//
//    @SerializedName("width")
//    private int mWidth;
//
//    @SerializedName("owner_id")
//    private int mOwnerId;
//
//    @SerializedName("user_id")
//    private int mUserId;
//
//    @SerializedName("access_key")
//    private String mAccessKey;


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getPhoto604() {
        return mPhoto604;
    }

    public void setPhoto604(String photo604) {
        mPhoto604 = photo604;
    }

    public String getPhoto130() {
        return mPhoto130;
    }

    public void setPhoto130(String photo130) {
        mPhoto130 = photo130;
    }

    public String getPhoto75() {
        return mPhoto75;
    }

    public void setPhoto75(String photo75) {
        mPhoto75 = photo75;
    }

    public int getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(int albumId) {
        mAlbumId = albumId;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mPhoto604);
        parcel.writeString(mPhoto130);
        parcel.writeString(mPhoto75);
        parcel.writeInt(mAlbumId);
        parcel.writeString(mText);
    }

    public Photo(final Parcel in) {
        mId = in.readInt();
        mPhoto604 = in.readString();
        mPhoto130 = in.readString();
        mPhoto75 = in.readString();
        mAlbumId = in.readInt();
        mText = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {

        @Override
        public Photo createFromParcel(final Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(final int size) {
            return new Photo[size];
        }

    };
}
