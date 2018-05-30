package com.udacity.gradle.builditbigger.backend;

import com.example.android.javajokes.Joker;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.builditbigger.gradle.udacity.com",
                ownerName = "backend.builditbigger.gradle.udacity.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    /** A simple test endpoint method that takes a name and says Hi back.
     *  This is useful for testing if the server is up.
     **/
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }

    /** The method used by the backend to retrieve the jokes */
    @ApiMethod(name = "tellJoke")
    public MyBean tellJoke() {

        //Joker joke = new Joker();
        String myJoke = Joker.getJoke();

        MyBean response = new MyBean();
        response.setData(myJoke);

        return response;
    }

}
