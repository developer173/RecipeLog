package com.example.recipelog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button buttonAddRecipe;
    private Button buttonRecipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAddRecipe = findViewById(R.id.buttonAddRecipe);
        buttonRecipeList = findViewById(R.id.buttonRecipeList);

        buttonAddRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipeAddActivity.class);
            startActivity(intent);
        });

        buttonRecipeList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipeListActivity.class);
            startActivity(intent);
        });
    }
}