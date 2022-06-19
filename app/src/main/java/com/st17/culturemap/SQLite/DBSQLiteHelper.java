package com.st17.culturemap.SQLite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBSQLiteHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "userinfo";
    public static final int DB_VERSION = 1; // версия базы данных
    public static final String TABLE_FAVOURIRE = "favourite"; // название таблицы в бд
    // названия столбцов
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DESCRIPTION_SHORT = "description_short";
    public static final String KEY_IMAGE_URL = "image_url";
    public static final String KEY_TYPE = "type";

    public DBSQLiteHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_FAVOURIRE + "(" + KEY_ID + " integer primary key,"
                + KEY_NAME + " text," + KEY_TYPE + " text,"
                + KEY_DESCRIPTION_SHORT + " text," + KEY_DESCRIPTION
                + " text," + KEY_IMAGE_URL + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
