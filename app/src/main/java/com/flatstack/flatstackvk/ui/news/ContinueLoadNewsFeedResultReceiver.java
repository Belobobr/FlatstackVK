package com.flatstack.flatstackvk.ui.news;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

public class ContinueLoadNewsFeedResultReceiver extends ResultReceiver {

    public static final int RESULT_OK = -1;

    private static final String EXTRA_NEWS_FEED_LIST_COUNT = "extra_news_feed_list_count";

    public interface OnNewsFeedLoaded {
        void onNewsFeedLoaded(int newsFeedListCount);
    }

    @NonNull
    public static Bundle onNewsFeedLoadFinished(final int feedEntryItemCount) {
        final Bundle args = new Bundle();
        args.putInt(EXTRA_NEWS_FEED_LIST_COUNT, feedEntryItemCount);
        return args;
    }

    private WeakReference<OnNewsFeedLoaded> mNewsFeedLoadListener;

    public ContinueLoadNewsFeedResultReceiver() {
        super(new Handler(Looper.getMainLooper()));
    }

    public void setOnContinueLoadNewsFeedFinished(@Nullable final OnNewsFeedLoaded onNewsFeedLoaded) {
        mNewsFeedLoadListener = new WeakReference<>(onNewsFeedLoaded);
    }

    @Override
    protected void onReceiveResult(final int resultCode, final Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        final OnNewsFeedLoaded onNewsFeedLoaded = (mNewsFeedLoadListener != null ?
                mNewsFeedLoadListener.get() : null);
        if (onNewsFeedLoaded != null) {
            int newsFeedCount = resultData.getInt(EXTRA_NEWS_FEED_LIST_COUNT);
            onNewsFeedLoaded.onNewsFeedLoaded(newsFeedCount);
        }
    }

}

