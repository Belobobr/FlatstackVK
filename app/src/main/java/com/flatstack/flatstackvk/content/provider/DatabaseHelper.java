package com.flatstack.flatstackvk.content.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.flatstack.flatstackvk.content.dao.GroupsDAO;
import com.flatstack.flatstackvk.content.dao.NewsFeedDao;
import com.flatstack.flatstackvk.content.dao.ProfilesDAO;
import com.flatstack.flatstackvk.content.dao.UserDAO;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "flatstack.vk.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        NewsFeedDao.onCreate(db);
        ProfilesDAO.onCreate(db);
        GroupsDAO.onCreate(db);
        UserDAO.onCreate(db);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        NewsFeedDao.onUpgrade(db, oldVersion, newVersion);
        ProfilesDAO.onUpgrade(db, oldVersion, newVersion);
        GroupsDAO.onUpgrade(db, oldVersion, newVersion);
        UserDAO.onUpgrade(db, oldVersion, newVersion);
    }

}