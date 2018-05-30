package com.udacity.gradle.builditbigger;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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

    private static final String TAG = MainActivityFragment.class.getSimpleName();

    private Context mContext;
    private InterstitialAd mInterstitialAd;
    private View mFragmentView;
    private ProgressBar mLoadingIndicator;

    // Listener variable
    private OnCallEndpointListener mCallback;

    // Listener for communication with the MainActivity.
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(TAG, "onCreateView");

        View root = inflater.inflate(R.layout.fragment_main, container, false);

        // This will be used for better transition setup
        mFragmentView = root.findViewById(R.id.rl_joke_fragment);
        mLoadingIndicator = root.findViewById(R.id.pb_loading_indicator);

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
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        return root;
    }

    @Override
    public void onStart() {
        // This is for better transition.
        mFragmentView.setVisibility(View.VISIBLE);
        super.onStart();
    }

    // This function will call the server for querying the data
    private void callEndpoint() {

        // This is used for testing with Espresso idling resources.
        // It will trigger the idling state to false.
        mCallback.onCallEndpoint(false);

        final EndpointsAsyncTask myTask = new EndpointsAsyncTask();

        myTask.setListener(new EndpointsAsyncTask.EndpointsAsyncTaskListener() {
            @Override
            public void onCompleted(String jokeString, Exception e) {

                Log.v(TAG, "EndpointsAsyncTaskListener onCompleted Activity:" + getActivity());

                // This will test if the Fragment is attached to the Activity and prevent error
                // after rotating the device.
                if(isAdded()) {

                    mLoadingIndicator.setVisibility(View.INVISIBLE);

                    // It will start the activity for showing the joke
                    Intent myIntent = new Intent(mContext, JokeActivity.class);

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
                        Log.e(TAG, "onCompleted:" + e.getMessage());
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                        // This is for better transition.
                        mFragmentView.setVisibility(View.VISIBLE);
                    }
                }

                // This is used for testing with Espresso idling resources.
                mCallback.onCallEndpoint(true);

                // Unregister the listener
                myTask.unregisterListener();
            }
        });

        // This will start the async task for querying the data
        myTask.execute();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_call_endpoint:

                mLoadingIndicator.setVisibility(View.VISIBLE);
                // This is for better transition
                mFragmentView.setVisibility(View.INVISIBLE);

                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        // Call the async task for loading the joke.
                        callEndpoint();
                        // Load the next interstitial.
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        Log.e(TAG, "onAdFailedToLoad");
                        // Call the async task for loading the joke.
                        callEndpoint();
                    }
                });

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.e("TAG", "The interstitial wasn't loaded yet.");
                    Toast.makeText(mContext, "Failed to load the add.", Toast.LENGTH_LONG).show();
                    // Call the async task for loading the joke.
                    callEndpoint();
                    // Load the next interstitial.
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

                break;
        }
    }

}
