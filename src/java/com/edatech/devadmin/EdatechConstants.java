package com.edatech.devadmin;

import android.net.Uri;

public final class EdatechConstants {
    static public final String SCHEME = "content";
    static public final String AUTHORITY = "com.edatech";
    static public final int PATH_MESSENGERS_DATA = 0;
    static public final int PATH_MESSENGERS_ITEM = 1;
    static public final int PATH_TEXT_VIEW_TYPE_DATA = 2;
    static public final int PATH_TEXT_VIEW_TYPE_ITEM = 4;
    static public final int PATH_TEXT_VIEW_FIELDS_DATA = 5;
    static public final int PATH_TEXT_VIEW_FIELDS_ITEM = 6;
    static public final int PATH_TEXT_MESSAGE_TYPE_DATA = 7;
    static public final int PATH_TEXT_MESSAGE_TYPE_ITEM = 8;
    static public final int PATH_TEXT_MESSAGES_DATA = 9;
    static public final int PATH_TEXT_MESSAGES_ITEM = 10;
    static public final int PATH_TEXT_MESSAGES_SENDER_DATA = 11;
    static public final int PATH_TEXT_MESSAGES_SENDER_ITEM = 12;

    public final static class MessengersTable {
        static public final String TABLE_NAME = "messengers";
        static public final String _ID = "_ID";
        static public final String PACKAGE = "package";
        static public final String TITLE = "title";
        static public final String CREATE_SQL = "create table if not exists " + TABLE_NAME + " (" +
                _ID + " integer primary key autoincrement, " +
                PACKAGE + " varchar(50), " +
                TITLE + " varchar(50), " +
                "UNIQUE(" + PACKAGE + ") " +
                ");";
        static public final String PATH_TABLE = TABLE_NAME;
        static public final String PATH_ITEM = PATH_TABLE + "/#";
        static public final String MESSENGERS_CONTENT_TYPE_DATA = "vnd.android.cursor.dir/vnd."
                + EdatechConstants.AUTHORITY + PATH_TABLE;
        static public final String MESSENGERS_CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd."
                + EdatechConstants.AUTHORITY + PATH_ITEM;
        static public final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY).appendPath(TABLE_NAME).build();
    }

    public final static class TextViewTypeTable {
        static public final String TABLE_NAME = "text_view_type";
        static public final String _ID = "_ID";
        static public final String TYPE = "type";
        static public final String TITLE = "title";
        static public final int TYPE_SENDER_ID = 0;
        static public final int TYPE_OUTGOING_MESSAGE = 1;
        static public final int TYPE_INCOMING_MESSAGE = 2;
        static public final int TYPE_INCOMING_MESSAGE_TIMESTAMP = 3;
        static public final int TYPE_OUTGOING_MESSAGE_TIMESTAMP = 4;
        static public final String PATH_TABLE = TABLE_NAME;
        static public final String PATH_ITEM = PATH_TABLE + "/#";
        static public final String CREATE_SQL = "create table if not exists " + TABLE_NAME + " (" +
                _ID + " integer primary key autoincrement, " +
                TYPE + " integer, " +
                TITLE + " varchar(50) " +
                ");";
        static public final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY).appendPath(TABLE_NAME).build();
    }

    public final static class TextViewFieldsTable {
        static public final String TABLE_NAME = "text_view_fields";
        static public final String _ID = "_ID";
        static public final String MESSENGER_ID = "messenger_id";
        static public final String TEXT_VIEW_ID = "text_view_id";
        static public final String TEXT_VIEW_TYPE_ID = "text_view_type_id";
        static public final String PATH_TABLE = TABLE_NAME;
        static public final String PATH_ITEM = PATH_TABLE + "/#";
        static public final String CREATE_SQL = "create table if not exists " + TABLE_NAME + " (" +
                _ID + " integer primary key autoincrement, " +
                MESSENGER_ID + " integer, " +
                TEXT_VIEW_ID + " integer, " +
                TEXT_VIEW_TYPE_ID + " integer, " +
                "UNIQUE(" + MESSENGER_ID + ", " + TEXT_VIEW_ID + ", " + TEXT_VIEW_TYPE_ID + ") " +
                ");";
        static public final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY).appendPath(TABLE_NAME).build();
    }

    public final static class TextMessageTypeTable {
        static public final String TABLE_NAME = "text_message_type";
        static public final String PATH_TABLE = TABLE_NAME;
        static public final String PATH_ITEM = TABLE_NAME + "/#";
        static public final String _ID = "_ID";
        static public final String TEXT_MESSAGE_TYPE = "message_type";
        static public final String TEXT_MESSAGE_TYPE_TITLE = "message_type_title";
        static public final int TYPE_OUTGOING_MESSAGE = 1;
        static public final int TYPE_INCOMING_MESSAGE = 2;
        static public final String CREATE_SQL = "create table if not exists " + TABLE_NAME + " (" +
                _ID + " integer primary key autoincrement, " +
                TEXT_MESSAGE_TYPE + " integer, " +
                TEXT_MESSAGE_TYPE_TITLE + " varchar(50) " +
                ");";
        static public final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY).appendPath(TABLE_NAME).build();
    }

    public final static class TextMessagesTable {
        static public final String TABLE_NAME = "text_messages";
        static public final String _ID = "_ID";
        static public final String MESSENGER_ID = "messenger_id";
        static public final String TEXT_MESSAGE_TYPE = "text_message_type_id";
        static public final String TEXT_MESSAGE_CONTENT = "text_message_content";
        static public final String TEXT_MESSAGE_TIMESTAMP = "timestamp";
        static public final String TEXT_MESSAGE_SENDER_ID = "sender_id";
        static public final String PATH_TABLE = TABLE_NAME;
        static public final String PATH_ITEM = PATH_TABLE + "/#";
        static public final String CREATE_SQL = "create table if not exists " + TABLE_NAME + " (" +
                _ID + " integer primary key autoincrement, " +
                MESSENGER_ID + " integer, " +
                TEXT_MESSAGE_CONTENT + " text, " +
                TEXT_MESSAGE_TYPE + " integer, " +
                TEXT_MESSAGE_SENDER_ID + " integer, " +
                TEXT_MESSAGE_TIMESTAMP + " timestamp default current_timestamp " +
                ");";
        static public final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY).appendPath(TABLE_NAME).build();
    }

    public final static class TextMessageSenderTable {
        static public final String TABLE_NAME = "text_message_sender";
        static public final String _ID = "_ID";
        static public final String MESSENGER_ID = "messenger_id";
        static public final String SENDER_NAME = "name";
        static public final String PATH_TABLE = TABLE_NAME;
        static public final String PATH_ITEM = PATH_TABLE + "/#";
        static public final String CREATE_SQL = "create table if not exists " + TABLE_NAME + " (" +
                _ID + " integer primary key autoincrement, " +
                MESSENGER_ID + " integer, " +
                SENDER_NAME + " varchar(250) " +
                ");";

        static public final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY).appendPath(TABLE_NAME).build();
    }
}
