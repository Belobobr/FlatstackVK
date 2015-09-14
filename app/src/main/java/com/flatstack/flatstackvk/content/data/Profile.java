package com.flatstack.flatstackvk.content.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.flatstack.flatstackvk.content.dao.Identify;
import com.google.gson.annotations.SerializedName;

public class Profile implements Identify, Parcelable{

    public Profile() {
    }

    @SerializedName("id")
    int mId;

    @SerializedName("first_name")
    String mFirstName;

    @SerializedName("sex")
    String mSex;

    @SerializedName("photo_100")
    String mPhoto100;

    @SerializedName("photo_50")
    String mPhoto50;

    @SerializedName("last_name")
    String mLastName;

    @SerializedName("screen_name")
    String mScreenName;

    @SerializedName("online_mobile")
    String mOnlineMobile;

    @SerializedName("online_app")
    String mOnlineApp;

    @SerializedName("online")
    String mOnline;

    @Override
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getSex() {
        return mSex;
    }

    public void setSex(String sex) {
        mSex = sex;
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

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getScreenName() {
        return mScreenName;
    }

    public void setScreenName(String screenName) {
        mScreenName = screenName;
    }

    public String getOnlineMobile() {
        return mOnlineMobile;
    }

    public void setOnlineMobile(String onlineMobile) {
        mOnlineMobile = onlineMobile;
    }

    public String getOnlineApp() {
        return mOnlineApp;
    }

    public void setOnlineApp(String onlineApp) {
        mOnlineApp = onlineApp;
    }

    public String getOnline() {
        return mOnline;
    }

    public void setOnline(String online) {
        mOnline = online;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(mId);
        dest.writeString(mFirstName);
        dest.writeString(mSex);
        dest.writeString(mPhoto100);
        dest.writeString(mPhoto50);
        dest.writeString(mLastName);
        dest.writeString(mScreenName);
        dest.writeString(mOnlineMobile);
        dest.writeString(mOnlineApp);
        dest.writeString(mOnline);
    }

    public Profile(final Parcel in) {
        mId = in.readInt();
        mFirstName = in.readString();
        mSex = in.readString();
        mPhoto100 = in.readString();
        mPhoto50 = in.readString();
        mLastName = in.readString();
        mScreenName = in.readString();
        mOnlineMobile = in.readString();
        mOnlineApp = in.readString();
        mOnline = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {

        @Override
        public Profile createFromParcel(final Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(final int size) {
            return new Profile[size];
        }

    };
}
