/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: July 5, 2017
 * Reference:
 *      https://github.com/udacity/Android_Me
 *      https://classroom.udacity.com/nanodegrees/nd801/parts/443745fb-4ae4-4918-8ea1-6bf24e356c1d
 *      https://stackoverflow.com/questions/38127101/how-to-add-cardview-inside-of-gridview
 *      https://github.com/treehouse/android-navigation-drawer-final
 *      https://developer.android.com/training/implementing-navigation/nav-drawer.html
 *      http://www.developerphil.com/parcelable-vs-serializable/
 *      http://www.tutecentral.com/android-custom-navigation-drawer/
 *      https://www.raywenderlich.com/126528/android-recyclerview-tutorial
 */

package com.udacity.jun.bakingrecipes;

import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.Toast;

//import com.udacity.jun.bakingrecipes.adapters.MainGridAdapter;
import com.udacity.jun.bakingrecipes.adapters.MainRecyclerViewAdapter;
import com.udacity.jun.bakingrecipes.async.RecipesListAsyncTaskLoader;
import com.udacity.jun.bakingrecipes.models.RecipeItem;

import java.io.Serializable;
import java.util.ArrayList;

// This activity is responsible for displaying the grid of the recipes cards
public class MainActivity extends AppCompatActivity implements
        MainRecyclerViewAdapter.RecyclerViewAdapterOnClickHandler{
//    private ArrayList<String> mRecipesNameList = new ArrayList<>();
    private ArrayList<RecipeItem> mRecipesItemList = new ArrayList<>();
//    private GridView mGridView;
    private RecyclerView mGridRecyclerView;
    private GridLayoutManager mGridLayoutManager;

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private static final int ASYNCTASK_LOADER_ID = 0;
    //asyncloader for loading the json file
    private final LoaderManager.LoaderCallbacks<ArrayList<RecipeItem>> asyncTaskLoaderListener
            = new LoaderManager.LoaderCallbacks<ArrayList<RecipeItem>>() {
        //Override the required methods in LoaderCallbacks
        @Override
        public Loader<ArrayList<RecipeItem>> onCreateLoader(
                int id, @NonNull final Bundle loaderArgs) {
            return new RecipesListAsyncTaskLoader(MainActivity.this);
        }
        //Called when a previously created loader has finished its load.
        @Override
        public void onLoadFinished(Loader<ArrayList<RecipeItem>> loader,
                                   ArrayList<RecipeItem> recipeResult) {
            //check if the passing result has content or not
            if (recipeResult != null) {
//                for (RecipeItem item : recipeResult) {
//                    mRecipesNameList.add(item.getName());
//                }
//                MainGridAdapter gridAdapter = new MainGridAdapter(MainActivity.this,
//                        mRecipesNameList);
                MainRecyclerViewAdapter mainRecyclerViewAdapter = new MainRecyclerViewAdapter(
                        MainActivity.this, recipeResult, MainActivity.this);
//                mGridView.setAdapter(gridAdapter);
                mGridRecyclerView.setAdapter(mainRecyclerViewAdapter);
                // set onItemClickListener after the grid view is populated
//                mGridView.setOnItemClickListener(new RecipeItemOnClick(recipeResult));
            } else {
//                mRecipesNameList = null;
//                Toast.makeText(MainActivity.this, getString(R.string.toast_msg_no_result),
//                        Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar.make(mGridRecyclerView, getString(R.string.toast_msg_no_result),
                        Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
        //onLoaderReset removes any references this activity had to the loader's data.
        @Override
        public void onLoaderReset(Loader<ArrayList<RecipeItem>> loader) {
        }
    };

    // create a main layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.app_name));
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        //navigation left drawer
        addDrawerItems();
        setupDrawer();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        //Recylerview
        //        mGridView = (GridView) findViewById(R.id.main_grid_view);
        mGridRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerView);
        // Determine if you're creating a tablet, mobile-portrait, or mobile-landscpae view
        boolean isLandscpeView = getResources().getBoolean(R.bool.isLandscape);
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if(isLandscpeView) {
            mGridLayoutManager = new GridLayoutManager(this, 2);
        } else if (isTablet) {
            mGridLayoutManager = new GridLayoutManager(this, 3);
        } else {
            mGridLayoutManager = new GridLayoutManager(this, 1);
        }
        mGridRecyclerView.setLayoutManager(mGridLayoutManager);
        mGridRecyclerView.setFocusable(false);

        // check the device connectivity
        if (isOnline()){
            //start loading the recipes date from the internet
            LoaderManager loaderManager = this.getSupportLoaderManager();
            Loader<ArrayList<RecipeItem>> asyncTaskLoader =
                    loaderManager.getLoader(ASYNCTASK_LOADER_ID);
            if (asyncTaskLoader == null) {
                loaderManager.initLoader(ASYNCTASK_LOADER_ID, null, asyncTaskLoaderListener);
            } else {
                loaderManager.restartLoader(ASYNCTASK_LOADER_ID, null, asyncTaskLoaderListener);
            }
        }else {
            setContentView(R.layout.activity_no_internet);
            TextView noInternet = (TextView) findViewById(R.id.no_internet);
            noInternet.setText(getString(R.string.no_internet_connection));
        }
    }

    //This method is to check the device is currently online or not
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

//    //This inner class is to handle the click event on the recipe card
//    public class RecipeItemOnClick implements AdapterView.OnItemClickListener{
//        private final ArrayList<RecipeItem> list;
//
//        public RecipeItemOnClick(ArrayList<RecipeItem> itemLists) {
//            this.list = itemLists;
//        }
//
//        // After detect a click on the grid view, the app triggers next activity
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            RecipeItem singleRecipe = list.get(position);
//            // Start the child activity
//            Context context = MainActivity.this;
//            Class destinationClass = DetailActivity.class;
//            Intent intentToStartDetailActivity = new Intent(context, destinationClass);
//            intentToStartDetailActivity.setAction(Intent.ACTION_SEND);
//            //put the serializable object into an intent
//            intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, (Serializable) singleRecipe);
//            startActivity(intentToStartDetailActivity);
//        }
//    }

    //handle the click event on the recipe card
    @Override
    public void RecipeItemOnClick(RecipeItem item){
        // Start the child activity
        Context context = MainActivity.this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.setAction(Intent.ACTION_SEND);
        //put the serializable object into an intent
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, (Serializable) item);
        startActivity(intentToStartDetailActivity);
    }

    private void addDrawerItems() {
        String[] mNavigationItems = getResources().getStringArray(R.array.drawer_items);
        // Set the adapter for the list view
        // must use android OWN resource layout android.R.layout.simple_list_item_1
        ArrayAdapter<String> mDrawerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                mNavigationItems);
        mDrawerList.setAdapter(mDrawerAdapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {
            //Called when a drawer has settled in a completely open state
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            //Called when a drawer has settled in a completely closed state
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Activate the navigation drawer toggle
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    private void selectItem(int position) {
        if (position == 0) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.about_dialog);
            Button btnOk = (Button) dialog.findViewById(R.id.ok_button);
            dialog.show();
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    mDrawerLayout.closeDrawers();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //send intent to widgetProvider in order to update the widget using widgetProvider
        // onUpdate() method
        Intent intent = new Intent(this, RecipesWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use a id array and AppWidgetManager.EXTRA_APPWIDGET_IDS instead of
        // AppWidgetManager.EXTRA_APPWIDGET_ID,since it seems the onUpdate() is only fired on that
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                RecipesWidgetProvider.class));
        Bundle extrasWidgetData = new Bundle();
        extrasWidgetData.putIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        intent.putExtras(extrasWidgetData);
        sendBroadcast(intent);
    }
}

