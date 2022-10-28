package com.edatech.devadmin;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class MessagesContentProvider extends ContentProvider {
    static private final String TAG = MessagesContentProvider.class.getSimpleName();
    static private final boolean DEBUG = true;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(EdatechConstants.AUTHORITY, EdatechConstants.MessengersTable.PATH_TABLE,
                EdatechConstants.TABLE_MESSENGERS_DATA);
        uriMatcher.addURI(EdatechConstants.AUTHORITY, EdatechConstants.MessengersTable.PATH_ITEM,
                EdatechConstants.TABLE_MESSENGERS_ITEM);
    }

    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, EdatechConstants.MessengersTable.CREATE_SQL, null, EdatechConstants.DATA_BASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            if (DEBUG) {
                Log.d(TAG, "execute sql '" + EdatechConstants.MessengersTable.CREATE_SQL + "'");
            }

            try {
                db.execSQL(EdatechConstants.MessengersTable.CREATE_SQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    DBHelper mDbHelper;
    SQLiteDatabase mDb;

    public MessagesContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = "";

        switch (uriMatcher.match(uri)) {
            case EdatechConstants.TABLE_MESSENGERS_ITEM:
                tableName = EdatechConstants.MessengersTable.TABLE_NAME;
                String _idStr = uri.getLastPathSegment();

                if (!TextUtils.isEmpty(selection)) {
                    selection += selection += " AND ";
                } else {
                    selection = "";
                }

                selection += EdatechConstants.MessengersTable._ID + " = " + _idStr;
                break;

            case EdatechConstants.TABLE_MESSENGERS_DATA:
                tableName = EdatechConstants.MessengersTable.TABLE_NAME;
                break;
        }

        mDb = mDbHelper.getWritableDatabase();
        int count = mDb.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case EdatechConstants.TABLE_MESSENGERS_DATA:
                return EdatechConstants.MessengersTable.MESSENGERS_CONTENT_TYPE_DATA;
            case EdatechConstants.TABLE_MESSENGERS_ITEM:
                return EdatechConstants.MessengersTable.MESSENGERS_CONTENT_TYPE_ITEM;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (DEBUG) {
            Log.d(TAG, "uri " + uri);
        }
        Uri resultUri = null;
        String insertion = "";

        switch (uriMatcher.match(uri)) {
            case EdatechConstants.TABLE_MESSENGERS_ITEM:
                if (!values.containsKey(EdatechConstants.MessengersTable._ID)) {
                    values.put(EdatechConstants.MessengersTable._ID, uri.getLastPathSegment());
                }
            case EdatechConstants.TABLE_MESSENGERS_DATA:
                mDb = mDbHelper.getWritableDatabase();
                long _id = mDb.insert(EdatechConstants.MessengersTable.TABLE_NAME, null, values);
                resultUri = ContentUris.withAppendedId(EdatechConstants.MessengersTable.CONTENT_URI, _id);
                getContext().getContentResolver().notifyChange(resultUri, null);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        return resultUri;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DBHelper(getContext());
        ContentValues values = new ContentValues();
        values.put(EdatechConstants.MessengersTable.PACKAGE, "org.telegram.messenger.web");
        values.put(EdatechConstants.MessengersTable.TITLE, "Telegram");
        insert(EdatechConstants.MessengersTable.CONTENT_URI, values);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor c = null;
        String tableName = "";
        Uri tableUri = null;

        switch (uriMatcher.match(uri)) {
            case EdatechConstants.TABLE_MESSENGERS_ITEM:
                tableName = EdatechConstants.MessengersTable.TABLE_NAME;
                tableUri = EdatechConstants.MessengersTable.CONTENT_URI;
                String _idStr = uri.getLastPathSegment();

                if (!TextUtils.isEmpty(selection)) {
                    selection += selection += " AND ";
                } else {
                    selection = "";
                }

                selection += EdatechConstants.MessengersTable._ID + " = " + _idStr;
                break;

            case EdatechConstants.TABLE_MESSENGERS_DATA:
                tableName = EdatechConstants.MessengersTable.TABLE_NAME;
                tableUri = EdatechConstants.MessengersTable.CONTENT_URI;

                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = EdatechConstants.MessengersTable.PACKAGE + " ASC";
                }
                break;
        }

        mDb = mDbHelper.getWritableDatabase();
        c = mDb.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), tableUri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if (DEBUG) {
            Log.d(TAG, "update, " + uri.toString());
        }

        String tableName = "";

        switch (uriMatcher.match(uri)) {
            case EdatechConstants.TABLE_MESSENGERS_ITEM:
                tableName = EdatechConstants.MessengersTable.TABLE_NAME;
                String _idStr = uri.getLastPathSegment();

                if (!TextUtils.isEmpty(selection)) {
                    selection += selection += " AND ";
                } else {
                    selection = "";
                }

                selection += EdatechConstants.MessengersTable._ID + " = " + _idStr;
                break;

            case EdatechConstants.TABLE_MESSENGERS_DATA:
                tableName = EdatechConstants.MessengersTable.TABLE_NAME;
                break;
        }

        if (DEBUG) {
            Log.d(TAG, "selection " + selection);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = db.update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
