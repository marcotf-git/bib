package com.example.android.jokelibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {

    public static final String JOKE_TEXT = "joke text";

    TextView mJokeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        String myJoke = getIntent().getStringExtra(JOKE_TEXT);

        mJokeView = findViewById(R.id.tv_joke);
        mJokeView.setText(myJoke);

    }
}
