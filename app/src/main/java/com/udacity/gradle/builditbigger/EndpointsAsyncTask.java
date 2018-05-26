package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;


class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {

    private static final String TAG = EndpointsAsyncTask.class.getSimpleName();

    private static MyApi myApiService = null;

    // Listener variables
    private EndpointsAsyncTaskListener mListener = null;
    private Exception mError = null;

    // Listener for communication with the MainActivity
    public interface EndpointsAsyncTaskListener {
        void onCompleted(String jokeString, Exception e);
    }


    @Override
    protected final String doInBackground(Void... voids) {

        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver
            myApiService = builder.build();
        }

        String content = null;

        try {
            content = myApiService.tellJoke().execute().getData();
            Log.v(TAG, "content:" + content);
        } catch (IOException e) {
            Log.v(TAG, "error in loading the database: " + e.getMessage());
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
    }


    public EndpointsAsyncTask setListener(EndpointsAsyncTaskListener listener) {
        this.mListener = listener;
        return this;
    }

}
