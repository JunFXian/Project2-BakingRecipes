/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: July 25, 2017
 * Reference:
 *      https://stackoverflow.com/questions/30174970/converting-deeply-nested-json-to-java-object-and-vice-versa
 */

package com.udacity.jun.bakingrecipes.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
//import java.util.Map;

public class RecipeItem implements Serializable {
    private int id;
    private String name;
    private ArrayList<RecipeIngredient> ingredients;
    private ArrayList<RecipeStep> steps;
    private int servings;
    private String image;

    public RecipeItem(int recipeId, String recipeName,
                      ArrayList<RecipeIngredient> recipeIngredients,
                      ArrayList<RecipeStep> recipeSteps,
                      int recipeServings, String recipeImage) {
        this.id = recipeId;
        this.name = recipeName;
        this.ingredients = recipeIngredients;
        this.steps = recipeSteps;
        this.servings = recipeServings;
        this.image = recipeImage;
    }

    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public ArrayList<RecipeIngredient> getIngredients() {
        return this.ingredients;
    }
    public ArrayList<RecipeStep> getSteps() {
        return this.steps;
    }
    public int getServings() {
        return this.servings;
    }
    public String getImage() {
        return this.image;
    }

    public void setId(int myId) {
        this.id = myId;
    }
    public void setName(String myName) {
        this.name = myName;
    }
    public void setIngredients(ArrayList<RecipeIngredient> myIngredients) {
        this.ingredients = myIngredients;
    }
    public void setSteps(ArrayList<RecipeStep> mySteps) {
        this.steps = mySteps;
    }
    public void setServings(int myServings) {
        this.servings = myServings;
    }
    public void setImage(String myIamge) {
        this.image = myIamge;
    }

    public String[] convertIngredientsToStringArray(){
        List<String> ingredients = new ArrayList<>();
        int size = this.ingredients.size();
        String[] returnArray = new String[size];
        for (int i = 0; i < size; i++) {
            String singeIngre = this.ingredients.get(i).convertToString();
            ingredients.add(singeIngre);
        }
        returnArray = ingredients.toArray(returnArray);
        return returnArray;
    }

    public void reorderStepId(ArrayList<RecipeStep> steps){
        for (int i = 0; i < steps.size(); i++) {
            steps.get(i).setId(i);
        }
    }
}
