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

import static com.example.android.jokelibrary.JokeActivity.JOKE_TEXT;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
        implements View.OnClickListener {

    private static final String TAG = MainActivityFragment.class.getSimpleName();

    private Context mContext;
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

        View root = inflater.inflate(R.layout.fragment_main, container, false);

        // This will be used for better transition setup
        mFragmentView = root.findViewById(R.id.rl_joke_fragment);
        mLoadingIndicator = root.findViewById(R.id.pb_loading_indicator);

        Button mButtonCallEndpoint = root.findViewById(R.id.button_call_endpoint);
        mButtonCallEndpoint.setOnClickListener(this);

        return root;
    }

    @Override
    public void onStart() {
        // This is for better transition.
        mFragmentView.setVisibility(View.VISIBLE);
        super.onStart();
    }

    // This function will call the server for querying the data
    public void callEndpoint() {

        // This is used for testing with Espresso idling resources.
        // It will trigger the idling state to false.
        mCallback.onCallEndpoint(false);

        EndpointsAsyncTask myTask = new EndpointsAsyncTask();
        myTask.setListener(new EndpointsAsyncTask.EndpointsAsyncTaskListener() {

            @Override
            public void onCompleted(String jokeString, Exception e) {

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

                // This is used for testing with Espresso idling resources.
                // It will trigger the idling state to true.
                mCallback.onCallEndpoint(true);
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

                // Call the server for querying the data
                callEndpoint();

                break;
        }
    }

}