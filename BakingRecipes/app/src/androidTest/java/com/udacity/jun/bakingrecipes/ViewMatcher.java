/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: Aug 8, 2017
 * Reference:
 */

package com.udacity.jun.bakingrecipes;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ViewMatcher {
    private final Matcher<View> mParentMatcher;

    public ViewMatcher(Matcher<View> parentMatcher) {
        this.mParentMatcher = parentMatcher;
    }

    public Matcher<View> childAtPosition(final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                mParentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && mParentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}