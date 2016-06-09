package com.dal_a.snapshoot;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextClock;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dal_a.snapshoot.Http.HttpRequest;
import com.facebook.Profile;
import com.nvanbenschoten.motion.ParallaxImageView;

import java.text.DateFormat;
import java.util.Date;

public class LockScreenActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private String TAG = "LockScreenActivity";

    GestureDetector gd;
    private static final int SWIPE_MIN_DISTANCE = 400;
    public static final String GESTURE_TAG = "GestureListener";

    private String path;
    private Long id;
    private String userID;
    private String time;
    private String favorite;
    private String title;
    private String text;
    private String MyFavorite;
    private String UserName;
    private int CommentsNum;
    private int Width;
    private int Height;
    private int SigPosition;
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        Bundle bundle = new Bundle();
//        bundle.putString("path", path);
//        bundle.putLong("id", id);
//        bundle.putString("userID", userID);
//        bundle.putString("favorite", favorite);
//        bundle.putString("title", title);
//        bundle.putString("text", text);
//        bundle.putString("MyFavorite", MyFavorite);
//        bundle.putString("UserName", UserName);
//        bundle.putInt("CommentsNum", CommentsNum);
//        bundle.putInt("Width",Width);
//        bundle.putInt("Height",Height);
//        bundle.putInt("SigPosition",SigPosition);
//        outState.putBundle("save_data", bundle);
//        super.onSaveInstanceState(outState);
//    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

//        if (savedInstanceState != null) {
//            Bundle bundle = savedInstanceState.getBundle("save_data");
//            path = bundle.getString("path");
//            id = bundle.getLong("id");
//            userID = bundle.getString("userID");
//            time = bundle.getString("time");
//            favorite = bundle.getString("favorite");
//            title = bundle.getString("title");
//            text = bundle.getString("text");
//            MyFavorite = bundle.getString("MyFavorite");
//            UserName = bundle.getString("UserName");
//            CommentsNum = bundle.getInt("CommentsNum");
//            Width = bundle.getInt("Width");
//            Height = bundle.getInt("Height");
//            SigPosition = bundle.getInt("SigPosition");
//        }


        gd = new GestureDetector(LockScreenActivity.this, LockScreenActivity.this);
        gd.setIsLongpressEnabled(false);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);



