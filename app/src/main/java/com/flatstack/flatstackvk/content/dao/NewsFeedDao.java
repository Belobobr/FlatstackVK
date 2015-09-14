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
import com.flatstack.flatstackvk.content.data.Attachment;
import com.flatstack.flatstackvk.content.data.NewsFeed;

public class NewsFeedDao extends AbstractDAO<NewsFeed.FeedEntry> {

    public static final String LOG_TAG = "NewsFeedDao";

    public static final String TABLE = "news_feed";

    public NewsFeedDao(Context context) {
        super(context);
    }

    public interface Columns extends BaseColumns {
        String TYPE = "type";
        String SOURCE_ID = "source_id";
        String DATE = "date";
        String POST_ID = "post_id";
        String TEXT = "text";
        String LIKES = "likes";
        String ATTACHMENTS = "attachments";
    }

    public static final Uri CONTENT_URI = DatabaseUtils.getUri(TABLE);

    public static final String[] PROJECTION =  {
            Columns._ID,
            Columns.TYPE,
            Columns.SOURCE_ID,
            Columns.DATE,
            Columns.POST_ID,
            Columns.TEXT,
            Columns.LIKES,
            Columns.ATTACHMENTS
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
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.TYPE));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().integer(Columns.SOURCE_ID));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().integer(Columns.DATE));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().integer(Columns.POST_ID));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.TEXT));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.LIKES));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.ATTACHMENTS));
        tableBuilder.create(db);

        DatabaseUtils.createIndex(db, TABLE, Columns._ID, new String[] { Columns._ID });
    }

    public static void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        DatabaseUtils.dropTable(db, TABLE);
        onCreate(db);
    }

    @NonNull
    @Override
    protected NewsFeed.FeedEntry getItemFromCursor(Cursor cursor) {
        NewsFeed.FeedEntry feedEntry = new NewsFeed.FeedEntry();
        feedEntry.setId(getId(cursor));
        feedEntry.setType(getString(cursor, Columns.TYPE));
        feedEntry.setSourceId(getInt(cursor, Columns.SOURCE_ID));
        feedEntry.setDate(getLong(cursor, Columns.DATE));
        feedEntry.setPostId(getLong(cursor, Columns.POST_ID));
        feedEntry.setText(getString(cursor, Columns.TEXT));
        feedEntry.setLikes(NewsFeed.Likes.getLikesFromJson(getString(cursor, Columns.LIKES)));
        feedEntry.setAttachments(Attachment.getAttachmentsFromJson(getString(cursor, Columns.ATTACHMENTS)));
        return feedEntry;
    }

    @NonNull
    @Override
    protected ContentValues toContentValues(@NonNull NewsFeed.FeedEntry entity) {
        ContentValues contentValues = new ContentValues();
        final int id = entity.getId();
        if (id != Identify.INVALID_ID) {
            contentValues.put(Columns._ID, id);
        }
        contentValues.put(Columns.TYPE, entity.getType());
        contentValues.put(Columns.SOURCE_ID, entity.getSourceId());
        contentValues.put(Columns.DATE, entity.getDate());
        contentValues.put(Columns.POST_ID, entity.getPostId());
        contentValues.put(Columns.TEXT, entity.getText());
        contentValues.put(Columns.LIKES, NewsFeed.Likes.likesToJson(entity.getLikes()));
        contentValues.put(Columns.ATTACHMENTS, Attachment.attachmentsToJson(entity.getAttachments()));
        return contentValues;
    }
}
