/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: July 26, 2017
 * Reference:
 */

package com.udacity.jun.bakingrecipes.models;

import java.io.Serializable;

public class RecipeIngredient implements Serializable {
    private String quantity;
    private String measure;
    private String ingredient;

    public RecipeIngredient(String ingredientQuantity, String ingredientMeasure,
                            String ingredientName) {
        this.quantity = ingredientQuantity;
        this.measure = ingredientMeasure;
        this.ingredient = ingredientName;
    }

    public String getQuantity() {
        return this.quantity;
    }
    public String getMeasure() {
        return this.measure;
    }
    public String getIngredient() {
        return this.ingredient;
    }

    public void setQuantity(String myQuantity) {
        this.quantity = myQuantity;
    }
    public void setMeasure(String myMeasure) {
        this.measure = myMeasure;
    }
    public void setIngredient(String myIngredient) {
        this.ingredient = myIngredient;
    }

    public String convertToString() {
        return this.ingredient + ": "
                + this.quantity + " " + this.measure;
    }
}
