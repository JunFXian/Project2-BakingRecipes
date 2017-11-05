/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: July 26, 2017
 * Reference:
 */

package com.udacity.jun.bakingrecipes.utilities;

import com.udacity.jun.bakingrecipes.models.RecipeItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface RecipesApi {
    String BASE_URL = "http://go.udacity.com";
    String SORT_URL = "/android-baking-app-json";

    @GET(SORT_URL)
    Call<ArrayList<RecipeItem>> getRecipesList();

    //To use the interface during runtime, need to build a Retrofit object
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
