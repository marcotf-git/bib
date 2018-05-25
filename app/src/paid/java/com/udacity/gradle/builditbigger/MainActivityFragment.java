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

import com.example.android.jokelibrary.JokeActivity;

import static com.example.android.jokelibrary.JokeActivity.JOKE_TEXT;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
        implements View.OnClickListener {

    private Context context;

    // Listener variable
    private OnCallEndpointListener mCallback;



    // Listener for communication with the RecipeDetailActivity
    public interface OnCallEndpointListener {
        void onCallEndpoint(Boolean idleState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnCallEndpointListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCallEndpointListener");
        }
    }

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

        return root;
    }


    public void callEndpoint() {

        mCallback.onCallEndpoint(false);

        EndpointsAsyncTask myTask = new EndpointsAsyncTask();
        myTask.setListener(new EndpointsAsyncTask.EndpointsAsyncTaskListener() {
            @Override
            public void onCompleted(String jokeString, Exception e) {
                Intent myIntent = new Intent(getContext(), JokeActivity.class);
                if (jokeString != null) {
                    myIntent.putExtra(JOKE_TEXT, jokeString);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

                mCallback.onCallEndpoint(true);

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


}
