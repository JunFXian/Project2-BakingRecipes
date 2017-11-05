/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: Aug 2, 2017
 * Reference:
 *      https://github.com/udacity/AdvancedAndroid_MyGarden/tree/TWID.05-Solution-GridView
 *      https://github.com/commonsguy/cw-advandroid/tree/master/AppWidget/LoremWidget
 */

package com.udacity.jun.bakingrecipes.utilities;

//import android.appwidget.AppWidgetManager;
import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
import android.widget.RemoteViewsService;

public class ListWidgetService extends RemoteViewsService {
//    private static final String TAG = "MYWIDGET";
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
//        Log.d(TAG, "start service");
//        Bundle extrasWidgetData = intent.getExtras();
//        int appWidgetId = extrasWidgetData.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
//                AppWidgetManager.INVALID_APPWIDGET_ID);
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}