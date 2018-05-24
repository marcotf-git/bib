package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.v4.util.Pair;
import android.test.ApplicationTestCase;
import android.app.Application;
import android.text.TextUtils;

import java.util.concurrent.CountDownLatch;


public class EndpointsAsyncTaskTest extends ApplicationTestCase<Application> {

    String mJokeString = null;
    Exception mError = null;
    CountDownLatch signal = null;

    public EndpointsAsyncTaskTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        signal = new CountDownLatch(1);
    }

    @Override
    protected void tearDown() throws Exception {
        signal.countDown();
    }

    public void testEndspointAsyncTask() throws InterruptedException {

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

        signal.await();

        assertNull(mError);
        assertFalse(TextUtils.isEmpty(mJokeString));

    }
}