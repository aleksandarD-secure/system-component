package com.edatech.devadmin;

import android.net.Uri;

final class EdatechConstants {
    static public final String SCHEME = "content";
    static public final String AUTHORITY = "com.edatech";
    static public final int DATA_BASE_VERSION = 1;
    static public final int TABLE_MESSENGERS_DATA = 0;
    static public final int TABLE_MESSENGERS_ITEM = 1;

    public final static class MessengersTable {
        static public final String TABLE_NAME = "messengers";
        static public final String _ID = "_ID";
        static public final String PACKAGE = "package";
        static public final String TITLE = "title";
        static public final String CREATE_SQL = "create table if not exists " + TABLE_NAME + " (" +
                _ID + " integer primary key autoincrement, " +
                PACKAGE + " varchar(50), " +
                TITLE + " varchar(50) " +
                ");";
        static public final String PATH_TABLE = "messengers";
        static public final String PATH_ITEM = PATH_TABLE + "/#";
        static public final String MESSENGERS_CONTENT_TYPE_DATA = "vnd.android.cursor.dir/vnd."
                + EdatechConstants.AUTHORITY + PATH_TABLE;
        static public final String MESSENGERS_CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd."
                + EdatechConstants.AUTHORITY + PATH_ITEM;
        static public final
        Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY).appendPath(TABLE_NAME).build();
    }
}
