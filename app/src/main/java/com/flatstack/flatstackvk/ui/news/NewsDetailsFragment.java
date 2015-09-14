package com.flatstack.flatstackvk.ui.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatstack.flatstackvk.R;
import com.flatstack.flatstackvk.content.data.Attachment;
import com.flatstack.flatstackvk.content.data.NewsFeed;
import com.flatstack.flatstackvk.ui.AbstractFragment;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;

import butterknife.Bind;

public class NewsDetailsFragment extends AbstractFragment implements View.OnClickListener{

    public static final String EXTRA_FEED_ENTRY = "EXTRA_FEED_ENTRY";

    NewsFeed.FeedEntry mFeedEntry;

    @Bind(R.id.image_1)
    ImageView mImageView1;
    @Bind(R.id.image_2)
    ImageView mImageView2;
    @Bind(R.id.image_3)
    ImageView mImageView3;
    @Bind(R.id.likes)
    TextView mLikesTextView;

    public static NewsDetailsFragment newInstance(NewsFeed.FeedEntry  feedEntry) {
        NewsDetailsFragment newsDetailsFragment = new NewsDetailsFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(EXTRA_FEED_ENTRY, feedEntry);
        newsDetailsFragment.setArguments(arguments);
        return newsDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFeedEntry = getArguments().getParcelable(EXTRA_FEED_ENTRY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_details, container, false);
    }

    private void setupToolbar() {
        if (getActivity() instanceof MainActivity) {
            final MainActivity mainActivity = (MainActivity)getActivity();
            mainActivity.setToolbarTitle(getString(R.string.news_feed));
            mainActivity.getToolbar().setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            mainActivity.getToolbar().getMenu().clear();
            mainActivity.getToolbar().setNavigationOnClickListener(this);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int imageViewCounter = 1;
        if (mFeedEntry.getAttachments() != null) {
            for (int i = 0; i < mFeedEntry.getAttachments().length; i++) {
                final Attachment attachment = mFeedEntry.getAttachments()[i];
                if (attachment.getType().equals("photo")) {
                    try {
                        Class<?> c = this.getClass();
                        Field imageViewField = c.getDeclaredField("mImageView" + imageViewCounter);
                        ImageView imageView = (ImageView) imageViewField.get(this);
                        imageView.setVisibility(View.VISIBLE);

                        Picasso.with(getActivity()).load(attachment.getPhoto().getPhoto130()).into(imageView);

                        imageViewCounter++;
                        if (imageViewCounter > 3)
                            break;
                    } catch (Exception exception) {

                    }
                }
            }
        }

        mLikesTextView.setText(mFeedEntry.getLikes().getCount());

        setupToolbar();
    }

    @Override
    public void onClick(View view) {
        getFragmentManager().popBackStack();
    }
}
