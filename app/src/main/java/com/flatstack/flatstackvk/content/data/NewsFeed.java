package com.flatstack.flatstackvk.content.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.flatstack.flatstackvk.content.dao.Identify;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;

public class NewsFeed {

    @SerializedName("response")
    public Response mResponse;

    public static class Response {
        @SerializedName("items")
        public FeedEntry[] mItems;

        @SerializedName("new_offset")
        public String mNewOffset;

        @SerializedName("next_from")
        public String mNextFrom;

        @SerializedName("groups")
        public Group[] mGroups;

        @SerializedName("profiles")
        public Profile[] mProfiles;
    }

    public static class FeedEntry implements Identify, Parcelable{

        public FeedEntry() {
        }

        int mId = INVALID_ID;

        @SerializedName("type")
        String mType;

        @SerializedName("source_id")
        int mSourceId;

        @SerializedName("date")
        long mDate;

        @SerializedName("post_id")
        long mPostId;

        @SerializedName("text")
        String mText;

        @SerializedName("likes")
        Likes mLikes;

        Group mGroup;

        Profile mProfile;

        @SerializedName("attachments")
        Attachment[] mAttachments;

        @Override
        public int getId() {
            return mId;
        }

        public void setId(int id) {
            mId = id;
        }

        public String getType() {
            return mType;
        }

        public void setType(String type) {
            mType = type;
        }

        public int getSourceId() {
            return mSourceId;
        }

        public void setSourceId(int sourceId) {
            mSourceId = sourceId;
        }

        public long getDate() {
            return mDate;
        }

        public void setDate(long date) {
            mDate = date;
        }

        public long getPostId() {
            return mPostId;
        }

        public void setPostId(long postId) {
            mPostId = postId;
        }

        public String getText() {
            return mText;
        }

        public void setText(String text) {
            mText = text;
        }

        public Likes getLikes() {
            return mLikes;
        }

        public void setLikes(Likes likes) {
            mLikes = likes;
        }

        public Group getGroup() {
            return mGroup;
        }

        public void setGroup(Group group) {
            mGroup = group;
        }

        public Profile getProfile() {
            return mProfile;
        }

        public void setProfile(Profile profile) {
            mProfile = profile;
        }

        @Nullable
        public Attachment[] getAttachments() {
            return mAttachments;
        }

        public void setAttachments(Attachment[] attachments) {
            mAttachments = attachments;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeInt(mId);
            dest.writeInt(mSourceId);
            dest.writeLong(mDate);
            dest.writeLong(mPostId);
            dest.writeString(mText);
            dest.writeString(Likes.likesToJson(mLikes));
            dest.writeParcelable(mGroup, 0);
            dest.writeParcelable(mProfile, 0);
            dest.writeParcelableArray(mAttachments, 0);
        }

        public FeedEntry(final Parcel in) {
            mId = in.readInt();
            mSourceId = in.readInt();
            mDate = in.readLong();
            mPostId = in.readLong();
            mText = in.readString();
            mLikes = Likes.getLikesFromJson(in.readString());
            mGroup = (Group)in.readParcelable(Group.class.getClassLoader());
            mProfile = (Profile)in.readParcelable(Profile.class.getClassLoader());
            mAttachments = (Attachment[])in.readParcelableArray(Attachment[].class.getClassLoader());
        }

        public static final Creator<FeedEntry> CREATOR = new Creator<FeedEntry>() {

            @Override
            public NewsFeed.FeedEntry createFromParcel(final Parcel in) {
                return new NewsFeed.FeedEntry(in);
            }

            @Override
            public NewsFeed.FeedEntry[] newArray(final int size) {
                return new NewsFeed.FeedEntry[size];
            }

        };
    }

    public static class Likes{
        @SerializedName("count")
        String mCount;

        public String getCount() {
            return mCount;
        }

        public void setCount(String count) {
            mCount = count;
        }

        @Nullable
        public static Likes getLikesFromJson(final String json) {
            Likes likes;
            JsonReader jsonReader = null;
            try {
                jsonReader = new JsonReader(new StringReader(json));
                jsonReader.setLenient(true);
                Gson gson = new Gson();
                likes = gson.fromJson(jsonReader, Likes.class);
            } finally {
                if (jsonReader != null) {
                    try {
                        jsonReader.close();
                    } catch (final IOException ioe) {
                    }
                }
            }

            return likes;
        }

        public static String likesToJson(@NonNull final Likes likes) {
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();

            // Format to JSON
            final String json = gson.toJson(likes);
            return json;
        }
    }
}
