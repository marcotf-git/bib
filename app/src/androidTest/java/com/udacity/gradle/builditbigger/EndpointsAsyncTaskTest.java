package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.v4.util.Pair;
import android.test.ApplicationTestCase;
import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class EndpointsAsyncTaskTest extends ApplicationTestCase<Application> {

    private static final String TAG = EndpointsAsyncTaskTest.class.getSimpleName();

    String mJokeString = null;
    Exception mError = null;
    CountDownLatch signal = null;

    public EndpointsAsyncTaskTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        // This sets the number of times countDown() must be invoked
        signal = new CountDownLatch(1);
    }

    // This method is automatically called after the test completes
    @Override
    protected void tearDown() throws Exception {
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
        task.execute(new Pair<Context, String>(getContext(), ""));

        // It will wait the countDown or 2000ms time
        signal.await(2000, TimeUnit.MILLISECONDS);

        Log.v(TAG, "mJokeString:" + mJokeString);

        assertNull(mError);
        assertFalse(TextUtils.isEmpty(mJokeString));

    }
}