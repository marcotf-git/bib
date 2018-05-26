package com.udacity.gradle.builditbigger;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;



@RunWith(AndroidJUnit4.class)
public class JokeActivityScreenTest {

    private static final String TAG = JokeActivityScreenTest.class.getSimpleName();

    private IdlingResource mIdlingResource;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);
    private IdlingRegistry myIdlingRegistry;


    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        //Espresso.registerIdlingResources(mIdlingResource); (deprecated)
        myIdlingRegistry = IdlingRegistry.getInstance();
        myIdlingRegistry.register(mIdlingResource);
    }



    /**
     * Scrolls to a Recipe and checks if its title is correct.
     */
    @Test
    public void testJoke() {

        onView(ViewMatchers.withId(R.id.button_call_endpoint))
                .perform(click());

        onView(ViewMatchers.withId(R.id.tv_joke))
                .check(matches(isDisplayed()));

    }


    // Unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            //Espresso.unregisterIdlingResources(mIdlingResource);
            myIdlingRegistry.unregister(mIdlingResource);
        }
    }

}