package com.flatstack.flatstackvk.ui.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatstack.flatstackvk.R;
import com.flatstack.flatstackvk.content.data.Group;
import com.flatstack.flatstackvk.content.data.NewsFeed;
import com.flatstack.flatstackvk.content.data.Profile;
import com.flatstack.flatstackvk.ui.OnItemClickListener;
import com.flatstack.flatstackvk.utils.DateTimeUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class NewsFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int FOOTER = 1;

    List<NewsFeed.FeedEntry> mItems;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;
    private boolean mHasFooter;

    public NewsFeedAdapter(OnItemClickListener onItemClickListener) {
        mItems = new ArrayList<>();
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mItems.size()) {
            return FOOTER;
        } else {
            return ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int itemViewType) {
        if (itemViewType == ITEM) {
            return new FeedEntryViewHolder(inflate(R.layout.layout_news_list_item, viewGroup), mOnItemClickListener);
        } else {
            return new ProgressViewHolder(inflate(R.layout.layout_load_more_indicator, viewGroup));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (position != mItems.size()) {
            NewsFeed.FeedEntry item = getItem(position);
            FeedEntryViewHolder feedEntryViewHolder = (FeedEntryViewHolder)viewHolder;
            feedEntryViewHolder.bind(item, position);
        }
    }

    @Override
    public int getItemCount() {
        return mHasFooter ? mItems.size() + 1 : mItems.size();
    }

    public void setItems(List<NewsFeed.FeedEntry> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void addFooter() {
        mHasFooter = true;
        notifyItemInserted(mItems.size());
    }

    public void removeFooter() {
        mHasFooter = false;
        notifyItemRemoved(mItems.size());
    }

    public NewsFeed.FeedEntry getItem(int position) {
        return mItems.get(position);
    }

    public View inflate(int layoutRes, ViewGroup parent) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        return mLayoutInflater.inflate(layoutRes, parent, false);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class FeedEntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        OnItemClickListener mOnItemClickListener;
        int mPosition;
        Context mContext;

        @Bind(R.id.title)
        TextView mTitle;
        @Bind(R.id.date)
        TextView mDateTextView;
        @Bind(R.id.avatar)
        ImageView mAvatar;
        @Bind(R.id.description)
        TextView mDescriptionTextView;

        public FeedEntryViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            mOnItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        public void bind(NewsFeed.FeedEntry feedEntry, int position) {
            mPosition = position;
            mDateTextView.setText(DateTimeUtils.getFormattedTime(feedEntry.getDate()));
            mDescriptionTextView.setText(feedEntry.getText());
            if (feedEntry.getText() == null || feedEntry.getText().isEmpty()) {
                mDescriptionTextView.setVisibility(View.GONE);
            } else {
                mDescriptionTextView.setVisibility(View.VISIBLE);
            }

            String avatarUrl;
            String title;
            if (feedEntry.getSourceId() < 0) {
                final Group group = feedEntry.getGroup();
                avatarUrl = group.getPhoto50();
                title = group.getName();
            } else {
                final Profile profile = feedEntry.getProfile();
                avatarUrl = profile.getPhoto50();
                title = profile.getFirstName() + " " + profile.getLastName();
            }
            mTitle.setText(title);
            Picasso.with(mContext).load(avatarUrl).into(mAvatar);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClickListener(mPosition);
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }
}

