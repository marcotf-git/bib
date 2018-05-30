package com.udacity.gradle.builditbigger;

import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@RunWith(AndroidJUnit4.class)
public class EndpointsAsyncTaskTest {

    private static final String TAG = EndpointsAsyncTaskTest.class.getSimpleName();
    private static final int TIME_OUT_MILLISECONDS = 2000;
    private static final String ERROR_MESSAGE = "Error";

    private String mJokeString = null;
    private Exception mError = null;
    private CountDownLatch signal = null;


    @Before
    public void setUp() {
        // This sets the number of times countDown() must be invoked
        signal = new CountDownLatch(1);
    }

    @Test
    public void testEndspointsAsyncTask() throws InterruptedException {

        EndpointsAsyncTask task = new EndpointsAsyncTask();
        task.setListener(new EndpointsAsyncTask.EndpointsAsyncTaskListener() {
            @Override
            public void onCompleted(String jokeString, Exception e) {
                mJokeString = jokeString;
                mError = e;
                signal.countDown();
            }
        });
        task.execute();

        // It will wait the countDown or 2000ms time
        signal.await(TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS);

        Log.v(TAG, "mJokeString:" + mJokeString);

        // Test if the method has returned an error
        assertNull(mError);

        // test if the method has returned a string
        assertFalse(TextUtils.isEmpty(mJokeString));

        // Test if contains any error message (or the word "error")
        assertFalse(mJokeString.toLowerCase().contains(ERROR_MESSAGE.toLowerCase()));

    }
}