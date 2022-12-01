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
    private static final String DATABASE_NAME = "edatech.db";
    private static final int DATABASE_VERSION = 1;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(EdatechConstants.AUTHORITY, EdatechConstants.MessengersTable.PATH_TABLE,
                EdatechConstants.PATH_MESSENGERS_DATA);
        uriMatcher.addURI(EdatechConstants.AUTHORITY, EdatechConstants.MessengersTable.PATH_ITEM,
                EdatechConstants.PATH_MESSENGERS_ITEM);

        uriMatcher.addURI(EdatechConstants.AUTHORITY, EdatechConstants.TextViewFieldsTable.PATH_TABLE,
                EdatechConstants.PATH_TEXT_VIEW_FIELDS_DATA);
        uriMatcher.addURI(EdatechConstants.AUTHORITY, EdatechConstants.TextViewFieldsTable.PATH_ITEM,
                EdatechConstants.PATH_TEXT_VIEW_FIELDS_ITEM);

        uriMatcher.addURI(EdatechConstants.AUTHORITY, EdatechConstants.TextViewTypeTable.PATH_TABLE,
                EdatechConstants.PATH_TEXT_VIEW_TYPE_DATA);
        uriMatcher.addURI(EdatechConstants.AUTHORITY, EdatechConstants.TextViewTypeTable.PATH_ITEM,
                EdatechConstants.PATH_TEXT_VIEW_TYPE_ITEM);

        uriMatcher.addURI(EdatechConstants.AUTHORITY, EdatechConstants.TextMessageTypeTable.PATH_TABLE,
                EdatechConstants.PATH_TEXT_MESSAGE_TYPE_DATA);
        uriMatcher.addURI(EdatechConstants.AUTHORITY, EdatechConstants.TextMessageTypeTable.PATH_ITEM,
                EdatechConstants.PATH_TEXT_MESSAGE_TYPE_ITEM);

        uriMatcher.addURI(EdatechConstants.AUTHORITY, EdatechConstants.TextMessagesTable.PATH_TABLE,
                EdatechConstants.PATH_TEXT_MESSAGES_DATA);
        uriMatcher.addURI(EdatechConstants.AUTHORITY, EdatechConstants.TextMessagesTable.PATH_ITEM,
                EdatechConstants.PATH_TEXT_MESSAGES_ITEM);

        uriMatcher.addURI(EdatechConstants.AUTHORITY, EdatechConstants.TextMessageSenderTable.PATH_TABLE,
                EdatechConstants.PATH_TEXT_MESSAGES_SENDER_DATA);
        uriMatcher.addURI(EdatechConstants.AUTHORITY, EdatechConstants.TextMessageSenderTable.PATH_ITEM,
                EdatechConstants.PATH_TEXT_MESSAGES_SENDER_ITEM);
    }

    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            executeSql(db, EdatechConstants.MessengersTable.CREATE_SQL);
            executeSql(db, EdatechConstants.TextViewTypeTable.CREATE_SQL);
            executeSql(db, EdatechConstants.TextViewFieldsTable.CREATE_SQL);
            executeSql(db, EdatechConstants.TextMessageTypeTable.CREATE_SQL);
            executeSql(db, EdatechConstants.TextMessagesTable.CREATE_SQL);
            executeSql(db, EdatechConstants.TextMessageSenderTable.CREATE_SQL);

            fillTextViewTypeTable(db);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

        public void executeSql(SQLiteDatabase db, String query) {
            if (DEBUG) {
                Log.d(TAG, "execute sql '" + query + "'");
            }

            try {
                db.execSQL(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void fillTextViewTypeTable(SQLiteDatabase db) {
        mDb = db; //early set db
        ContentValues values = new ContentValues();
        values.put(EdatechConstants.TextViewTypeTable.TYPE,
                EdatechConstants.TextViewTypeTable.TYPE_SENDER_ID);
        values.put(EdatechConstants.TextViewTypeTable.TITLE, "sender id");
        insert(EdatechConstants.TextViewTypeTable.CONTENT_URI, values);

        values.put(EdatechConstants.TextViewTypeTable.TYPE,
                EdatechConstants.TextViewTypeTable.TYPE_OUTGOING_MESSAGE);
        values.put(EdatechConstants.TextViewTypeTable.TITLE, "outgoing message");
        insert(EdatechConstants.TextViewTypeTable.CONTENT_URI, values);

        values.put(EdatechConstants.TextViewTypeTable.TYPE,
                EdatechConstants.TextViewTypeTable.TYPE_INCOMING_MESSAGE);
        values.put(EdatechConstants.TextViewTypeTable.TITLE, "incoming message");
        insert(EdatechConstants.TextViewTypeTable.CONTENT_URI, values);

        values.put(EdatechConstants.TextViewTypeTable.TYPE,
                EdatechConstants.TextViewTypeTable.TYPE_INCOMING_MESSAGE_TIMESTAMP);
        values.put(EdatechConstants.TextViewTypeTable.TITLE, "incoming message receive time");
        insert(EdatechConstants.TextViewTypeTable.CONTENT_URI, values);

        values.put(EdatechConstants.TextViewTypeTable.TYPE,
                EdatechConstants.TextViewTypeTable.TYPE_OUTGOING_MESSAGE_TIMESTAMP);
        values.put(EdatechConstants.TextViewTypeTable.TITLE, "outgoing message receive time");
        insert(EdatechConstants.TextViewTypeTable.CONTENT_URI, values);
    }

    private SQLiteDatabase mDb;

    public MessagesContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = "";

        switch (uriMatcher.match(uri)) {
            case EdatechConstants.PATH_MESSENGERS_ITEM:
                tableName = EdatechConstants.MessengersTable.TABLE_NAME;
                String _idStr = uri.getLastPathSegment();

                if (!TextUtils.isEmpty(selection)) {
                    selection += selection += " AND ";
                } else {
                    selection = "";
                }

                selection += EdatechConstants.MessengersTable._ID + " = " + _idStr;
                break;

            case EdatechConstants.PATH_MESSENGERS_DATA:
                tableName = EdatechConstants.MessengersTable.TABLE_NAME;
                break;
        }

        int count = mDb.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /// TODO: Implement this
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case EdatechConstants.PATH_MESSENGERS_DATA:
                return EdatechConstants.MessengersTable.MESSENGERS_CONTENT_TYPE_DATA;
            case EdatechConstants.PATH_MESSENGERS_ITEM:
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
        long _id = -1;
        String tableName = "";
        Uri contentUri = null;

        switch (uriMatcher.match(uri)) {
            case EdatechConstants.PATH_MESSENGERS_ITEM:
                if (!values.containsKey(EdatechConstants.MessengersTable._ID)) {
                    values.put(EdatechConstants.MessengersTable._ID, uri.getLastPathSegment());
                }
            case EdatechConstants.PATH_MESSENGERS_DATA:
                tableName = EdatechConstants.MessengersTable.TABLE_NAME;
                contentUri = EdatechConstants.MessengersTable.CONTENT_URI;
                break;

            case EdatechConstants.PATH_TEXT_VIEW_TYPE_ITEM:
                if (!values.containsKey(EdatechConstants.TextViewTypeTable._ID)) {
                    values.put(EdatechConstants.TextViewTypeTable._ID, uri.getLastPathSegment());
                }

            case EdatechConstants.PATH_TEXT_VIEW_TYPE_DATA:
                tableName = EdatechConstants.TextViewTypeTable.TABLE_NAME;
                contentUri = EdatechConstants.TextViewTypeTable.CONTENT_URI;
                break;

            case EdatechConstants.PATH_TEXT_VIEW_FIELDS_ITEM:
                if (!values.containsKey(EdatechConstants.TextViewFieldsTable._ID)) {
                    values.put(EdatechConstants.TextViewFieldsTable._ID, uri.getLastPathSegment());
                }

            case EdatechConstants.PATH_TEXT_VIEW_FIELDS_DATA:
                tableName = EdatechConstants.TextViewFieldsTable.TABLE_NAME;
                contentUri = EdatechConstants.TextViewFieldsTable.CONTENT_URI;
                break;


            case EdatechConstants.PATH_TEXT_MESSAGE_TYPE_ITEM:
                if (!values.containsKey(EdatechConstants.TextViewFieldsTable._ID)) {
                    values.put(EdatechConstants.TextViewFieldsTable._ID, uri.getLastPathSegment());
                }
                break;

            case EdatechConstants.PATH_TEXT_MESSAGE_TYPE_DATA:
                tableName = EdatechConstants.TextMessageTypeTable.TABLE_NAME;
                contentUri = EdatechConstants.TextMessageTypeTable.CONTENT_URI;
                break;

            case EdatechConstants.PATH_TEXT_MESSAGES_ITEM:
                if (!values.containsKey(EdatechConstants.TextMessagesTable._ID)) {
                    values.put(EdatechConstants.TextMessagesTable._ID, uri.getLastPathSegment());
                }
                break;

            case EdatechConstants.PATH_TEXT_MESSAGES_DATA:
                tableName = EdatechConstants.TextMessagesTable.TABLE_NAME;
                contentUri = EdatechConstants.TextMessagesTable.CONTENT_URI;
                break;

            case EdatechConstants.PATH_TEXT_MESSAGES_SENDER_ITEM:
                if (!values.containsKey(EdatechConstants.TextMessageSenderTable._ID)) {
                    values.put(EdatechConstants.TextMessageSenderTable._ID, uri.getLastPathSegment());
                }
                break;

            case EdatechConstants.PATH_TEXT_MESSAGES_SENDER_DATA:
                tableName = EdatechConstants.TextMessageSenderTable.TABLE_NAME;
                contentUri = EdatechConstants.TextMessageSenderTable.CONTENT_URI;
                break;

            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        _id = mDb.insert(tableName, null, values);
        resultUri = ContentUris.withAppendedId(contentUri, _id);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public boolean onCreate() {
        DBHelper helper = new DBHelper(getContext());
        mDb = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor c = null;
        String tableName = "";
        Uri tableUri = null;
        String insertId = "";
        String _idStr = "";

        switch (uriMatcher.match(uri)) {
            case EdatechConstants.PATH_MESSENGERS_ITEM: {
                tableName = EdatechConstants.MessengersTable.TABLE_NAME;
                tableUri = EdatechConstants.MessengersTable.CONTENT_URI;
                _idStr = uri.getLastPathSegment();
                insertId = EdatechConstants.MessengersTable._ID;
            }
            break;

            case EdatechConstants.PATH_MESSENGERS_DATA: {
                tableName = EdatechConstants.MessengersTable.TABLE_NAME;
                tableUri = EdatechConstants.MessengersTable.CONTENT_URI;

                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = EdatechConstants.MessengersTable.PACKAGE + " ASC";
                }
            }
            break;

            case EdatechConstants.PATH_TEXT_VIEW_TYPE_ITEM:
                tableName = EdatechConstants.TextViewTypeTable.TABLE_NAME;
                tableUri = EdatechConstants.TextViewTypeTable.CONTENT_URI;
                _idStr = uri.getLastPathSegment();
                insertId = EdatechConstants.TextViewTypeTable._ID;
                break;

            case EdatechConstants.PATH_TEXT_VIEW_TYPE_DATA:
                tableName = EdatechConstants.TextViewTypeTable.TABLE_NAME;
                tableUri = EdatechConstants.TextViewTypeTable.CONTENT_URI;

                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = EdatechConstants.TextViewTypeTable.TYPE + " ASC";
                }
                break;

            case EdatechConstants.PATH_TEXT_VIEW_FIELDS_ITEM:
                tableName = EdatechConstants.TextViewFieldsTable.TABLE_NAME;
                tableUri = EdatechConstants.TextViewFieldsTable.CONTENT_URI;
                _idStr = uri.getLastPathSegment();
                insertId = EdatechConstants.TextViewFieldsTable._ID;
                break;

            case EdatechConstants.PATH_TEXT_VIEW_FIELDS_DATA:
                tableName = EdatechConstants.TextViewFieldsTable.TABLE_NAME;
                tableUri = EdatechConstants.TextViewFieldsTable.CONTENT_URI;

                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = EdatechConstants.TextViewFieldsTable.MESSENGER_ID + " ASC";
                }
                break;

            case EdatechConstants.PATH_TEXT_MESSAGE_TYPE_ITEM:
                tableName = EdatechConstants.TextMessageTypeTable.TABLE_NAME;
                tableUri = EdatechConstants.TextMessageTypeTable.CONTENT_URI;
                _idStr = uri.getLastPathSegment();
                insertId = EdatechConstants.TextMessageTypeTable._ID;
                break;

            case EdatechConstants.PATH_TEXT_MESSAGE_TYPE_DATA:
                tableName = EdatechConstants.TextMessageTypeTable.TABLE_NAME;
                tableUri = EdatechConstants.TextMessageTypeTable.CONTENT_URI;

                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = EdatechConstants.TextMessageTypeTable._ID + " ASC";
                }
                break;

            case EdatechConstants.PATH_TEXT_MESSAGES_ITEM:
                tableName = EdatechConstants.TextMessagesTable.TABLE_NAME;
                tableUri = EdatechConstants.TextMessagesTable.CONTENT_URI;
                _idStr = uri.getLastPathSegment();
                insertId = EdatechConstants.TextMessagesTable._ID;
                break;

            case EdatechConstants.PATH_TEXT_MESSAGES_DATA:
                tableName = EdatechConstants.TextMessagesTable.TABLE_NAME;
                tableUri = EdatechConstants.TextMessagesTable.CONTENT_URI;

                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = EdatechConstants.TextMessagesTable._ID + " ASC";
                }
                break;

            case EdatechConstants.PATH_TEXT_MESSAGES_SENDER_ITEM:
                tableName = EdatechConstants.TextMessageSenderTable.TABLE_NAME;
                tableUri = EdatechConstants.TextMessageSenderTable.CONTENT_URI;
                _idStr = uri.getLastPathSegment();
                insertId = EdatechConstants.TextMessageSenderTable._ID;
                break;

            case EdatechConstants.PATH_TEXT_MESSAGES_SENDER_DATA:
                tableName = EdatechConstants.TextMessageSenderTable.TABLE_NAME;
                tableUri = EdatechConstants.TextMessageSenderTable.CONTENT_URI;

                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = EdatechConstants.TextMessageSenderTable._ID + " ASC";
                }
                break;
        }

        if (!_idStr.isEmpty() && !insertId.isEmpty()) {
            if (!TextUtils.isEmpty(selection)) {
                selection += selection += " AND ";
            } else {
                selection = "";
            }

            selection += insertId + " = " + _idStr;
        }

        c = mDb.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), tableUri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        /// TODO: Implement this function
        if (DEBUG) {
            Log.d(TAG, "update, " + uri.toString());
        }

        String tableName = "";

        switch (uriMatcher.match(uri)) {
            case EdatechConstants.PATH_MESSENGERS_ITEM:
                tableName = EdatechConstants.MessengersTable.TABLE_NAME;
                String _idStr = uri.getLastPathSegment();

                if (!TextUtils.isEmpty(selection)) {
                    selection += selection += " AND ";
                } else {
                    selection = "";
                }

                selection += EdatechConstants.MessengersTable._ID + " = " + _idStr;
                break;

            case EdatechConstants.PATH_MESSENGERS_DATA:
                tableName = EdatechConstants.MessengersTable.TABLE_NAME;
                break;
        }

        if (DEBUG) {
            Log.d(TAG, "selection " + selection);
        }

        int count = mDb.update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
