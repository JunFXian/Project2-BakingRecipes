/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: July 26, 2017
 * Reference:
 */

package com.udacity.jun.bakingrecipes.async;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
//import android.util.Log;

import com.udacity.jun.bakingrecipes.models.RecipeItem;
import com.udacity.jun.bakingrecipes.utilities.RecipesApi;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;

public class RecipesListAsyncTaskLoader extends AsyncTaskLoader<ArrayList<RecipeItem>> {
    private ArrayList<RecipeItem> mRecipesList = null;

    // constructor for the AsyncTask class
    public RecipesListAsyncTaskLoader(Context contextInfo)
    {
        super(contextInfo);
    }

    // onStartLoading() is called when a loader first starts loading data
    @Override
    protected void onStartLoading() {
        if (mRecipesList != null) {
            // Delivers any previously loaded data immediately
            deliverResult(mRecipesList);
        } else {
            // Force a new load
            forceLoad();
        }
    }

    // loadInBackground() performs asynchronous loading of data
    @Override
    public ArrayList<RecipeItem> loadInBackground() {
        ArrayList<RecipeItem> result = null;
        try {
            RecipesApi list = RecipesApi.retrofit.create(RecipesApi.class);
            Call<ArrayList<RecipeItem>> call = list.getRecipesList();
            result = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result != null) {
            for (RecipeItem single : result) {
                single.reorderStepId(single.getSteps());
//                for (RecipeStep step : single.getSteps()) {
//                    Log.d("STEPORDER", String.valueOf(step.getId()));
//                }
            }
            return result;
        } else {
            return null;
        }
    }

    // deliverResult sends the result of the load to the registered listener
    @Override
    public void deliverResult(ArrayList<RecipeItem> data) {
        mRecipesList = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}