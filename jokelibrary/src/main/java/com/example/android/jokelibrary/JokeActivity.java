package com.example.android.jokelibrary;

import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {

    private static final String TAG = JokeActivity.class.getSimpleName();
    public static final String JOKE_TEXT = "joke text";

    TextView mJokeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // inside your activity (if you did not enable transitions in your theme)
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            // Apply activity transition
            // set an enter transition
            getWindow().setEnterTransition(new Explode());
        }

        setContentView(R.layout.activity_joke);

        String myJoke = getIntent().getStringExtra(JOKE_TEXT);

        mJokeView = findViewById(R.id.tv_joke);
        mJokeView.setText(myJoke);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar mActionBar = getSupportActionBar();

        if(mActionBar != null) {
            // Enable the Up button
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Enable action to close the activity
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
