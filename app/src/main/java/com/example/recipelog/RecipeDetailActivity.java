package com.example.recipelog;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipelog.model.Recipe;
import com.example.recipelog.repository.RecipeRepository;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView textRecipeName;
    private TextView textCategory;
    private TextView textMainIngredient;
    private TextView textRecipeText;
    private TextView textMemo;

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

        int recipeId = getIntent().getIntExtra("recipe_id", -1);

        if (recipeId == -1) {
            Toast.makeText(this, "レシピIDが取得できません", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadRecipe(recipeId);
    }

    private void loadRecipe(int recipeId) {
        Recipe recipe = recipeRepository.getById(recipeId);

        if (recipe == null) {
            Toast.makeText(this, "レシピが見つかりません", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        textRecipeName.setText(recipe.getRecipeName());
        textCategory.setText("料理種別：" + recipe.getCategory());
        textMainIngredient.setText("メイン食材：" + recipe.getMainIngredient());
        textRecipeText.setText(recipe.getRecipeText());
        textMemo.setText(recipe.getMemo());
    }
}