/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: July 5, 2017
 * Reference:
 *      https://github.com/udacity/Android_Me
 *      https://stackoverflow.com/questions/13620711/how-to-pass-data-from-activity-to-fragment
 *      https://stackoverflow.com/questions/32604552/onattach-not-called-in-fragment
 */

package com.udacity.jun.bakingrecipes.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.jun.bakingrecipes.R;
import com.udacity.jun.bakingrecipes.adapters.IngredientsRecyclerViewAdapter;
import com.udacity.jun.bakingrecipes.adapters.StepsRecyclerViewAdapter;
import com.udacity.jun.bakingrecipes.models.RecipeIngredient;
import com.udacity.jun.bakingrecipes.models.RecipeItem;
import com.udacity.jun.bakingrecipes.models.RecipeStep;

import java.util.ArrayList;

// This fragment displays all of the recipe cards on 1 grid list
public class MasterListFragment extends Fragment implements
        StepsRecyclerViewAdapter.RecyclerViewAdapterOnClickHandler{
    private ArrayList<RecipeIngredient> mIngredients;
    private ArrayList<RecipeStep> mSteps;

    // Define a new interface DataPassingListener that triggers a callback in the host activity
    private DataPassingListener mCallback;

    // DataPassingListener interface, calls a method in the host activity named onStepSelected
    public interface DataPassingListener {
        RecipeItem getRecipeDataFromActivity();
        void onStepSelected(int id);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the host activity has implemented the callback interface
        try {
            mCallback = (DataPassingListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    "interface methods");
        }
        //parse data
        try {
            mIngredients = mCallback.getRecipeDataFromActivity().getIngredients();
            mSteps = mCallback.getRecipeDataFromActivity().getSteps();
        } catch (NullPointerException e){
            return;
        }
    }

    // Mandatory empty constructor
    public MasterListFragment() {
    }

    // Inflates the RecyclerView of ingredients and steps
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_master_list,
                container, false);

        // Get a reference to the RecyclerView in the fragment_master_list xml layout file
        RecyclerView mIngredientsRecyclerView = (RecyclerView)
                rootView.findViewById(R.id.detail_ingredients_view);
        RecyclerView mStepsRecyclerView = (RecyclerView) rootView.findViewById(R.id.detail_steps_view);

        LinearLayoutManager ingredientsLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager stepsLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mIngredientsRecyclerView.setLayoutManager(ingredientsLayoutManager);
        mStepsRecyclerView.setLayoutManager(stepsLayoutManager);
        mIngredientsRecyclerView.setHasFixedSize(true);
        mStepsRecyclerView.setHasFixedSize(true);
        mIngredientsRecyclerView.setFocusable(false);
        mStepsRecyclerView.setFocusable(false);

        TextView ingredientsTitle = (TextView) rootView.
                findViewById(R.id.detail_ingredients_title);
        TextView stepsTitle = (TextView) rootView.findViewById(R.id.detail_steps_title);
        ingredientsTitle.setText(getString(R.string.ingredients_title) + " (" +
                mIngredients.size() + " items)");
        stepsTitle.setText(getString(R.string.steps_title) + " (" + (mSteps.size() - 1)
                + " steps)");

        if (mIngredients != null && mSteps != null) {
            // Create the adapter and attach to the RecyclerView
            IngredientsRecyclerViewAdapter mIngredientsAdapter =
                    new IngredientsRecyclerViewAdapter(getActivity(), mIngredients);
            mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
            StepsRecyclerViewAdapter mStepsAdapter =
                    new StepsRecyclerViewAdapter(getActivity(), mSteps, MasterListFragment.this);
            mStepsRecyclerView.setAdapter(mStepsAdapter);
        }

        // Return the root view
        return rootView;
    }

    //This method is for responding to clicks from steps list.
    @Override
    public void onClick(RecipeStep step) {
        mCallback.onStepSelected(step.getId());
    }
}