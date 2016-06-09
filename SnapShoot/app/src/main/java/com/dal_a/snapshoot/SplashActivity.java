package com.dal_a.snapshoot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {
    static final int SPLASH_DISPLAY_LENGTH = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startService(new Intent(SplashActivity.this, LockScreenService.class));

                FacebookSdk.sdkInitialize(getApplicationContext());


                // Add code to print out the key hash
                try {
                    PackageInfo info = getPackageManager().getPackageInfo(
                            "com.dal_a.snapshoot",
                            PackageManager.GET_SIGNATURES);
                    for (Signature signature : info.signatures) {
                        MessageDigest md = MessageDigest.getInstance("SHA");
                        md.update(signature.toByteArray());
                        Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                    }
                } catch (PackageManager.NameNotFoundException e) {

                } catch (NoSuchAlgorithmException e) {

                }

                SharedPreferences settings = getSharedPreferences(Constant.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();

                if (AccessToken.getCurrentAccessToken() == null) {
                    editor.putInt(Constant.PREFS_SOCIAL_INT, Constant.SOCIAL_NULL);
                    editor.putBoolean(Constant.PREFS_LOGIN_BOOLEAN, false);
                }
                else {
                    editor.putInt(Constant.PREFS_SOCIAL_INT, Constant.SOCIAL_FACEBOOK );
                    editor.putBoolean(Constant.PREFS_LOGIN_BOOLEAN, true);
                }
                // Commit the edits!
                editor.commit();

                startActivity(new Intent(SplashActivity.this, ListActivity.class));
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH * 1000);

//        startService(new Intent(this, LockScreenService.class));
//
//        FacebookSdk.sdkInitialize(getApplicationContext());
//
//
//        // Add code to print out the key hash
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.dal_a.snapshoot",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
//
//        SharedPreferences settings = getSharedPreferences(Constant.PREFS_NAME, 0);
//        SharedPreferences.Editor editor = settings.edit();
//
//        if (AccessToken.getCurrentAccessToken() == null) {
//            editor.putInt(Constant.PREFS_SOCIAL_INT, Constant.SOCIAL_NULL);
//            editor.putBoolean(Constant.PREFS_LOGIN_BOOLEAN, false);
//        }
//        else {
//            editor.putInt(Constant.PREFS_SOCIAL_INT, Constant.SOCIAL_FACEBOOK );
//            editor.putBoolean(Constant.PREFS_LOGIN_BOOLEAN, true);
//        }
//        // Commit the edits!
//        editor.commit();
//
//        startActivity(new Intent(SplashActivity.this, ListActivity.class));
//        finish();
    }

}
