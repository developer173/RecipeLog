package com.example.recipelog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "recipe_log.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RECIPES = "recipes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "recipe_name";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_MAIN_INGREDIENT = "main_ingredient";
    public static final String COLUMN_RECIPE_TEXT = "recipe_text";
    public static final String COLUMN_MEMO = "memo";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =
                "CREATE TABLE " + TABLE_RECIPES + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_RECIPE_NAME + " TEXT, " +
                        COLUMN_CATEGORY + " TEXT, " +
                        COLUMN_MAIN_INGREDIENT + " TEXT, " +
                        COLUMN_RECIPE_TEXT + " TEXT, " +
                        COLUMN_MEMO + " TEXT, " +
                        COLUMN_CREATED_AT + " TEXT, " +
                        COLUMN_UPDATED_AT + " TEXT" +
                        ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Ver0.1では単純に作り直しでOK
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }
}