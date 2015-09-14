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
import com.flatstack.flatstackvk.content.data.Group;

public class GroupsDAO extends AbstractDAO<Group>{

    public static final String LOG_TAG = "GroupsDAO";

    public static final String TABLE = "groups";

    public GroupsDAO(Context context) {
        super(context);
    }

    public interface Columns extends BaseColumns {
        String PHOTO_200 = "photo_200";
        String PHOTO_100 = "photo_100";
        String PHOTO_50 = "photo_50";
        String IS_CLOSED = "is_closed";
        String NAME = "name";
        String SCREEN_NAME = "screen_name";
        String TYPE = "type";
    }

    public static final Uri CONTENT_URI = DatabaseUtils.getUri(TABLE);

    public static final String[] PROJECTION =  {
            Columns._ID,
            Columns.PHOTO_200,
            Columns.PHOTO_100,
            Columns.PHOTO_50,
            Columns.IS_CLOSED,
            Columns.NAME,
            Columns.SCREEN_NAME,
            Columns.TYPE
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
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.PHOTO_200));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.PHOTO_100));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.PHOTO_50));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.IS_CLOSED));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.NAME));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.SCREEN_NAME));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.TYPE));
        tableBuilder.create(db);

        DatabaseUtils.createIndex(db, TABLE, Columns._ID, new String[] { Columns._ID });
    }

    public static void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        DatabaseUtils.dropTable(db, TABLE);
        onCreate(db);
    }

    @NonNull
    @Override
    protected Group getItemFromCursor(Cursor cursor) {
        Group group = new Group();
        group.setId(getId(cursor));
        group.setPhoto200(getString(cursor, Columns.PHOTO_200));
        group.setPhoto100(getString(cursor, Columns.PHOTO_100));
        group.setPhoto50(getString(cursor, Columns.PHOTO_50));
        group.setIsClosed(getString(cursor, Columns.IS_CLOSED));
        group.setName(getString(cursor, Columns.NAME));
        group.setScreenName(getString(cursor, Columns.SCREEN_NAME));
        group.setType(getString(cursor, Columns.TYPE));
        return group;
    }

    @NonNull
    @Override
    protected ContentValues toContentValues(@NonNull Group entity) {
        ContentValues contentValues = new ContentValues();
        final int id = entity.getId();
        if (id != Identify.INVALID_ID) {
            contentValues.put(Columns._ID, id);
        }
        contentValues.put(Columns.TYPE, entity.getType());
        contentValues.put(Columns.PHOTO_200, entity.getPhoto200());
        contentValues.put(Columns.PHOTO_100, entity.getPhoto100());
        contentValues.put(Columns.PHOTO_50, entity.getPhoto50());
        contentValues.put(Columns.IS_CLOSED, entity.getIsClosed());
        contentValues.put(Columns.NAME, entity.getName());
        contentValues.put(Columns.SCREEN_NAME, entity.getScreenName());
        contentValues.put(Columns.TYPE, entity.getType());
        return contentValues;
    }
}
