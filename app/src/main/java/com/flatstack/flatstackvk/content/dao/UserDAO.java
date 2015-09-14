package com.flatstack.flatstackvk.content.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.flatstack.flatstackvk.content.DatabaseUtils;
import com.flatstack.flatstackvk.content.data.UserInfo;

public class UserDAO extends AbstractDAO<UserInfo> {

    public static final String LOG_TA = "UserDao";

    public static final String TABLE = "user";

    public interface Columns extends BaseColumns {
        String NEWS_LIST_NEXT_PAGE_INDICATOR = "news_list_next_page_indicator";
    }

    public static final Uri CONTENT_URI = DatabaseUtils.getUri(TABLE);

    public static final String[] PROJECTION =  {
            Columns._ID,
            Columns.NEWS_LIST_NEXT_PAGE_INDICATOR,
    };

    public static void onCreate(final SQLiteDatabase db) {
        final DatabaseUtils.TableBuilder tableBuilder = new DatabaseUtils.TableBuilder(TABLE);
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().integer(Columns._ID));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().integer(Columns.NEWS_LIST_NEXT_PAGE_INDICATOR));
        tableBuilder.create(db);

        DatabaseUtils.createIndex(db, TABLE, Columns._ID, new String[] { Columns._ID });
    }

    public static void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        DatabaseUtils.dropTable(db, TABLE);
        onCreate(db);
    }

    public UserDAO(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected Uri getTableUri() {
        return CONTENT_URI;
    }

    @Nullable
    @Override
    protected String[] getProjection() {
        return PROJECTION;
    }

    @NonNull
    @Override
    protected UserInfo getItemFromCursor(Cursor cursor) {
        final UserInfo userInfo = new UserInfo();
        userInfo.setId(getId(cursor));
        userInfo.setNewsListNextPageIndicator(getString(cursor, Columns.NEWS_LIST_NEXT_PAGE_INDICATOR));
        return userInfo;
    }

    @NonNull
    @Override
    protected ContentValues toContentValues(@NonNull UserInfo userInfo) {
        ContentValues contentValues = new ContentValues();
        final int id = userInfo.getId();
        if (id != Identify.INVALID_ID) {
            contentValues.put(Columns._ID, id);
        }
        contentValues.put(Columns.NEWS_LIST_NEXT_PAGE_INDICATOR, userInfo.getNewsListNextPageIndicator());
        return contentValues;
    }
}
