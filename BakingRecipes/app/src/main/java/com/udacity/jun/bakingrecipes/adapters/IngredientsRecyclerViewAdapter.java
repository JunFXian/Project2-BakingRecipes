/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: July 27, 2017
 * Reference:
 */

package com.udacity.jun.bakingrecipes.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.jun.bakingrecipes.R;
import com.udacity.jun.bakingrecipes.models.RecipeIngredient;

import java.util.ArrayList;

public class IngredientsRecyclerViewAdapter extends
        RecyclerView.Adapter<IngredientsRecyclerViewAdapter.IngredientsViewHolder> {
    // The context is used to utility methods, app resources and layout inflaters
    private final Activity mActivity;
    private final ArrayList<RecipeIngredient> mIngredientsList;

    //Constructor to creates an Adapter
    public IngredientsRecyclerViewAdapter(@NonNull Activity activity,
                                          ArrayList<RecipeIngredient> list) {
        mActivity = activity;
        mIngredientsList = list;
    }

    // A ViewHolder is a required part of the pattern for RecyclerViews.
    class IngredientsViewHolder extends RecyclerView.ViewHolder {
        final TextView ingredientName;
        final TextView ingredientQuantity;

        IngredientsViewHolder(View view) {
            super(view);
            ingredientName = (TextView) view.findViewById(R.id.ingredient_name);
            ingredientQuantity = (TextView) view.findViewById(R.id.ingredient_quantity);
        }
    }

    //This gets called when each new ViewHolder is created.
    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.recipe_ingredient_list,
                viewGroup, false);
        view.setFocusable(true);
        return new IngredientsViewHolder(view);
    }

    //OnBindViewHolder is called by the RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(IngredientsViewHolder viewHolder, int position) {
        //ingredient name
        String name = String.valueOf(position + 1) + ". " +
                mIngredientsList.get(position).getIngredient() + " :   ";
        viewHolder.ingredientName.setText(name);
        //ingredient quantity
        String quantity = mIngredientsList.get(position).getQuantity() + " " +
                mIngredientsList.get(position).getMeasure();
        viewHolder.ingredientQuantity.setText(quantity);
    }

    //This method simply returns the number of items to display
    @Override
    public int getItemCount() {
        return mIngredientsList.size();
    }
}