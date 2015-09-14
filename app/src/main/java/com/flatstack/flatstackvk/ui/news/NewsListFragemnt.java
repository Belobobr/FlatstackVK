package com.flatstack.flatstackvk.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.flatstack.flatstackvk.R;
import com.flatstack.flatstackvk.content.data.NewsFeed;
import com.flatstack.flatstackvk.content.loader.NewsFeedLoader;
import com.flatstack.flatstackvk.service.VkontakteClientService;
import com.flatstack.flatstackvk.ui.AbstractFragment;
import com.flatstack.flatstackvk.ui.OnItemClickListener;
import com.vk.sdk.VKSdk;

import java.util.List;

import butterknife.Bind;

public class NewsListFragemnt extends AbstractFragment implements LoaderManager.LoaderCallbacks<List<NewsFeed.FeedEntry>>,
        OnItemClickListener, ContinueLoadNewsFeedResultReceiver.OnNewsFeedLoaded,
        Toolbar.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener, ReloadNewsFeedResultReceiver.OnNewsFeedReloadedListener {

    public static final String EXTRA_CONTINUE_LOAD_RESULT_RECEIVER = "continue_load_receiver";
    public static final String EXTRA_RELOAD_NEWS_FEED_RESULT_RECEIVER = "reload_news_feed_result_receiver";

    @Bind(R.id.recyrcle_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.progress_bar)
    View mProgressBar;

    NewsFeedAdapter mNewsFeedAdapter;
    int mNewsFeedItemCount;

    public static NewsListFragemnt newInstance() {
        NewsListFragemnt videoLibraryFragment = new NewsListFragemnt();
        Bundle arguments = new Bundle();
        videoLibraryFragment.setArguments(arguments);
        return videoLibraryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            requestNewsFeed();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_feed_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(R.id.news_feed_loader, null, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNewsFeedAdapter = new NewsFeedAdapter(this);
        mRecyclerView.setAdapter(mNewsFeedAdapter);
        mRecyclerView.addOnScrollListener(mScrollListener);
        setupToolbar();
        final ContinueLoadNewsFeedResultReceiver receiver = getArguments().getParcelable(EXTRA_CONTINUE_LOAD_RESULT_RECEIVER);
        if (receiver != null) {
            receiver.setOnContinueLoadNewsFeedFinished(this);
        }
        final ReloadNewsFeedResultReceiver reloadNewsFeedResultReceiver = getArguments().getParcelable(EXTRA_RELOAD_NEWS_FEED_RESULT_RECEIVER);
        if (reloadNewsFeedResultReceiver != null) {
            reloadNewsFeedResultReceiver.setOnReloadNewsFeedListener(this);
            if (mNewsFeedAdapter.getItemCount() == 0) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        }
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        requestNewsFeed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        final ContinueLoadNewsFeedResultReceiver receiver = getArguments().getParcelable(EXTRA_CONTINUE_LOAD_RESULT_RECEIVER);
        if (receiver != null) {
            receiver.setOnContinueLoadNewsFeedFinished(null);
        }
    }

    private void setupToolbar() {
        if (getActivity() instanceof MainActivity) {
            final MainActivity mainActivity = (MainActivity)getActivity();
            mainActivity.setToolbarTitle(getString(R.string.news_feed));
            mainActivity.setMenuItemClickListener(this);
            mainActivity.inflateMenuToToolbar(R.menu.menu_new_list_fragment);
            mainActivity.getToolbar().setNavigationIcon(null);
        }
    }

    private void requestNewsFeed() {
        final ReloadNewsFeedResultReceiver receiver = new ReloadNewsFeedResultReceiver();
        getArguments().putParcelable(EXTRA_RELOAD_NEWS_FEED_RESULT_RECEIVER, receiver);
        receiver.setOnReloadNewsFeedListener(this);
        VkontakteClientService.queryNewsFeed(getActivity(), receiver);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new NewsFeedLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsFeed.FeedEntry>> loader, List<NewsFeed.FeedEntry> data) {
        mNewsFeedAdapter.setItems(data);
        final ContinueLoadNewsFeedResultReceiver receiver = getArguments().getParcelable(EXTRA_CONTINUE_LOAD_RESULT_RECEIVER);
        if (receiver != null) {
            mNewsFeedAdapter.addFooter();
        }
        mNewsFeedItemCount = data.size();
    }

    @Override
    public void onLoaderReset(Loader<List<NewsFeed.FeedEntry>> loader) {

    }

    @Override
    public void onItemClickListener(int position) {
        if (getActivity() instanceof MainActivity) {
            final MainActivity mainActivity = (MainActivity)getActivity();
            mainActivity.replaceFragment(NewsDetailsFragment.newInstance(mNewsFeedAdapter.getItem(position)), true);
        }
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LinearLayoutManager layoutManager = ((LinearLayoutManager)mRecyclerView.getLayoutManager());
            int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
            if (lastVisiblePosition == mNewsFeedAdapter.getItemCount() - 1) {
                ContinueLoadNewsFeedResultReceiver receiver = getArguments().getParcelable(EXTRA_CONTINUE_LOAD_RESULT_RECEIVER);
                if (receiver == null && mNewsFeedItemCount == mNewsFeedAdapter.getItemCount()) {
                    receiver = new ContinueLoadNewsFeedResultReceiver();
                    receiver.setOnContinueLoadNewsFeedFinished(NewsListFragemnt.this);
                    getArguments().putParcelable(EXTRA_CONTINUE_LOAD_RESULT_RECEIVER, receiver);
                    mNewsFeedAdapter.addFooter();

                    VkontakteClientService.continueLoadNewsFeed(getActivity(), receiver);
                }
            }
        }
    };

    @Override
    public void onNewsFeedLoaded(final int newsFeedItemCount) {
        getArguments().putParcelable(EXTRA_CONTINUE_LOAD_RESULT_RECEIVER, null);
        mNewsFeedItemCount = newsFeedItemCount;
        mNewsFeedAdapter.removeFooter();
    }

    @Override
    public void onNewsFeedReloaded(int newsFeedListCount) {
        getArguments().putParcelable(EXTRA_RELOAD_NEWS_FEED_RESULT_RECEIVER, null);
        mSwipeRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                //TODO https://github.com/VKCOM/vk-android-sdk/issues/37 ???????? ????? ?????????? ? ???????
                VKSdk.logout();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                return true;
            default:
                return false;
        }
    }
}
