package com.example.android.javajokes;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Joker {

    private static final String PATH = "assets/";
    private static final String FILE_NAME = "jokes.json";

    private static List<String> jokes;
    private static Integer jokeCount;

    // This is only for testing
    public static void main(String[] args) {
        Joker.getJoke();
    }

    public Joker() {
    }

    public static String getJoke() {

        System.out.println("getJoke");

        // Only read the jokes if they aren't on (static) memory yet
        if (null == jokes) {
            jokes = readJokes();
        }

        if (null == jokeCount){
            jokeCount = 0;
        } else if (jokeCount >= jokes.size()) {
            jokeCount = 0;
        }

        String myJoke;

        if(null != jokes && jokes.size() > 0) {
            myJoke = jokes.get(jokeCount);
            jokeCount++;
            System.out.println("getJoke return: myJoke:" + myJoke);
            return myJoke;
        } else {
            return "Error in loading the joke.";
        }

    }

    private static List<String> readJokes() {

        System.out.println("readJokes");

        if (null == jokes) {

            jokes = new ArrayList<>();

            JSONParser parser = new JSONParser();
            JSONArray mArray = null;

            String errorMessage = "error";

            try {
                mArray = (JSONArray) parser.parse(new FileReader(PATH + FILE_NAME));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                errorMessage = e.getMessage();
            }

            if (null != mArray) {
                for (Object myObject : mArray) {
                    JSONObject joke = (JSONObject) myObject;
                    long id = (long) joke.get("id");
                    String description = (String) joke.get("description");
                    System.out.println(id + " " + description);
                    jokes.add(description);
                }
            }

            if (jokes.size() == 0) {
                jokes.add(errorMessage);
            }

        }

        return jokes;

    }
}

