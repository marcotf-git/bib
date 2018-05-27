package com.udacity.gradle.builditbigger;

import android.test.ApplicationTestCase;
import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class EndpointsAsyncTaskTest extends ApplicationTestCase<Application> {

    private static final String TAG = EndpointsAsyncTaskTest.class.getSimpleName();
    private static final int TIME_OUT_MILLISECONDS = 2000;

    private String mJokeString = null;
    private Exception mError = null;
    private CountDownLatch signal = null;

    public EndpointsAsyncTaskTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() {
        try {
            super.setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // This sets the number of times countDown() must be invoked
        signal = new CountDownLatch(1);
    }

    // This method is automatically called after the test completes
    @Override
    protected void tearDown() {
        try {
            super.tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // This will make the test pass the await
        signal.countDown();
    }


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

        assertNull(mError);
        assertFalse(TextUtils.isEmpty(mJokeString));

    }
}