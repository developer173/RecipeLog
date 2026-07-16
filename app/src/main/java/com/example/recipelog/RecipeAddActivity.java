package com.example.recipelog;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

    private Button buttonCancelRecipe;
    private Button buttonSaveRecipe;

    private TextView textRecipeFormTitle;
    private RecipeRepository recipeRepository;

    private int recipeId = -1;
    private boolean isEditMode = false;

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

        buttonCancelRecipe = findViewById(R.id.buttonCancelRecipe);
        buttonSaveRecipe = findViewById(R.id.buttonSaveRecipe);
        textRecipeFormTitle = findViewById(R.id.textRecipeFormTitle);

        recipeId = getIntent().getIntExtra("recipe_id", -1);

        if (recipeId != -1) {
            isEditMode = true;
            loadRecipeForEdit();
        }

        buttonCancelRecipe.setOnClickListener(v -> finish());
        buttonSaveRecipe.setOnClickListener(v -> saveRecipe());
    }

    private void loadRecipeForEdit() {
        Recipe recipe = recipeRepository.getById(recipeId);

        if (recipe == null) {
            Toast.makeText(
                    this,
                    "レシピが見つかりません",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        editRecipeName.setText(recipe.getRecipeName());
        editCategory.setText(recipe.getCategory());
        editMainIngredient.setText(recipe.getMainIngredient());
        editRecipeText.setText(recipe.getRecipeText());
        editMemo.setText(recipe.getMemo());

        textRecipeFormTitle.setText("レシピ編集");
        buttonSaveRecipe.setText("更新");
        setTitle("レシピ編集");
    }

    private void saveRecipe() {
        String recipeName =
                editRecipeName.getText().toString().trim();

        String category =
                editCategory.getText().toString().trim();

        String mainIngredient =
                editMainIngredient.getText().toString().trim();

        String recipeText =
                editRecipeText.getText().toString().trim();

        String memo =
                editMemo.getText().toString().trim();

        if (recipeName.isEmpty()) {
            editRecipeName.setError("料理名を入力してください");
            editRecipeName.requestFocus();
            return;
        }

        if (category.isEmpty()) {
            editCategory.setError("料理種別を入力してください");
            editCategory.requestFocus();
            return;
        }

        if (mainIngredient.isEmpty()) {
            editMainIngredient.setError("メイン食材を入力してください");
            editMainIngredient.requestFocus();
            return;
        }

        if (recipeText.isEmpty()) {
            editRecipeText.setError("レシピを入力してください");
            editRecipeText.requestFocus();
            return;
        }

        Recipe recipe = new Recipe();

        recipe.setId(recipeId);
        recipe.setRecipeName(recipeName);
        recipe.setCategory(category);
        recipe.setMainIngredient(mainIngredient);
        recipe.setRecipeText(recipeText);
        recipe.setMemo(memo);

        if (isEditMode) {
            updateRecipe(recipe);
        } else {
            insertRecipe(recipe);
        }
    }

    private void insertRecipe(Recipe recipe) {
        long result = recipeRepository.insert(recipe);

        if (result != -1) {
            Toast.makeText(
                    this,
                    "保存しました",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        } else {
            Toast.makeText(
                    this,
                    "保存に失敗しました",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void updateRecipe(Recipe recipe) {
        int result = recipeRepository.update(recipe);

        if (result > 0) {
            Toast.makeText(
                    this,
                    "更新しました",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        } else {
            Toast.makeText(
                    this,
                    "更新に失敗しました",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}