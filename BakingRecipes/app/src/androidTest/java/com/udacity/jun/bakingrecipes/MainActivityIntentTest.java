/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: Aug 4, 2017
 * Reference:
 *      https://github.com/udacity/AdvancedAndroid_TeaTime/blob/TESP.03-Solution-AddOrderSummaryActivityTest/app/src/androidTest/java/com/example/android/teatime/OrderSummaryActivityTest.java
 *      https://spin.atomicobject.com/2016/04/15/espresso-testing-recyclerviews/
 */

package com.udacity.jun.bakingrecipes;

import android.app.Activity;
import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.jun.bakingrecipes.models.RecipeItem;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Serializable;

import static android.app.Instrumentation.ActivityResult;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)

public class MainActivityIntentTest {
    private static final RecipeItem recipe = null;

    @Rule
    public IntentsTestRule<MainActivity> mMainIntentTestRule
            = new IntentsTestRule<>(MainActivity.class);

    // Convenience helper
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

//    @Test
//    public void clickRecipeCard_SendsRecipe() {
//        //1. Find the first recipe card
//        //2. Perform click action on the card
////        onData(anything()).inAdapterView(withId(R.id.main_grid_view)).atPosition(1).perform(click());
//        onView(withRecyclerView(R.id.main_recyclerView).atPosition(1)).perform(click());
//        //3. Check if intent sent by the application.
//        intended(allOf(hasAction(Intent.ACTION_SEND), hasExtra(Intent.EXTRA_TEXT, (Serializable) recipe)));
//    }
}

















