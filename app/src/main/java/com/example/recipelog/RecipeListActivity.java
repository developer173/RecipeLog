package com.example.recipelog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipelog.model.Recipe;
import com.example.recipelog.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    private ListView listRecipe;
    private RecipeRepository recipeRepository;
    private List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        listRecipe = findViewById(R.id.listRecipe);
        recipeRepository = new RecipeRepository(this);

        loadRecipes();

        listRecipe.setOnItemClickListener((parent, view, position, id) -> {
            Recipe recipe = recipeList.get(position);

            Intent intent = new Intent(
                    RecipeListActivity.this,
                    RecipeDetailActivity.class
            );
            intent.putExtra("recipe_id", recipe.getId());
            startActivity(intent);
        });
    }

    private void loadRecipes() {
        recipeList = recipeRepository.getAll();

        List<String> displayList = new ArrayList<>();

        for (Recipe recipe : recipeList) {
            displayList.add(
                    recipe.getRecipeName()
                            + "\n"
                            + recipe.getCategory()
                            + " / "
                            + recipe.getMainIngredient()
            );
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );

        listRecipe.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadRecipes();
    }
}