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
import com.flatstack.flatstackvk.content.data.Profile;

public class ProfilesDAO extends AbstractDAO<Profile>{

    public static final String LOG_TAG = "ProfilesDAO";

    public static final String TABLE = "profiles";

    public ProfilesDAO(Context context) {
        super(context);
    }

    public interface Columns extends BaseColumns {
        String PHOTO_100 = "photo_100";
        String PHOTO_50 = "photo_50";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
    }

    public static final Uri CONTENT_URI = DatabaseUtils.getUri(TABLE);

    public static final String[] PROJECTION =  {
            Columns._ID,
            Columns.PHOTO_100,
            Columns.PHOTO_50,
            Columns.FIRST_NAME,
            Columns.LAST_NAME,
    };

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

    public static void onCreate(final SQLiteDatabase db) {
        final DatabaseUtils.TableBuilder tableBuilder = new DatabaseUtils.TableBuilder(TABLE);
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().integer(Columns._ID));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.PHOTO_100));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.PHOTO_50));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.FIRST_NAME));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.LAST_NAME));
        tableBuilder.create(db);

        DatabaseUtils.createIndex(db, TABLE, Columns._ID, new String[] { Columns._ID });
    }

    public static void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        DatabaseUtils.dropTable(db, TABLE);
        onCreate(db);
    }

    @NonNull
    @Override
    protected Profile getItemFromCursor(Cursor cursor) {
        Profile profile = new Profile();;
        profile.setId(getId(cursor));
        profile.setPhoto100(getString(cursor, Columns.PHOTO_100));
        profile.setPhoto50(getString(cursor, Columns.PHOTO_50));
        profile.setFirstName(getString(cursor, Columns.FIRST_NAME));
        profile.setLastName(getString(cursor, Columns.LAST_NAME));
        return profile;
    }

    @NonNull
    @Override
    protected ContentValues toContentValues(@NonNull Profile entity) {
        ContentValues contentValues = new ContentValues();
        final int id = entity.getId();
        if (id != Identify.INVALID_ID) {
            contentValues.put(Columns._ID, id);
        }
        contentValues.put(Columns.PHOTO_100, entity.getPhoto100());
        contentValues.put(Columns.PHOTO_50, entity.getPhoto50());
        contentValues.put(Columns.FIRST_NAME, entity.getFirstName());
        contentValues.put(Columns.LAST_NAME, entity.getLastName());
        return contentValues;
    }
}
