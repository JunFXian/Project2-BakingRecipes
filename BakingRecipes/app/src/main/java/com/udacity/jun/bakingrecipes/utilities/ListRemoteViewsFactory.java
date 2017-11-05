/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: Aug 2, 2017
 * Reference:
 *      https://github.com/udacity/AdvancedAndroid_MyGarden/tree/TWID.05-Solution-GridView
 *      https://github.com/commonsguy/cw-advandroid/tree/master/AppWidget/LoremWidget
 */

package com.udacity.jun.bakingrecipes.utilities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.jun.bakingrecipes.R;
import com.udacity.jun.bakingrecipes.RecipesWidgetProvider;

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
//    private static final String TAG = "MYWIDGET";

    private final String[] mIngredietns;
    private Context mContext = null;
    private final int mAppWidgetId;

    public ListRemoteViewsFactory(Context context, Intent intent) {
//        Log.d(TAG, "Create ListRemoteViewsFactory object");
        Bundle extrasWidgetData = intent.getExtras();
        this.mContext = context;
        this.mIngredietns = extrasWidgetData
                .getStringArray(mContext.getString(R.string.bundle_key_recipe_array));
//        int appWidgetId = extrasWidgetData.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
//                AppWidgetManager.INVALID_APPWIDGET_ID);
        this.mAppWidgetId = Integer.valueOf(intent.getData().getSchemeSpecificPart())
                - RecipesWidgetProvider.randomNumber;
    }

    @Override
    public void onCreate() {
    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return this.mIngredietns.length;
    }

    //This method acts like the onBindViewHolder method in an Adapter
    @Override
    public RemoteViews getViewAt(int position) {
        if (position >= getCount()) {
            return getLoadingView();
        }
//        Log.d(TAG, String.valueOf(position));
        RemoteViews row = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_ingredient_list);
        //ingredient name
        String name = String.valueOf(position + 1) + ". " + mIngredietns[position];
        row.setTextViewText(R.id.widget_ingredient_name, name);

        Intent rowIntent = new Intent();
        rowIntent.putExtra(mContext.getString(R.string.bundle_key_row_name), name);
        row.setOnClickFillInIntent(R.id.widget_ingredient_name, rowIntent);
        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