//        String urlServer = "http://sonorous-antler-104213.appspot.com/view_photo/AMIfv95lTdbquC75ziTEe7LaAFeA0m3nvwbM_j48ZGQDN50FaRiZ8Y7CdwqFO-d1zUwJTGVDPiXEAeKoCT6pFSWZiN1Ud0wehLE-KR37lHBLABUAl24nIMpBMvip310E_Klwt5y5lLzjf31s6ZZI7FI3nHOxb-la4N05-bZkkAjVIp74rJ9EFCQ";
//        Glide.with(this)
//                .load(R.drawable.sample1)
//                .placeholder(R.drawable.loading)
//                .into(background);


        ((TextClock) findViewById(R.id.clock)).setTypeface(Typeface.createFromAsset(this.getAssets(), "TRUE_TYPOLA.ttf"));


        //랜덤하게 이미지 획득
        new GetRandomImageFromServer().execute();


        //사진 자세히 보기
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LockScreenActivity.this, DetailPhotoActivity.class);


                Bundle bundle = new Bundle();
                bundle.putString("path", path);
                bundle.putLong("id", id);
                bundle.putString("UserID", userID);
                bundle.putString("Time", time);
                bundle.putInt("Favorite", Integer.parseInt(favorite));
                bundle.putString("Title", title);
                bundle.putString("Text", text);
                bundle.putString("MyFavorite", MyFavorite);
                bundle.putInt("CommentsNum", CommentsNum);
                bundle.putInt("Width", Width);
                bundle.putInt("Height", Height);
                bundle.putInt("SigPosition", SigPosition);

                intent.putExtras(bundle);
                startActivity(intent, bundle);
                finish();

            }
        });
        //카메라 열기
        findViewById(R.id.more).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                startActivity(takePictureIntent);
                finish();
                return false;
            }
        });

        overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);


        ((TextClock) findViewById(R.id.date)).setFormat12Hour("MMM dd E");
        ((TextClock) findViewById(R.id.clock)).setFormat12Hour("hh:mm");

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ParallaxImageView) findViewById(R.id.background)).registerSensorManager();

    }

    @Override
    public void onPause() {
        ((ParallaxImageView) findViewById(R.id.background)).unregisterSensorManager();
        super.onPause();
    }


    public boolean dispatchTouchEvent(MotionEvent ev) {
        gd.onTouchEvent(ev);

        if (ev.getAction() == MotionEvent.ACTION_UP) {
            ((LockScreenEffectCanvas) findViewById(R.id.effectScreen)).setPosition(-1, -1);
        }

        return super.dispatchTouchEvent(ev);
    }

    // BEGIN_INCLUDE(init_gestureListener)
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // Up motion completing a single tap occurred.
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // Touch has been long enough to indicate a long press.
        // Does not indicate motion is complete yet (no up event necessarily)

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {

        ((LockScreenEffectCanvas) findViewById(R.id.effectScreen)).setPosition(e2.getX(), e2.getY());
        float disX = e2.getX() - e1.getX();
        float disY = e2.getY() - e1.getY();

        float distance = (float) Math.sqrt(disX * disX + disY * disY);
        if (distance > SWIPE_MIN_DISTANCE)
            finish();

        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // Fling event occurred.  Notification of this one happens after an "up" event.
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // User performed a down event, and hasn't moved yet.
    }

    @Override
    public boolean onDown(MotionEvent e) {
        ((LockScreenEffectCanvas) findViewById(R.id.effectScreen)).setPosition(e.getX(), e.getY());

        // "Down" event - User touched the screen.
        return true;
    }

    //사진 랜덤하게 가져오기
    private class GetRandomImageFromServer extends AsyncTask<Void, Void, String> {
        private String TAG = "GetRandomImageFromServer";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            /*********** 로그인 확인 ************/
            String currentUserFacebookID = "0";
            SharedPreferences settings = LockScreenActivity.this.getSharedPreferences(Constant.PREFS_NAME, 0);
            if (settings.getBoolean(Constant.PREFS_LOGIN_BOOLEAN, false) == true)
                //로그인 O
                currentUserFacebookID = Profile.getCurrentProfile().getId();


            String randomBlobKeyAndUserID = HttpRequest.get(Constant.MAIN_URL + "/randomPicture/"+currentUserFacebookID).body();//랜덤 사진의 blobkey 획득
            String[] randomData = randomBlobKeyAndUserID.split("\\*\\*");

            // imageId = randomData[0]
            // blobKey = randomData[1]
            // UserID = randomData[2]
            // Time = randomData[3]
            // Favorite = randomData[4]
            // title = randomData[5]
            // text = randomData[6]
            // myFavorite = randomData[7]
            // UserName = randomData[8]
            // CommentsNum = randomData[9]
            //width == randomData[10] : 사진 너비
            //height == randomData[11] : 사진 높이
            //sigPosition == randomData[12] : 낙관 위치

            //임시 코드임 삭제 예정 *************************
            if(randomData[10].equals("None"))
                randomData[10] = "500";
            if(randomData[11].equals("None"))
                randomData[11] = "500";
            if(randomData[12].equals("None"))
                randomData[12] = "1";
            //*********************************************

            path = Constant.MAIN_URL + "/view/" + randomData[1];
            id = Long.parseLong(randomData[0]);
            userID = randomData[2];
            time = randomData[3];
            favorite = randomData[4];
            title = randomData[5];
            text = randomData[6];
            MyFavorite = randomData[7];
            UserName = randomData[8];
            CommentsNum = Integer.parseInt(randomData[9]);
            Width = Integer.parseInt(randomData[10]);
            Height = Integer.parseInt(randomData[11]);
            SigPosition = Integer.parseInt(randomData[12]);
            return path;
        }

        @Override
        protected void onPostExecute(String path) {
            ParallaxImageView background = (ParallaxImageView) LockScreenActivity.this.findViewById(R.id.background);
            Glide.with(LockScreenActivity.this)
                    .load(path)
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.loading)
                    .into(background);

            ((TextView) findViewById(R.id.LockName)).setText(UserName);
            ((TextView) findViewById(R.id.LockTitle)).setText(title);
            super.onPostExecute(path);
        }
    }

}
