package com.flatstack.flatstackvk.service.background.impl;

import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.flatstack.flatstackvk.content.dao.GroupsDAO;
import com.flatstack.flatstackvk.content.dao.NewsFeedDao;
import com.flatstack.flatstackvk.content.dao.ProfilesDAO;
import com.flatstack.flatstackvk.content.dao.UserDAO;
import com.flatstack.flatstackvk.content.data.NewsFeed;
import com.flatstack.flatstackvk.content.data.UserInfo;
import com.flatstack.flatstackvk.service.background.BackgroundJob;
import com.flatstack.flatstackvk.ui.news.ContinueLoadNewsFeedResultReceiver;
import com.flatstack.flatstackvk.ui.news.ReloadNewsFeedResultReceiver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ustimov on 01.08.2015.
 */
public class QueryNewsList extends BackgroundJob<List<NewsFeed.FeedEntry>> {

    private static final String LOG_TAG = "QueryNewsList";

    private final Context mContext;
    private final Gson mJson;
    private final boolean mContinueLoad;
    private final ResultReceiver mResultReceiver;
    private final NewsFeedDao mNewsFeedDao;
    private final UserDAO mUserDAO;
    private final ProfilesDAO mProfilesDao;
    private final GroupsDAO mGroupsDao;

    public QueryNewsList(final Context context, final boolean continueLoad, final ResultReceiver resultReceiver,
                         @NonNull final NewsFeedDao newsFeedDao, @NonNull final UserDAO userDAO,
                         @NonNull final ProfilesDAO profilesDAO, @NonNull final GroupsDAO groupsDAO) {
        super();
        mContext = context;
        final GsonBuilder gsonBuilder = new GsonBuilder();
        mJson = gsonBuilder.create();
        mContinueLoad = continueLoad;
        mResultReceiver = resultReceiver;
        mNewsFeedDao = newsFeedDao;
        mUserDAO = userDAO;
        mProfilesDao = profilesDAO;
        mGroupsDao = groupsDAO;
    }

    @Nullable
    @Override
    protected List<NewsFeed.FeedEntry> doInBackground() {
        VKRequest request;
        if (mContinueLoad) {
            UserInfo userInfo = mUserDAO.get(UserInfo.CURRENT_USER_ID);
            if (userInfo != null) {
                String newsListNextPageIndicator = userInfo.getNewsListNextPageIndicator();
                request = new VKRequest("newsfeed.get", VKParameters.from(VKApiConst.FILTERS, "post", "start_from", newsListNextPageIndicator));
            } else {
                return null;
            }
        } else {
            request = new VKRequest("newsfeed.get", VKParameters.from(VKApiConst.FILTERS, "post"));
        }

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                NewsFeed newsFeed = (NewsFeed) mJson.fromJson(response.responseString, NewsFeed.class);
                if (!mContinueLoad) {
                    mNewsFeedDao.deleteAll();
                }
                mNewsFeedDao.bulkInsert(Arrays.asList(newsFeed.mResponse.mItems));
                mProfilesDao.bulkInsert(Arrays.asList(newsFeed.mResponse.mProfiles));
                mGroupsDao.bulkInsert(Arrays.asList(newsFeed.mResponse.mGroups));
                UserInfo userInfo = mUserDAO.get(UserInfo.CURRENT_USER_ID);
                if (userInfo != null) {
                    userInfo.setNewsListNextPageIndicator(newsFeed.mResponse.mNextFrom);
                    mUserDAO.insertOrUpdate(userInfo);
                }
                if (mContinueLoad) {
                    Bundle args = ContinueLoadNewsFeedResultReceiver.onNewsFeedLoadFinished(mNewsFeedDao.getCount());
                    mResultReceiver.send(ContinueLoadNewsFeedResultReceiver.RESULT_OK, args);
                } else {
                    if (mResultReceiver != null) {
                        Bundle args = ReloadNewsFeedResultReceiver.onNewsFeedLoadFinished(mNewsFeedDao.getCount());
                        mResultReceiver.send(ReloadNewsFeedResultReceiver.RESULT_OK, args);
                    }
                }
            }

            @Override
            public void onError(VKError error) {
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            }
        });

        return null;
    }

}
