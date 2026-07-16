package com.example.recipelog;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipelog.model.Recipe;
import com.example.recipelog.repository.RecipeRepository;
import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView textRecipeName;
    private TextView textCategory;
    private TextView textMainIngredient;
    private TextView textRecipeText;
    private TextView textMemo;
    private Button buttonEditRecipe;
    private Button buttonDeleteRecipe;

    private int recipeId = -1;
    private Recipe currentRecipe;
    private RecipeRepository recipeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipeRepository = new RecipeRepository(this);

        textRecipeName = findViewById(R.id.textRecipeName);
        textCategory = findViewById(R.id.textCategory);
        textMainIngredient = findViewById(R.id.textMainIngredient);
        textRecipeText = findViewById(R.id.textRecipeText);
        textMemo = findViewById(R.id.textMemo);
        buttonEditRecipe = findViewById(R.id.buttonEditRecipe);
        buttonDeleteRecipe = findViewById(R.id.buttonDeleteRecipe);

        recipeId = getIntent().getIntExtra("recipe_id", -1);

        if (recipeId == -1) {
            Toast.makeText(this, "レシピIDが取得できません", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        buttonEditRecipe.setOnClickListener(v -> openEditScreen());

        buttonDeleteRecipe.setOnClickListener(v -> showDeleteConfirmDialog());
        loadRecipe(recipeId);
    }

    private void loadRecipe(int recipeId) {
        currentRecipe = recipeRepository.getById(recipeId);

        if (currentRecipe == null) {
            Toast.makeText(
                    this,
                    "レシピが見つかりません",
                    Toast.LENGTH_SHORT
            ).show();
            finish();
            return;
        }

        textRecipeName.setText(currentRecipe.getRecipeName());
        textCategory.setText(
                "料理種別：" + currentRecipe.getCategory()
        );
        textMainIngredient.setText(
                "メイン食材：" + currentRecipe.getMainIngredient()
        );
        textRecipeText.setText(currentRecipe.getRecipeText());
        textMemo.setText(currentRecipe.getMemo());
    }
    private void openEditScreen() {
        Intent intent = new Intent(
                RecipeDetailActivity.this,
                RecipeAddActivity.class
        );

        intent.putExtra("recipe_id", recipeId);

        startActivity(intent);
    }

    private void showDeleteConfirmDialog() {
        if (currentRecipe == null) {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("削除確認")
                .setMessage(
                        "「"
                                + currentRecipe.getRecipeName()
                                + "」を削除しますか？"
                )
                .setNegativeButton("キャンセル", null)
                .setPositiveButton(
                        "削除",
                        (dialog, which) -> deleteRecipe()
                )
                .show();
    }

    private void deleteRecipe() {
        int result = recipeRepository.deleteById(recipeId);

        if (result > 0) {
            Toast.makeText(
                    this,
                    "削除しました",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        } else {
            Toast.makeText(
                    this,
                    "削除に失敗しました",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (recipeId != -1) {
            loadRecipe(recipeId);
        }
    }
}