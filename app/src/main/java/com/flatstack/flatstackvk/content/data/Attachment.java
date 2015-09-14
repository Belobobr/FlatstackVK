package com.flatstack.flatstackvk.content.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;

public class Attachment implements Parcelable {

    public Attachment() {
    }

    @SerializedName("type")
    private String mType;

    @SerializedName("photo")
    private Photo mPhoto;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mType);
        parcel.writeParcelable(mPhoto, 0);
    }

    public Attachment(final Parcel in) {
        mType = in.readString();
        mPhoto = in.readParcelable(Photo.class.getClassLoader());
    }

    public static final Creator<Attachment> CREATOR = new Creator<Attachment>() {

        @Override
        public Attachment createFromParcel(final Parcel in) {
            return new Attachment(in);
        }

        @Override
        public Attachment[] newArray(final int size) {
            return new Attachment[size];
        }

    };

    @Nullable
    public static Attachment[] getAttachmentsFromJson(final String json) {
        Attachment attachments[];
        JsonReader jsonReader = null;
        try {
            jsonReader = new JsonReader(new StringReader(json));
            jsonReader.setLenient(true);
            Gson gson = new Gson();
            attachments = gson.fromJson(jsonReader, Attachment[].class);
        } finally {
            if (jsonReader != null) {
                try {
                    jsonReader.close();
                } catch (final IOException ioe) {
                }
            }
        }

        return attachments;
    }

    public static String attachmentsToJson(@NonNull final Attachment[] attachment) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        // Format to JSON
        final String json = gson.toJson(attachment);
        return json;
    }
}
