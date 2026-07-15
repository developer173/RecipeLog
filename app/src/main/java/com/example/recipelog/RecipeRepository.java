package com.example.recipelog.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.recipelog.database.RecipeDbHelper;
import com.example.recipelog.model.Recipe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecipeRepository {

    private final RecipeDbHelper dbHelper;

    public RecipeRepository(Context context) {
        dbHelper = new RecipeDbHelper(context);
    }

    public long insert(Recipe recipe) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String now = getNowText();

        ContentValues values = new ContentValues();
        values.put(RecipeDbHelper.COLUMN_RECIPE_NAME, recipe.getRecipeName());
        values.put(RecipeDbHelper.COLUMN_CATEGORY, recipe.getCategory());
        values.put(RecipeDbHelper.COLUMN_MAIN_INGREDIENT, recipe.getMainIngredient());
        values.put(RecipeDbHelper.COLUMN_RECIPE_TEXT, recipe.getRecipeText());
        values.put(RecipeDbHelper.COLUMN_MEMO, recipe.getMemo());
        values.put(RecipeDbHelper.COLUMN_CREATED_AT, now);
        values.put(RecipeDbHelper.COLUMN_UPDATED_AT, now);

        return db.insert(RecipeDbHelper.TABLE_RECIPES, null, values);
    }

    public List<Recipe> getAll() {
        List<Recipe> recipeList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql =
                "SELECT * FROM " + RecipeDbHelper.TABLE_RECIPES +
                        " ORDER BY " + RecipeDbHelper.COLUMN_ID + " DESC";

        Cursor cursor = db.rawQuery(sql, null);

        try {
            while (cursor.moveToNext()) {
                Recipe recipe = cursorToRecipe(cursor);
                recipeList.add(recipe);
            }
        } finally {
            cursor.close();
        }

        return recipeList;
    }


    public Recipe getById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = RecipeDbHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                RecipeDbHelper.TABLE_RECIPES,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        try {
            if (cursor.moveToFirst()) {
                return cursorToRecipe(cursor);
            }
        } finally {
            cursor.close();
        }

        return null;
    }
    public int update(Recipe recipe) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(
                RecipeDbHelper.COLUMN_RECIPE_NAME,
                recipe.getRecipeName()
        );
        values.put(
                RecipeDbHelper.COLUMN_CATEGORY,
                recipe.getCategory()
        );
        values.put(
                RecipeDbHelper.COLUMN_MAIN_INGREDIENT,
                recipe.getMainIngredient()
        );
        values.put(
                RecipeDbHelper.COLUMN_RECIPE_TEXT,
                recipe.getRecipeText()
        );
        values.put(
                RecipeDbHelper.COLUMN_MEMO,
                recipe.getMemo()
        );
        values.put(
                RecipeDbHelper.COLUMN_UPDATED_AT,
                getNowText()
        );

        String whereClause =
                RecipeDbHelper.COLUMN_ID + " = ?";

        String[] whereArgs = {
                String.valueOf(recipe.getId())
        };

        return db.update(
                RecipeDbHelper.TABLE_RECIPES,
                values,
                whereClause,
                whereArgs
        );
    }

    public int deleteById(int recipeId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause =
                RecipeDbHelper.COLUMN_ID + " = ?";

        String[] whereArgs = {
                String.valueOf(recipeId)
        };

        return db.delete(
                RecipeDbHelper.TABLE_RECIPES,
                whereClause,
                whereArgs
        );
    }

    private Recipe cursorToRecipe(Cursor cursor) {
        Recipe recipe = new Recipe();

        recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow(RecipeDbHelper.COLUMN_ID)));
        recipe.setRecipeName(cursor.getString(cursor.getColumnIndexOrThrow(RecipeDbHelper.COLUMN_RECIPE_NAME)));
        recipe.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(RecipeDbHelper.COLUMN_CATEGORY)));
        recipe.setMainIngredient(cursor.getString(cursor.getColumnIndexOrThrow(RecipeDbHelper.COLUMN_MAIN_INGREDIENT)));
        recipe.setRecipeText(cursor.getString(cursor.getColumnIndexOrThrow(RecipeDbHelper.COLUMN_RECIPE_TEXT)));
        recipe.setMemo(cursor.getString(cursor.getColumnIndexOrThrow(RecipeDbHelper.COLUMN_MEMO)));
        recipe.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(RecipeDbHelper.COLUMN_CREATED_AT)));
        recipe.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(RecipeDbHelper.COLUMN_UPDATED_AT)));

        return recipe;
    }

    private String getNowText() {
        return new SimpleDateFormat(
                "yyyy/MM/dd HH:mm",
                Locale.JAPAN
        ).format(new Date());
    }
}