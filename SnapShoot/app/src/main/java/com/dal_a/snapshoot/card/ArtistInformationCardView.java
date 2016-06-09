package com.dal_a.snapshoot.card;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dal_a.snapshoot.Constant;
import com.dal_a.snapshoot.DetailPhotoActivity;
import com.dal_a.snapshoot.Http.HttpRequest;
import com.dal_a.snapshoot.ListActivity;
import com.dal_a.snapshoot.R;
import com.dal_a.snapshoot.model.BaseCard;
import com.dal_a.snapshoot.model.BaseCardView;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by GA on 2015. 9. 16..
 */
public class ArtistInformationCardView extends BaseCardView<ArtistInformationCard> {

    // Default constructors
    public ArtistInformationCardView(Context context) {
        super(context);
    }

    public ArtistInformationCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArtistInformationCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void build(ArtistInformationCard card) {
        super.build(card);

        StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) this.getLayoutParams();
        sglp.setFullSpan(true);
        this.setLayoutParams(sglp);

//        ((TextView)findViewById(R.id.artist)).setText(card.getArtist());

        ((ProfilePictureView)findViewById(R.id.userImage)).setProfileId(card.ToUserID);


        ((TextView) findViewById(R.id.artist2)).setText(card.getName());
        ((TextView) findViewById(R.id.followers)).setText(card.getFollows() + "명의 팔로워");
        ((TextView) findViewById(R.id.introduction)).setText(card.getExplanation());
        final String[] params = {card.getToUserID(), card.getFromUserID()};
        new Followed().execute(params);//기존에 이미 팔로우 관계인지 확인한다.


        if(!params[0].equals(params[1]) && !params[1].equals("0")) {//내가 내 자신을 팔로우 할 수 없다. && 로그인된 상태
            //follow 버튼
            ((Button) findViewById(R.id.follow)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    new updateFollow().execute(params);
                }
            });
        }
        else //내가 내 자신을 팔로우 할 수 없다. && 로그인된 상태
            ((Button) findViewById(R.id.follow)).setVisibility(GONE);

    }

    //기존에 이미 팔로우 관계인지 확인
    private class Followed extends AsyncTask<String, Void, Boolean> {
        private String TAG = "Followed";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if(!params[1].equals("0")) {//현재 유저가 로그인상태일때
                String response = HttpRequest.get(Constant.MAIN_URL + "/isFollowed/"+params[1]+"/"+params[0]).body();
                if(response.equals("True"))
                    return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean a) {
            if(a)//이미 팔로우 관계일때
                ((Button) findViewById(R.id.follow)).setText("FOLLOW 중");
            else//팔로우관계가 아닐떄
                ((Button) findViewById(R.id.follow)).setText("FOLLOW 하기");
            super.onPostExecute(a);
        }

    }

    //팔로우 관계 추가 또는 삭제
    private class updateFollow extends AsyncTask<String, Void, Boolean> {
        private String TAG = "addFollow";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if(!params[1].equals("0")) {//현재 유저가 로그인상태일때

                HttpRequest request = HttpRequest.post(Constant.MAIN_URL + "/toggleFollow");
                request.part("ToUserID", params[0]);
                request.part("FromUserID", params[1]);

                if (request.ok()) {//전송
                    Log.d(TAG, "AddFollow Succeed");//업로드 성공시
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean a) {
            if(a) {//Follow에 성공했을 때 Follow 버튼을 업데이트한다.
                if(((Button) findViewById(R.id.follow)).getText().toString().equals("FOLLOW 중")) {
                    ((Button) findViewById(R.id.follow)).setText("FOLLOW 하기");

                    //~명의 팔로워 개수 업데이트
                    String currentFollowerNum = ((TextView) findViewById(R.id.followers)).getText().toString();
                    int currentFollowerNum_ = Integer.parseInt(currentFollowerNum.split("명")[0]);
                    ((TextView) findViewById(R.id.followers)).setText((currentFollowerNum_-1)+"명의 팔로워");
                }
                else {
                    ((Button) findViewById(R.id.follow)).setText("FOLLOW 중");

                    //~명의 팔로워 개수 업데이트
                    String currentFollowerNum = ((TextView) findViewById(R.id.followers)).getText().toString();
                    int currentFollowerNum_ = Integer.parseInt(currentFollowerNum.split("명")[0]);
                    ((TextView) findViewById(R.id.followers)).setText((currentFollowerNum_+1)+"명의 팔로워");
                }
            }
            super.onPostExecute(a);
        }

    }
}