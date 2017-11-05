/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: Aug 4, 2017
 * Reference:
 *      https://spin.atomicobject.com/2016/04/15/espresso-testing-recyclerviews/
 */

package com.udacity.jun.bakingrecipes;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)

public class DetailActivityBasicTest {
    //set the rule so that the tests are starting from main activity
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    // Convenience helper
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    private ViewMatcher leftDrawer = new ViewMatcher(allOf(withId(R.id.detail_left_drawer),
            withParent(withId(R.id.detail_drawer_layout))));

    @Test
    public void OpenDetailView_CheckUI() {
        //1. Find the 2nd recipe card
        //2. Perform click action on the card
//        onData(anything()).inAdapterView(withId(R.id.main_grid_view)).atPosition(1).perform(click());
        onView(withRecyclerView(R.id.main_recyclerView).atPosition(1)).perform(click());
        //3. Check if the recipe detail view UI are displayed as expected
        onView(withId(R.id.detail_ingredients_title)).check(matches(isDisplayed()));
        onView(withId(R.id.detail_ingredients_view)).check(matches(isDisplayed()));
        onView(withId(R.id.detail_steps_title)).check(matches(isDisplayed()));
        onView(withId(R.id.detail_steps_view)).check(matches(isDisplayed()));
    }

    @Test
    public void clickRecipeStep_OpenStepDetailView() {
        //1. Open the 3rd recipe card
        //2. Perform click action on the first step
        onView(withRecyclerView(R.id.main_recyclerView).atPosition(1)).perform(click());
        onView(withRecyclerView(R.id.detail_steps_view).atPosition(0)).perform(click());
        //3. Check if the step detail view is open
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewMatcher player = new ViewMatcher(withId(R.id.player_view));
        onView(allOf(player.childAtPosition(0), isDisplayed())).check(matches(isDisplayed()));
    }

    @Test
    public void clickNavigationDrawer_OpenLeftDrawer() {
        //1. Go to Detail view
        //2. Find the navigation button
        //3. Perform click action on the button
        onView(withRecyclerView(R.id.main_recyclerView).atPosition(1)).perform(click());
        onView(allOf(withContentDescription("Open navigation drawer"),
                withParent(allOf(withId(R.id.action_bar),
                        withParent(withId(R.id.action_bar_container)))),
                isDisplayed())).perform(click());
        //4. Check if the left drawer is open
        onView(withId(R.id.detail_drawer_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void clickHomeNavigation_OpenMainActivity() {
        //1. Go to Detail view, find the navigation Home button
        //2. Perform click action on the button
        onView(withRecyclerView(R.id.main_recyclerView).atPosition(1)).perform(click());
        onView(allOf(withContentDescription("Open navigation drawer"),
                withParent(allOf(withId(R.id.action_bar),
                        withParent(withId(R.id.action_bar_container)))),
                isDisplayed())).perform(click());
        onView(allOf(withId(android.R.id.text1), withText(R.string.action_recipes), leftDrawer.childAtPosition(0),
                isDisplayed())).perform(click());
        //3. Check if the Main activity is open
        onView(withRecyclerView(R.id.main_recyclerView).atPosition(1)).check(matches(isDisplayed()));
    }
}

















