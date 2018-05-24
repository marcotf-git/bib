package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.util.Pair;
import android.util.Log;
import android.widget.Toast;

import com.example.android.jokelibrary.JokeActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.IdlingResource.SimpleIdlingResource;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

import static android.support.v4.content.ContextCompat.startActivity;
import static com.example.android.jokelibrary.JokeActivity.JOKE_TEXT;
import static java.security.AccessController.getContext;


class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {

    private static final String TAG = EndpointsAsyncTask.class.getSimpleName();

    private static MyApi myApiService = null;
    private Context context;
    private String result;

    // Listener variables
    private EndpointsAsyncTaskListener mListener = null;
    private Exception mError = null;

    // Listener for communication with the MainActivity
    public interface EndpointsAsyncTaskListener {
        void onCompleted(String jokeString, Exception e);
    }


    @Override
    protected String doInBackground(Pair<Context, String>... params) {

        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        context = params[0].first;
        String name = params[0].second;

        String content = null;

        try {
            content = myApiService.tellJoke().execute().getData();
        } catch (IOException e) {
            Log.v(TAG, e.getMessage());
            mError = e;
        }

        return content;
    }

    @Override
    protected void onPostExecute(String result) {

        //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        if(this.mListener != null) {
            this.mListener.onCompleted(result, mError);
        }
        this.result = result;

    }

    public EndpointsAsyncTask setListener(EndpointsAsyncTaskListener listener) {
        this.mListener = listener;
        return this;
    }

}
