package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.jokelibrary.JokeActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.udacity.gradle.builditbigger.IdlingResource.SimpleIdlingResource;

import static com.example.android.jokelibrary.JokeActivity.JOKE_TEXT;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
        implements View.OnClickListener {

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    private Context context;


    // Mandatory constructor for instantiating the fragment
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getContext();

        View root = inflater.inflate(R.layout.fragment_main, container, false);

        Button mButtonCallEndpoint = root.findViewById(R.id.button_call_endpoint);
        mButtonCallEndpoint.setOnClickListener(this);

        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        return root;
    }


    public void callEndpoint() {

        getIdlingResource();

        EndpointsAsyncTask myTask = new EndpointsAsyncTask();
        myTask.setListener(new EndpointsAsyncTask.EndpointsAsyncTaskListener() {
            @Override
            public void onCompleted(String jokeString, Exception e) {
                Intent myIntent = new Intent(getContext(), JokeActivity.class);
                if (jokeString != null) {
                    myIntent.putExtra(JOKE_TEXT, jokeString);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                }

                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }

            }
        });

        myTask.execute(new Pair<Context, String>(context, ""));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_call_endpoint:
                // Call the async task
                callEndpoint();
                break;
        }
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
