package com.dal_a.snapshoot;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by GA on 2015. 9. 9..
 */
public class Constant {

    public static ArrayList<Long> clickedCard = new ArrayList<>();
    public static boolean uploadSuccess = false;
    //실행 중인 Activity 저장
    public static ArrayList<Activity> actList = new ArrayList<>();
    public static final String PREFS_NAME = "Snapshoot";

    public static final String PREFS_LOGIN_BOOLEAN = "Login";
    public static final String PREFS_SOCIAL_INT = "Social";

    public static final int SOCIAL_NULL = 0;
    public static final int SOCIAL_FACEBOOK = 1;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    static final String SENDER_ID = "402463951293";

    /**
     * Web client ID from Google Cloud console.
     */
    static final String WEB_CLIENT_ID = "AIzaSyBsHUX8t8r7m2r1KunpYaQPieLSA7tPyqI";

    /**
     * The web client ID from Google Cloud Console.
     */
    static final String AUDIENCE_ANDROID_CLIENT_ID =
            "server:client_id:" + WEB_CLIENT_ID;

    /**
     * The URL to the API. Default when running locally on your computer:
     * "http://10.0.2.2:8080/_ah/api/"
     */
    public static final String MAIN_URL = "http://sonorous-antler-104213.appspot.com";
//    public static final String ROOT_URL = "http://10.0.2.2:8080/_ah/api/";
    /**
     * Default constructor, never called.
     */
    private Constant() { }

}
