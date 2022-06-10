package com.example.parstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Post.class);

        // initialize Parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("iQ2fGHrIkHe29PAnkJ5DirChP34MJgtPyYRmFR5v")
                .clientKey("zmIc256s0QR3sqSEwZvOO8R2C6dqC2bTXjsvQ0HP")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
