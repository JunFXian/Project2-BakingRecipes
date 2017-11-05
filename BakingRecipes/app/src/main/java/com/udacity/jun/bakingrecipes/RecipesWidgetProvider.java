/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: Aug 2, 2017
 * Reference:
 *      https://github.com/udacity/AdvancedAndroid_MyGarden/tree/TWID.05-Solution-GridView
 *      https://stackoverflow.com/questions/3455123/programmatically-update-widget-from-activity-service-receiver
 *      https://github.com/commonsguy/cw-advandroid/tree/master/AppWidget/LoremWidget
 *      https://stackoverflow.com/questions/14486888/android-appwidget-listview-not-updating
 */

package com.udacity.jun.bakingrecipes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.udacity.jun.bakingrecipes.models.RecipeItem;
import com.udacity.jun.bakingrecipes.utilities.ListWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class RecipesWidgetProvider extends AppWidgetProvider {
//    private static final String TAG = "MYWIDGET";
    private RecipeItem mRecipeItem;
    private String[] mWidgetData;
    private int[] mWidgetIds;
    public static int randomNumber;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] mWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : mWidgetIds) {
            RemoteViews remoteView;
            if (mWidgetData == null) {
                remoteView = getDefaultRemoteView(context, R.drawable.backing_recipes_cooking_icon,
                        context.getString(R.string.widget_goto_home_text));
            } else {
                remoteView = getIngredientsRemoteView(context, appWidgetId, mWidgetData,
                        mRecipeItem);
            }
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteView);

        }
        super.onUpdate(context, appWidgetManager, mWidgetIds);
    }

    //Creates and returns the RemoteViews to be displayed in the default recipes widget
    // (no recipes selected)
    private static RemoteViews getDefaultRemoteView(Context context, int imgRes,
                                                    String widgetText) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.widget_baking_recipes);
        views.setTextViewText(R.id.widget_text, widgetText);
        views.setImageViewResource(R.id.widget_cooking_image, imgRes);

        //create an intent to launch MainActivity when clicked
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // Set the click handler to open the MainActivity
        //widget allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.widget_cooking_image, mainPendingIntent);
        return views;
    }

    //Creates and returns the RemoteViews to be displayed in the recipe ingredients depending on
    // which recipe is selected
    private static RemoteViews getIngredientsRemoteView(Context context, int appWidgetId,
                                                        String[] recipeArray, RecipeItem recipe) {
//        Log.d(TAG, "start inflating ingredients list");
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.widget_recipe_ingredients);
        String widgetIngredientsTitle = recipe.getName() + " ingredients:";
        views.setTextViewText(R.id.widget_ingredients_title, widgetIngredientsTitle);

//        Log.d(TAG, "start serviceIntent");
        Intent serviceIntent = new Intent(context, ListWidgetService.class);
        Bundle extrasWidgetData = new Bundle();
        //RemoteViews cannot handle Serializable format, so change it to string array
//        extrasWidgetData.putStringArray(context.getString(R.string.bundle_key_selected_recipe),
//                (Serializable) recipe);
        extrasWidgetData.putStringArray(context.getString(R.string.bundle_key_recipe_array),
                recipeArray);
        extrasWidgetData.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.putExtras(extrasWidgetData);
        //setting a unique Uri to the intent
        int max = context.getResources().getInteger(R.integer.max_random_range);
        //update a different appWidgetId
        randomNumber = (int) (Math.random()*max);
        serviceIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetId+randomNumber), null));
//        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        // Set the ListWidgetService intent to act as the adapter for the ListView
//        views.setRemoteAdapter(appWidgetId, R.id.widget_ingredients_view, serviceIntent);
        views.setRemoteAdapter(R.id.widget_ingredients_view, serviceIntent);

        //create an intent to launch MainActivity when clicked
//            Intent detailIntent = new Intent(context, DetailActivity.class);
        Intent detailIntent = new Intent(context, MainActivity.class);
//            //put the serializable object into an intent
//            detailIntent.putExtra(Intent.EXTRA_TEXT, (Serializable) recipe);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, detailIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // Set the click handler to open the DetailActivity
        //widget allow click handlers to only launch pending intents
//        views.setOnClickPendingIntent(R.id.widget_ingredients_view, detailPendingIntent);
        views.setPendingIntentTemplate(R.id.widget_ingredients_view, mainPendingIntent);

        //setting an empty view in case of no data
        views.setEmptyView(R.id.widget_ingredients_view, R.id.empty_view);
        return views;
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundleData = intent.getExtras();
        if (bundleData != null) {
            this.mWidgetData = null;
            try {
                this.mRecipeItem = (RecipeItem) bundleData
                        .getSerializable(context.getString(R.string.bundle_key_selected_recipe));
                this.mWidgetData = mRecipeItem.convertIngredientsToStringArray();
            } catch (NullPointerException e){
                //do nothing
            }
            this.mWidgetIds = bundleData.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        }

        super.onReceive(context, intent);
    }
}

