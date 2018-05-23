package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.javajokes.Joker;
import com.example.android.jokelibrary.JokeActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener{

    Button mButtonCallEndpoint;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mButtonCallEndpoint = root.findViewById(R.id.button_call_endpoint);
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


//    public void tellJoke() {
//
//        Joker joke = new Joker();
//        String myJoke = joke.getJoke();
//        //Toast.makeText(this, myJoke, Toast.LENGTH_SHORT).show();
//        Intent myIntent = new Intent(getContext(), JokeActivity.class);
//        myIntent.putExtra(JOKE_TEXT, myJoke);
//        startActivity(myIntent);
//
//    }

    public void callEndpoint() {
        new EndpointsAsyncTask().execute(new Pair<Context, String>(getContext(), ""));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_call_endpoint:
                callEndpoint();
                break;
        }

    }
}
