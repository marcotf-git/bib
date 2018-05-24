package com.example.android.javajokes;

public class Joker {

    private String[] jokes = {
            "This is my first joke. Please wait for the next!",
            "This is my second joke. Please wait for the next!",
            "This is my third joke. Please wait for the next!",
            "This is my final joke. I will reset to the first!"
    };

    private static Integer jokeCount;

    public Joker() {
        if (null == jokeCount){
            jokeCount = -1;
        }
    }

    public String getJoke() {

        jokeCount++;

        if (jokeCount >= jokes.length) {
            jokeCount = 0;
        }

        return jokes[jokeCount];

    }
}
