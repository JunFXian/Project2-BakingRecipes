/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: July 25, 2017
 * Reference:
 *      https://github.com/udacity/Android_Me
 *      https://stackoverflow.com/questions/5049852/android-drawing-separator-divider-line-in-layout/14533085#14533085
 */

package com.udacity.jun.bakingrecipes;

import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.udacity.jun.bakingrecipes.fragments.MasterListFragment;
import com.udacity.jun.bakingrecipes.fragments.StepDetailFragment;
import com.udacity.jun.bakingrecipes.models.RecipeItem;

import java.io.Serializable;

// This activity is responsible for displaying the list of the recipe details
public class DetailActivity extends AppCompatActivity
        implements MasterListFragment.DataPassingListener,
        StepDetailFragment.DataPassingFromDetailListener{
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private RecipeItem mSelectedRecipe;
    // Variables to track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean mTwoPane = false;
    private int mCurrentStepIndex = 0;

    //step details variables
    private StepDetailFragment mStepFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get data from the intent
        Intent intentFromMainActivity = getIntent();
        // check if the intent and its data can pass correctly
        if (intentFromMainActivity != null) {
            if (intentFromMainActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mSelectedRecipe = (RecipeItem)
                        intentFromMainActivity.getSerializableExtra(Intent.EXTRA_TEXT);
                String singleRecipeName = mSelectedRecipe.getName();
                setTitle(singleRecipeName);
                //get data from parent activity before inflate the layout
                setContentView(R.layout.activity_detail);

                // Determine if you're creating a two-pane or single-pane display
                boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
                if(tabletSize) {
                    // This Constraintlayout will only initially exist in the two-pane tablet case
                    mTwoPane = true;

                    if (mSelectedRecipe != null) {
                        // Create a new step detail fragment
//                        mStepFragment = new StepDetailFragment();
                        mStepFragment = StepDetailFragment.newInstance();
                        //transact the next fragment on the same runtime activity
                        // Only create new fragments when there is no previously saved state
                        if(savedInstanceState == null) {
                            //update the step index
                            mCurrentStepIndex = 0;
                        } else {
                            mCurrentStepIndex = savedInstanceState.
                                    getInt(getString(R.string.bundle_key_step_id));
                        }
                        //pass data to step detail fragment object
                        mStepFragment.setStep(mSelectedRecipe.getSteps().get(mCurrentStepIndex));
                        mStepFragment.setStepsCount(mSelectedRecipe.getSteps().size());
                        // replace the fragment to its container using a FragmentManager and a Transaction
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.step_detail_container_tablet, mStepFragment)
                                .commit();
                    }
                } else {
                    mTwoPane = false;
                }

                //navigation drawer
                mDrawerList = (ListView) findViewById(R.id.detail_left_drawer);
                mDrawerLayout = (DrawerLayout)findViewById(R.id.detail_drawer_layout);

                addDrawerItems();
                setupDrawer();
                if (getSupportActionBar() != null) {
                    //detail activity style theme must has action bar (top bar) enabled
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);
                }
            } else {
                setContentView(R.layout.activity_no_internet);
            }
        }
    }

    // Save and restore some data when the whole lifecycle of activity runs again due to screen size,
    // orientation change
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mTwoPane) {
            outState.putInt(getString(R.string.bundle_key_step_id), mCurrentStepIndex);
        }
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mTwoPane) {
            if (savedInstanceState != null) {
                mCurrentStepIndex = savedInstanceState.
                        getInt(getString(R.string.bundle_key_step_id));
            }
        }
    }

    // Define the behavior for getRecipeDataFromActivity()
    public RecipeItem getRecipeDataFromActivity() {
        return mSelectedRecipe;
    }

    // Define the behavior for onStepSelected
    public void onStepSelected(int selectedId) {
        if (mSelectedRecipe != null) {
            RecipeItem singleSelectedItem = mSelectedRecipe;

            if (mTwoPane) {
                // Create a new step detail fragment
//                mStepFragment = new StepDetailFragment();
                mStepFragment = StepDetailFragment.newInstance();
                //transact the next fragment on the same runtime activity
                //pass data to step detail fragment object
                mStepFragment.setStep(singleSelectedItem.getSteps().get(selectedId));
                mStepFragment.setStepsCount(singleSelectedItem.getSteps().size());
                // replace the fragment to its container using a FragmentManager and a Transaction
                mFragmentManager = getSupportFragmentManager();
                mFragmentManager.beginTransaction()
                        .replace(R.id.step_detail_container_tablet, mStepFragment)
                        .commit();

                //update the step index
                mCurrentStepIndex = selectedId;
            } else {
                Bundle extrasData = new Bundle();
                //put the serializable object into a bundle
                extrasData.putSerializable(getString(R.string.bundle_key_selected_recipe),
                        (Serializable) singleSelectedItem);
                //put the id into a bundle
                extrasData.putInt(getString(R.string.bundle_key_step_id), selectedId);

                // Start the child activity
                Context context = DetailActivity.this;
                Class destinationClass = StepDetailActivity.class;
                Intent intentToStartStepDetailActivity = new Intent(context, destinationClass);
                //put the bundle object into an intent using putExtras
                intentToStartStepDetailActivity.putExtras(extrasData);
                startActivity(intentToStartStepDetailActivity);
            }
        }
    }

    // Define the behavior for onNavigationButtonClick
    public void onNavigationButtonClick(int goToId) {
        //do nothing
    }

    private void addDrawerItems() {
        String[] mNavigationItems = getResources().getStringArray(R.array.detail_drawer_items);
        // Set the adapter for the list view
        // must use android OWN resource layout android.R.layout.simple_list_item_1
        ArrayAdapter<String> mDrawerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mNavigationItems);
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
        if (mDrawerToggle != null) {
            // Sync the toggle state after onRestoreInstanceState has occurred.
            mDrawerToggle.syncState();
        }
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
        switch (position) {
            case 0:
                // Start the main activity
                Context context = DetailActivity.this;
                Class destinationClass = MainActivity.class;
                Intent intentToRestartMainActivity = new Intent(context, destinationClass);
                startActivity(intentToRestartMainActivity);
                mDrawerLayout.closeDrawers();
                break;
            case 1:
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
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                RecipesWidgetProvider.class));
        //Trigger data update to handle the ListView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredients_view);
        if (mSelectedRecipe != null) {
            //Now update all widgets
//        RecipesWidgetProvider.updateWidgets(this, appWidgetManager, appWidgetIds, mSelectedRecipe);
            Bundle extrasWidgetData = new Bundle();
            extrasWidgetData.putSerializable(getString(R.string.bundle_key_selected_recipe),
                    mSelectedRecipe);
            extrasWidgetData.putIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            Intent intent = new Intent(this, RecipesWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            // Use a id array and AppWidgetManager.EXTRA_APPWIDGET_IDS instead of
            // AppWidgetManager.EXTRA_APPWIDGET_ID,since it seems the onUpdate() is only fired on that
            intent.putExtras(extrasWidgetData);
            sendBroadcast(intent);
        }
    }
}

