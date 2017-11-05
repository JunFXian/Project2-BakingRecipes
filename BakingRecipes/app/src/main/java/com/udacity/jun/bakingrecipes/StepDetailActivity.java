/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: July 5, 2017
 * Reference:
 *      https://github.com/udacity/Android_Me
 */

package com.udacity.jun.bakingrecipes;

import android.app.Dialog;
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

import com.udacity.jun.bakingrecipes.fragments.StepDetailFragment;
import com.udacity.jun.bakingrecipes.models.RecipeItem;

import java.io.Serializable;

// This activity is responsible for displaying the the recipe step details
public class StepDetailActivity extends AppCompatActivity implements
        StepDetailFragment.DataPassingFromDetailListener{
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private RecipeItem mSelectedRecipeDetail;
    // Variables to store the values for the list index of the selected step
    private int mSelectedStepIndex;

    private StepDetailFragment mStepFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get data from the intent
        Intent intentFromDetailActivity = getIntent();
        // check if the intent and its data can pass correctly
        if (intentFromDetailActivity != null) {
            Bundle extrasData = intentFromDetailActivity.getExtras();
            if (!extrasData.isEmpty()) {
                mSelectedRecipeDetail = (RecipeItem)
                        extrasData.getSerializable(getString(R.string.bundle_key_selected_recipe));
                mSelectedStepIndex = extrasData.getInt(getString(R.string.bundle_key_step_id));
                String singleRecipeName = mSelectedRecipeDetail.getName();
                setTitle(singleRecipeName);
            }
        }

        //get data from parent activity before inflate the layout
        setContentView(R.layout.activity_step_detail);

        //start inflating the fragment
//        mStepFragment = new StepDetailFragment();
        mStepFragment = StepDetailFragment.newInstance();
        //pass data to step detail fragment object
        mStepFragment.setStepsCount(mSelectedRecipeDetail.getSteps().size());
        if (savedInstanceState != null) {
            mSelectedStepIndex = savedInstanceState.
                    getInt(getString(R.string.bundle_key_step_id));
        }
        mStepFragment.setStep(mSelectedRecipeDetail.getSteps().get(mSelectedStepIndex));
        // Add the fragment to its container using a FragmentManager and a Transaction
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.step_detail_container, mStepFragment)
                .commit();

        //navigation drawer
        mDrawerList = (ListView) findViewById(R.id.step_detail_left_drawer);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.step_detail_drawer_layout);

        addDrawerItems();
        setupDrawer();
        if (getSupportActionBar() != null) {
            //detail activity style theme must has action bar (top bar) enabled
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    // Save and restore some data when the whole lifecycle of activity runs again due to screen size,
    // orientation change
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.bundle_key_step_id), mSelectedStepIndex);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mSelectedStepIndex = savedInstanceState.
                    getInt(getString(R.string.bundle_key_step_id));
        }
    }

    // Define the behavior for onNavigationButtonClick
    public void onNavigationButtonClick(int goToId) {
        RecipeItem singleSelectedItem = mSelectedRecipeDetail;

        // Create a new step detail fragment
//        mStepFragment = new StepDetailFragment();
        mStepFragment = StepDetailFragment.newInstance();
        //transact the next fragment on the same runtime activity
        //pass data to step detail fragment object
        mStepFragment.setStep(singleSelectedItem.getSteps().get(goToId));
        mStepFragment.setStepsCount(singleSelectedItem.getSteps().size());
        // replace the fragment to its container using a FragmentManager and a Transaction
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .replace(R.id.step_detail_container, mStepFragment)
                .commit();
        mSelectedStepIndex = goToId;
    }

    private void addDrawerItems() {
        String[] mNavigationItems = getResources().getStringArray(R.array.detail_step_drawer_items);
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
        switch (position) {
            case 0:
                // Start the main activity
                Context context0 = StepDetailActivity.this;
                Class destinationClass0 = MainActivity.class;
                Intent intentToRestartMainActivity = new Intent(context0, destinationClass0);
                startActivity(intentToRestartMainActivity);
                mDrawerLayout.closeDrawers();
                break;
            case 1:
                // Start the detail activity
                Context context1 = StepDetailActivity.this;
                Class destinationClass1 = DetailActivity.class;
                Intent intentToRestartDetailActivity = new Intent(context1, destinationClass1);
                intentToRestartDetailActivity.putExtra(Intent.EXTRA_TEXT,
                        (Serializable) mSelectedRecipeDetail);
                startActivity(intentToRestartDetailActivity);
                mDrawerLayout.closeDrawers();
                break;
            case 2:
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
}

