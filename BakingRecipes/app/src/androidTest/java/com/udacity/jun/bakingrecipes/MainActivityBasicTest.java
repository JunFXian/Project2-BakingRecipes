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

public class MainActivityBasicTest {
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    // Convenience helper
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    private ViewMatcher leftDrawer = new ViewMatcher(allOf(withId(R.id.left_drawer),
            withParent(withId(R.id.drawer_layout))));

    @Test
    public void clickRecipeCard_OpenDetailView() {
        //1. Find the first recipe card
        //2. Perform click action on the card
//        onData(anything()).inAdapterView(withId(R.id.main_grid_view)).atPosition(2).perform(click());
        onView(withRecyclerView(R.id.main_recyclerView).atPosition(1)).perform(click());
        //3. Check if the recipe detail view is open
        onView(withId(R.id.detail_ingredients_title)).check(matches(isDisplayed()));
    }

    @Test
    public void clickNavigationDrawer_OpenLeftDrawer() {
        //1. Find the navigation button
        //2. Perform click action on the button
        onView(allOf(withContentDescription("Open navigation drawer"),
                withParent(allOf(withId(R.id.action_bar),
                        withParent(withId(R.id.action_bar_container)))),
                isDisplayed())).perform(click());
        //3. Check if the left drawer is open
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void clickAboutNavigation_OpenAboutDialog() {
        //1. Find the navigation About button
        //2. Perform click action on the button
        onView(allOf(withContentDescription("Open navigation drawer"),
                withParent(allOf(withId(R.id.action_bar),
                        withParent(withId(R.id.action_bar_container)))),
                isDisplayed())).perform(click());
        onView(allOf(withId(android.R.id.text1), withText(R.string.action_about), leftDrawer.childAtPosition(0),
                isDisplayed())).perform(click());
        //3. Check if the About dialog is open
        onView(withId(R.id.about_dialog)).check(matches(withText(R.string.about_dialog)));
    }
}

















