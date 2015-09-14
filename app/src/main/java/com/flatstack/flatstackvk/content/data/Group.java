package com.flatstack.flatstackvk.content.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.flatstack.flatstackvk.content.dao.Identify;
import com.google.gson.annotations.SerializedName;

public class Group implements Identify, Parcelable{

    public Group() {
    }

    @SerializedName("id")
    int mId;

    @SerializedName("photo_200")
    String mPhoto200;

    @SerializedName("photo_100")
    String mPhoto100;

    @SerializedName("photo_50")
    String mPhoto50;

    @SerializedName("is_closed")
    String mIsClosed;

    @SerializedName("name")
    String mName;

    @SerializedName("screen_name")
    String mScreenName;

    @SerializedName("type")
    String mType;

    @Override
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getPhoto200() {
        return mPhoto200;
    }

    public void setPhoto200(String photo200) {
        mPhoto200 = photo200;
    }

    public String getPhoto100() {
        return mPhoto100;
    }

    public void setPhoto100(String photo100) {
        mPhoto100 = photo100;
    }

    public String getPhoto50() {
        return mPhoto50;
    }

    public void setPhoto50(String photo50) {
        mPhoto50 = photo50;
    }

    public String getIsClosed() {
        return mIsClosed;
    }

    public void setIsClosed(String isClosed) {
        mIsClosed = isClosed;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getScreenName() {
        return mScreenName;
    }

    public void setScreenName(String screenName) {
        mScreenName = screenName;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(mId);
        dest.writeString(mPhoto200);
        dest.writeString(mPhoto100);
        dest.writeString(mPhoto50);
        dest.writeString(mIsClosed);
        dest.writeString(mName);
        dest.writeString(mScreenName);
        dest.writeString(mType);
    }

    public Group(final Parcel in) {
        mId = in.readInt();
        mPhoto200 = in.readString();
        mPhoto100 = in.readString();
        mPhoto50 = in.readString();
        mIsClosed = in.readString();
        mName = in.readString();
        mScreenName = in.readString();
        mType = in.readString();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {

        @Override
        public Group createFromParcel(final Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(final int size) {
            return new Group[size];
        }

    };
}
