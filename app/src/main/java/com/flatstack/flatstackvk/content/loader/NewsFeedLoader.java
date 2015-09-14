package com.flatstack.flatstackvk.content.loader;

import android.content.Context;

import com.flatstack.flatstackvk.content.dao.GroupsDAO;
import com.flatstack.flatstackvk.content.dao.NewsFeedDao;
import com.flatstack.flatstackvk.content.dao.ProfilesDAO;
import com.flatstack.flatstackvk.content.data.Group;
import com.flatstack.flatstackvk.content.data.NewsFeed;
import com.flatstack.flatstackvk.content.data.Profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsFeedLoader extends AbstractContentProviderLoader<List<NewsFeed.FeedEntry>>{

    private NewsFeedDao mNewsFeedDao;
    private ProfilesDAO mProfilesDAO;
    private GroupsDAO mGroupsDAO;

    public NewsFeedLoader(final Context context) {
        super(context, NewsFeedDao.CONTENT_URI);
        mNewsFeedDao = new NewsFeedDao(context);
        mProfilesDAO = new ProfilesDAO(context);
        mGroupsDAO = new GroupsDAO(context);
    }

    @Override
    public List<NewsFeed.FeedEntry> loadInBackground() {
        List<NewsFeed.FeedEntry> feedEntries =  mNewsFeedDao.getAll();
        List<Profile> profiles = mProfilesDAO.getAll();
        List<Group> groups = mGroupsDAO.getAll();
        Map<Integer, Profile> profilesMap = new HashMap<>();
        Map<Integer, Group> groupsMap = new HashMap<>();
        for (Profile profile : profiles) {
            profilesMap.put(profile.getId(), profile);
        }
        for (Group group : groups) {
            groupsMap.put(group.getId(), group);
        }
        for (NewsFeed.FeedEntry feedEntry : feedEntries) {
            if (feedEntry.getSourceId() < 0) {
                int groupId = Math.abs(feedEntry.getSourceId());
                feedEntry.setGroup(groupsMap.get(groupId));
            } else {
                feedEntry.setProfile(profilesMap.get(feedEntry.getSourceId()));
            }
        }
        return feedEntries;
    }
}
