package com.example.recipelog;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipelog.model.Recipe;
import com.example.recipelog.repository.RecipeRepository;

public class RecipeAddActivity extends AppCompatActivity {

    private EditText editRecipeName;
    private EditText editCategory;
    private EditText editMainIngredient;
    private EditText editRecipeText;
    private EditText editMemo;

    private RecipeRepository recipeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add);

        recipeRepository = new RecipeRepository(this);

        editRecipeName = findViewById(R.id.editRecipeName);
        editCategory = findViewById(R.id.editCategory);
        editMainIngredient = findViewById(R.id.editMainIngredient);
        editRecipeText = findViewById(R.id.editRecipeText);
        editMemo = findViewById(R.id.editMemo);

        Button buttonSaveRecipe = findViewById(R.id.buttonSaveRecipe);

        buttonSaveRecipe.setOnClickListener(v -> saveRecipe());
    }

    private void saveRecipe() {
        String recipeName = editRecipeName.getText().toString().trim();
        String category = editCategory.getText().toString().trim();
        String mainIngredient = editMainIngredient.getText().toString().trim();
        String recipeText = editRecipeText.getText().toString().trim();
        String memo = editMemo.getText().toString().trim();

        if (recipeName.isEmpty()) {
            Toast.makeText(this, "料理名を入力してください", Toast.LENGTH_SHORT).show();
            return;
        }

        Recipe recipe = new Recipe();
        recipe.setRecipeName(recipeName);
        recipe.setCategory(category);
        recipe.setMainIngredient(mainIngredient);
        recipe.setRecipeText(recipeText);
        recipe.setMemo(memo);

        long result = recipeRepository.insert(recipe);

        if (result != -1) {
            Toast.makeText(this, "保存しました", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "保存に失敗しました", Toast.LENGTH_SHORT).show();
        }
    }
}