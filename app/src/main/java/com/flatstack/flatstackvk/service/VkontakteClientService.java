package com.flatstack.flatstackvk.service;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;

import com.flatstack.flatstackvk.content.dao.GroupsDAO;
import com.flatstack.flatstackvk.content.dao.NewsFeedDao;
import com.flatstack.flatstackvk.content.dao.ProfilesDAO;
import com.flatstack.flatstackvk.content.dao.UserDAO;
import com.flatstack.flatstackvk.service.background.BackgroundExecutorService;
import com.flatstack.flatstackvk.service.background.BackgroundJob;
import com.flatstack.flatstackvk.service.background.impl.QueryNewsList;
import com.flatstack.flatstackvk.ui.news.ContinueLoadNewsFeedResultReceiver;
import com.flatstack.flatstackvk.ui.news.ReloadNewsFeedResultReceiver;


public class VkontakteClientService extends LiveLongAndProsperIntentService {

    private static final String NAME = "VkClientService";

    private static final String ACTION_QUERY_NEWS_FEED = "VkontakteClientService.ACTION_QUERY_NEWS_FEED";
    private static final String ACTION_CONTINUE_LOAD_NEWS_FEED = "VkontakteClientService.CONTINUE_LOAD_NEWS_FEED";

    private static final String EXTRA_RESULT_RECEIVER = "result_receiver";


    public static void queryNewsFeed(final Context context, final ReloadNewsFeedResultReceiver reloadNewsFeedResultReceiver) {
        final Intent intent = createActionIntent(context, ACTION_QUERY_NEWS_FEED);
        intent.putExtra(EXTRA_RESULT_RECEIVER, reloadNewsFeedResultReceiver);
        context.startService(intent);
    }

    public static void continueLoadNewsFeed(final Context context, final ContinueLoadNewsFeedResultReceiver continueLoadNewsFeedResultReceiver) {
        final Intent intent = createActionIntent(context, ACTION_CONTINUE_LOAD_NEWS_FEED);
        intent.putExtra(EXTRA_RESULT_RECEIVER, continueLoadNewsFeedResultReceiver);
        context.startService(intent);
    }

    @NonNull
    private static Intent createActionIntent(final Context context, final String action) {
        final Intent intent = new Intent(context, VkontakteClientService.class);
        intent.setAction(action);
        return intent;
    }

    private BackgroundExecutorService mBackgroundExecutorService;
    private NewsFeedDao mNewsFeedDao;
    private UserDAO mUserDAO;
    private GroupsDAO mGroupsDAO;
    private ProfilesDAO mProfilesDAO;

    public VkontakteClientService() {
        super(NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBackgroundExecutorService = new BackgroundExecutorService();
        mNewsFeedDao = new NewsFeedDao(this);
        mUserDAO = new UserDAO(this);
        mProfilesDAO = new ProfilesDAO(this);
        mGroupsDAO = new GroupsDAO(this);
    }

    @Override
    public void onDestroy() {
        mBackgroundExecutorService.shutdown();
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(@NonNull final Intent intent) {
        final String action = intent.getAction();
        if (ACTION_QUERY_NEWS_FEED.equals(action)) {
            final ResultReceiver resultReceiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
            onQueryNewsFeed(resultReceiver);
        } else if (ACTION_CONTINUE_LOAD_NEWS_FEED.equals(action)) {
            final ResultReceiver resultReceiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
            onContinueLoadNewsFeed(resultReceiver);
        }
    }

    private void onQueryNewsFeed(final ResultReceiver resultReceiver) {
        final BackgroundJob<?> job = new QueryNewsList(this, false, resultReceiver, mNewsFeedDao, mUserDAO, mProfilesDAO, mGroupsDAO);
        mBackgroundExecutorService.submit(job);
    }

    private void onContinueLoadNewsFeed(final ResultReceiver resultReceiver) {
        final BackgroundJob<?> job = new QueryNewsList(this, true, resultReceiver, mNewsFeedDao, mUserDAO, mProfilesDAO, mGroupsDAO);
        mBackgroundExecutorService.submit(job);
    }


}
