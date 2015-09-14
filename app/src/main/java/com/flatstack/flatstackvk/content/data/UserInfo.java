package com.flatstack.flatstackvk.content.data;


import com.flatstack.flatstackvk.content.dao.Identify;

public class UserInfo implements Identify {

    private int mId = CURRENT_USER_ID;

    String mNewsListNextPageIndicator;

    public static final int CURRENT_USER_ID = 0;

    @Override
    public int getId() {
        return 0;
    }

    public void setId(int id) {
    }

    public String getNewsListNextPageIndicator() {
        return mNewsListNextPageIndicator;
    }

    public void setNewsListNextPageIndicator(String newsListNextPageIndicator) {
        mNewsListNextPageIndicator = newsListNextPageIndicator;
    }


}
