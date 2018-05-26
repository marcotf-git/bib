package com.udacity.gradle.builditbigger;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.jokelibrary.JokeActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import static com.example.android.jokelibrary.JokeActivity.JOKE_TEXT;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
        implements View.OnClickListener {

    private Context mContext;
    private InterstitialAd mInterstitialAd;
    private View mFragmentView;

    // Listener variable
    private OnCallEndpointListener mCallback;

    // Listener for communication with the RecipeDetailActivity.
    // This is used for testing with Espresso idling resources.
    public interface OnCallEndpointListener {
        void onCallEndpoint(Boolean idleState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;

        try {
            mCallback = (OnCallEndpointListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCallEndpointListener");
        }
    }

    // Mandatory constructor for instantiating the fragment.
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);

        // This will be used for better transition setup
        mFragmentView = root.findViewById(R.id.rl_joke_fragment);

        Button mButtonCallEndpoint = root.findViewById(R.id.button_call_endpoint);
        mButtonCallEndpoint.setOnClickListener(this);

        AdView mAdView = root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        // Interstitial ad setup
        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        return root;
    }

    @Override
    public void onStart() {
        // This is for better transition.
        mFragmentView.setVisibility(View.VISIBLE);
        super.onStart();
    }

    public void callEndpoint() {

        // This is used for testing with Espresso idling resources.
        mCallback.onCallEndpoint(false);

        EndpointsAsyncTask myTask = new EndpointsAsyncTask();
        myTask.setListener(new EndpointsAsyncTask.EndpointsAsyncTaskListener() {

            @Override
            public void onCompleted(String jokeString, Exception e) {
                Intent myIntent = new Intent(getContext(), JokeActivity.class);
                if (jokeString != null) {
                    myIntent.putExtra(JOKE_TEXT, jokeString);

                    // This is for better transition
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(myIntent,
                                ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                    } else {
                        startActivity(myIntent);
                    }

                } else {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    // This is for better transition.
                    mFragmentView.setVisibility(View.VISIBLE);
                }

                // This is used for testing with Espresso idling resources.
                mCallback.onCallEndpoint(true);

            }
        });

        myTask.execute();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_call_endpoint:

                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        // Call the async task for loading the joke.
                        callEndpoint();
                        // Load the next interstitial.
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                });

                if (mInterstitialAd.isLoaded()) {
                    // This is for better transition
                    mFragmentView.setVisibility(View.INVISIBLE);
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                break;
        }
    }

}
