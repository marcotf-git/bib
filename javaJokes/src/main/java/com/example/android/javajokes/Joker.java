package com.example.android.javajokes;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//public class Joker {
//
//    private String[] jokes = {
//            "This is my first joke. Please wait for the next!",
//            "This is my second joke. Please wait for the next!",
//            "This is my third joke. Please wait for the next!",
//            "This is my final joke. I will reset to the first!"
//    };
//
//    private static Integer jokeCount;
//
//    public Joker() {
//        if (null == jokeCount){
//            jokeCount = -1;
//        }
//    }
//
//    public String getJoke() {
//
//        jokeCount++;
//
//        if (jokeCount >= jokes.length) {
//            jokeCount = 0;
//        }
//
//        return jokes[jokeCount];
//
//    }
//}

public class Joker {

    private static List<String> jokes;

    private static Integer jokeCount;

    public Joker() {

        if (null == jokeCount){
            jokeCount = -1;
        }

//        if (jokes == null){
//            readJokes();
//        }

        jokes = new ArrayList<String>();
        jokes.add("This is a test Joke number 1");
        jokes.add("This is a test Joke number 2");
        jokes.add("This is a test Joke number 3");

    }

    public String getJoke() {

        jokeCount++;

        if (jokeCount >= jokes.size()) {
            jokeCount = 0;
        }

        String myJoke = jokes.get(jokeCount);

        System.out.println("getJoke myJoke:" + myJoke);

        return myJoke;

    }

    private void readJokes() {

        jokes = new ArrayList<String>();

        JSONParser parser = new JSONParser();

        JSONArray mArray = null;

        try {
            mArray = (JSONArray) parser.parse(new FileReader("assets/jokes.json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (Object myObject : mArray) {

            JSONObject joke = (JSONObject) myObject;

            long id = (long) joke.get("id");
            System.out.println(id);

            String description = (String) joke.get("description");
            System.out.println(description);

            jokes.add(description);

        }
    }
}

