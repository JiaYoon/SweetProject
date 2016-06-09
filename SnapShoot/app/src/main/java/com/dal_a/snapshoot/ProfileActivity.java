package com.dal_a.snapshoot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dal_a.snapshoot.Http.HttpRequest;
import com.facebook.login.widget.ProfilePictureView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private final String TAG = "ProfileActivity";

    String UserID;
    String UserToken;
    String selectedString;

    boolean sendEnable = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Bundle bundle = new Bundle();
        bundle.putString("selected", selectedString);
        bundle.putString("userid", UserID);
        bundle.putString("usertoken", UserToken);

        outState.putBundle("save_data", bundle);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        if (savedInstanceState != null) {
            Bundle bundle = savedInstanceState.getBundle("save_data");
            selectedString = bundle.getString("selected");
            UserID = bundle.getString("userid");
            UserToken = bundle.getString("usertoken");
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("회원 가입(2/2)");
        getSupportActionBar().setSubtitle("당신에 대해 말해주세요");

        //Get UserID From SelectCategoryActivity
        Intent intent = getIntent();
        UserID = intent.getStringExtra("UserID");
        UserToken = intent.getStringExtra("UserToken");

        final ArrayList<Integer> selected = intent.getIntegerArrayListExtra("Selected");
        String selectedString_ = "";//선택된 카테고리들을 하나의 스트링으로 변환함
        for(int i=0;i<selected.size();i++)
            selectedString_ += selected.get(i);
        selectedString = selectedString_;

        if (UserID != null) {
            //HERE: DISPLAY USER'S PICTURE
            ((ProfilePictureView)findViewById(R.id.userImage)).setProfileId(UserID);
        }

        ((EditText)findViewById(R.id.inputNameText)).addTextChangedListener(t);
        ((EditText)findViewById(R.id.inputNameText)).setFocusable(true);
        ((EditText)findViewById(R.id.inputIntroductionText)).addTextChangedListener(t);
//        Button join = (Button) findViewById(R.id.join);
//        join.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "SnapShoot Join request");
//                //get UserName
//                String UserName = ((EditText)findViewById(R.id.inputNameText)).getText().toString();
//                if(UserName.equals("")) {//if UserName is not appropriate,
//                    Toast.makeText(ProfileActivity.this, "이름을 입력해 주세요", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                String UserIntroduction = ((EditText)findViewById(R.id.inputIntroductionText)).getText().toString();
//                if(UserIntroduction.equals("")){
//                    Toast.makeText(ProfileActivity.this, "어떤 사진가인지 말씀해주세요", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                //Post to Server
//                String[] userData = {UserID,UserToken,UserName,UserIntroduction,selectedString};
//                new LoginByFacebook().execute(userData);
//
//            }
//        });
    }

    TextWatcher t = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int l = ((EditText)findViewById(R.id.inputNameText)).getText().length();
            int r = ((EditText)findViewById(R.id.inputIntroductionText)).getText().length();
            if(l > 0 & r > 0){
                sendEnable = true;
            }else{
                sendEnable = false;
            }
            invalidateOptionsMenu();
        }
    };
    //For login -- AsyncTask<UserID,void,void>
    private class LoginByFacebook extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... UserInfo) {
            Map<String, String> data = new HashMap<String, String>();
            data.put("UserID", UserInfo[0]);
            data.put("UserToken",UserInfo[1]);
            data.put("UserName", UserInfo[2]);
            data.put("UserIntroduction",UserInfo[3]);
            data.put("Category",UserInfo[4]);
            if (HttpRequest.post(Constant.MAIN_URL + "/login").form(data).created()) {
                System.out.println("User was created");
            }
            SharedPreferences settings = getSharedPreferences(Constant.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(Constant.PREFS_LOGIN_BOOLEAN, true);
            editor.putInt(Constant.PREFS_SOCIAL_INT, Constant.SOCIAL_FACEBOOK);
            editor.commit();

            return null;
        }

        @Override
        protected void onPostExecute(Void a) {
            Log.d(TAG, "Login Success");
            Toast.makeText(ProfileActivity.this,"로그인이 완료되었습니다",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ProfileActivity.this,SelectPhotoActivity.class);
            startActivity(intent);

            ProfileActivity.this.finish();
            super.onPostExecute(a);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modify_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                Log.d(TAG, "SnapShoot Join request");
                //get UserName
                String UserName = ((EditText)findViewById(R.id.inputNameText)).getText().toString();
                if(UserName.equals("")) {//if UserName is not appropriate,
                    Toast.makeText(ProfileActivity.this, "이름을 입력해 주세요", Toast.LENGTH_SHORT).show();
                    return true;
                }
                String UserIntroduction = ((EditText)findViewById(R.id.inputIntroductionText)).getText().toString();
                if(UserIntroduction.equals("")){
                    Toast.makeText(ProfileActivity.this, "어떤 사진가인지 말씀해주세요", Toast.LENGTH_SHORT).show();
                    return true;
                }
                //Post to Server
                String[] userData = {UserID,UserToken,UserName,UserIntroduction,selectedString};
                new LoginByFacebook().execute(userData);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_send);

        if (sendEnable) {
            item.setEnabled(true);
            item.getIcon().setAlpha(255);
        } else {
            // disabled
            item.setEnabled(false);
            item.getIcon().setAlpha(130);
        }

        return true;
    }
}
