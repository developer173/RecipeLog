package com.example.recipelog.model;

public class Recipe {

    private int id;
    private String recipeName;
    private String category;
    private String mainIngredient;
    private String recipeText;
    private String memo;
    private String createdAt;
    private String updatedAt;

    public Recipe() {
    }

    public Recipe(
            int id,
            String recipeName,
            String category,
            String mainIngredient,
            String recipeText,
            String memo,
            String createdAt,
            String updatedAt
    ) {
        this.id = id;
        this.recipeName = recipeName;
        this.category = category;
        this.mainIngredient = mainIngredient;
        this.recipeText = recipeText;
        this.memo = memo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getCategory() {
        return category;
    }

    public String getMainIngredient() {
        return mainIngredient;
    }

    public String getRecipeText() {
        return recipeText;
    }

    public String getMemo() {
        return memo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setMainIngredient(String mainIngredient) {
        this.mainIngredient = mainIngredient;
    }

    public void setRecipeText(String recipeText) {
        this.recipeText = recipeText;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}