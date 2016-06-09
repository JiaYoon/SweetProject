package com.dal_a.snapshoot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dal_a.snapshoot.Http.HttpRequest;
import com.dal_a.snapshoot.model.BaseCard;
import com.dal_a.snapshoot.model.ListCard;
import com.dal_a.snapshoot.model.OnButtonPressListener;
import com.dal_a.snapshoot.view.ImageRecyclerViewAdapter;
import com.dal_a.snapshoot.view.MyRecyclerView;
import com.facebook.Profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by GA on 2015. 9. 26..
 */
public abstract class ListFragment extends Fragment {
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected ProgressBar mProgressBar;
    protected MyRecyclerView mRecyclerView;
    protected String lastImageTime;//사진을 동적으로 로드하기 위한 변수

    public void onSaveInstanceState(Bundle outState) {
        outState.putString("time", lastImageTime);
    }


//
//    @Override
//    public void onStart(){
//        super.onStart();
//        int size = Constant.clickedCard.size();
//        if(size == 0){
//            new setImages().execute();
//            return;
//        }
//        for(int i = 0; i < size; i++){
//            new changeImage().execute(Constant.clickedCard.get(i));
//            //이미지 정보 서버에서 불러와서
//            //이미지 아이디를 인자로 : Constant.clickedCard.get(i)
//
////            ((ImageRecyclerViewAdapter) mRecyclerView.getAdapter()).changeCard(Constant.clickedCard.get(i), card);
//        }
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle b = getArguments();
        lastImageTime = b.getString("time");
    }

    @Override
    public void onStart(){
        super.onStart();
        if(lastImageTime != null) {
            //복구
            Log.d("asdad", "복구 ");
            restoreAfterFirst();
        }else {
            //first Time
            setAtFirst();
            Log.d("asdad", "첫번째");
        }
    }


    abstract void setupRecyclerView(MyRecyclerView recyclerView);

    protected class addImages extends AsyncTask<Void, Void, List<BaseCard>> {
        private final String TAG = "ADDIMAGE_LISTFRAGMENT";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(false);
            mProgressBar.setVisibility(View.VISIBLE);

            Log.d(TAG, "ADDIMAGE_LISTFRAGMENT _ " + lastImageTime);
        }

        @Override
        protected List<BaseCard> doInBackground(Void... params) {
            /*********** 로그인 확인 ************/
            String currentUserFacebookID = "0";
            SharedPreferences settings = getActivity().getSharedPreferences(Constant.PREFS_NAME, 0);
            if (settings.getBoolean(Constant.PREFS_LOGIN_BOOLEAN, false) == true)
                //로그인 O
                currentUserFacebookID = Profile.getCurrentProfile().getId();

            String datas = HttpRequest.get(Constant.MAIN_URL + "/"+getUrlTag()+"/" + currentUserFacebookID + "/" + lastImageTime).body();

            return getCards(datas);
        }

        @Override
        protected void onPostExecute(List<BaseCard> result) {
            mProgressBar.setVisibility(View.GONE);

            mRecyclerView.addCards(result);
            super.onPostExecute(result);

        }
    }

    protected class setImages extends AsyncTask<Void, Void, List<BaseCard>> {
        private final String TAG = "setImages";

        protected String currentUserFacebookID = "0";
        @Override
        protected void onPreExecute() {
            if(!mSwipeRefreshLayout.isRefreshing()){
                mProgressBar.setVisibility(View.VISIBLE);
            }

            //setImages에서는 lastImageTime을 현재시간으로 바꿔줌
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            lastImageTime = dateFormat.format(calendar.getTime());
            Log.d(TAG, "lastImageTime : " + lastImageTime);

            super.onPreExecute();
        }

        @Override
        protected List<BaseCard> doInBackground(Void... params) {
            /*********** 로그인 확인 ************/
//            String currentUserFacebookID = "0";
            SharedPreferences settings = getActivity().getSharedPreferences(Constant.PREFS_NAME, 0);
            if (settings.getBoolean(Constant.PREFS_LOGIN_BOOLEAN, false) == true)
                //로그인 O
                currentUserFacebookID = Profile.getCurrentProfile().getId();

            String datas = HttpRequest.get(Constant.MAIN_URL + "/"+getUrlTag()+"/" + currentUserFacebookID + "/" + lastImageTime).body();
            Log.d("path", Constant.MAIN_URL + "/"+getUrlTag()+"/" + currentUserFacebookID + "/" + lastImageTime);

            Log.d(TAG, "timelineData : " + datas);
            return getCards(datas);

        }

        @Override
        protected void onPostExecute(List<BaseCard> result) {
            //마지막 사진의 시간을 기록한다
            mRecyclerView.setCards(result);
            mSwipeRefreshLayout.setRefreshing(false);
            mProgressBar.setVisibility(View.GONE);


            super.onPostExecute(result);
        }
    }


    protected class changeImage extends AsyncTask<Long, Void, List<BaseCard>> {
        private final String TAG = "setImages";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<BaseCard> doInBackground(Long... params) {
            /*********** 로그인 확인 ************/
            String currentUserFacebookID = "0";
            SharedPreferences settings = getActivity().getSharedPreferences(Constant.PREFS_NAME, 0);
            if (settings.getBoolean(Constant.PREFS_LOGIN_BOOLEAN, false) == true)
                //로그인 O
                currentUserFacebookID = Profile.getCurrentProfile().getId();

            String data = HttpRequest.get(Constant.MAIN_URL + "/getSingleImageInfo/" + params[0]+"/" +currentUserFacebookID).body();

            return getCard(data);

        }

        @Override
        protected void onPostExecute(List<BaseCard> result) {
            if(result.size()>0)
                mRecyclerView.changeCard(result.get(0));
            super.onPostExecute(result);
        }
    }

    private List<BaseCard> getCard(String data){
        String TAG = "NewsGetCard";
        //Query the applications
        ArrayList<BaseCard> cards = new ArrayList<>();
        if (data.equals("No Data"))//데이터가 없을 때
            return cards;
        //데이터 parsing -----------------------------------------------------------------------------------------------
        //날라오는 데이터의 형식
        //(str(image.key)+"**"+str(image.blobKey)+"**"+str(image.UserID)+"**"+str(image.Time)+"**"+str(image.favorite))+"**"+str(image.title)+"**"+str(image.text)"|"
        //1. | 단위로 열개의 데이터를 쪼갠다.
        String[] images = data.split("\\|");
        Log.d(TAG, "Image의 개수 : " + images.length);
        //2. 각각의 이미지를 순회한다.
        for (int i = 0; i < images.length; i++) {
            //3. ** 단위로 데이터를 쪼갠다.
            Log.d(TAG, images[i]);
            String[] detail = images[i].split("\\*\\*");
            Log.d(TAG, "Image당 detail의 개수 : " + detail.length);

            //key == detail[0]
            //blobKey == detail[1]
            //UserID == detail[2]
            //Time == detail[3]
            //favorite == detail[4]
            //title == detail[5]
            //text == detail[6]
            //myFavorite == detail[7] : 내가 좋아요 했던 사진이면 "True", 아니면 "False"
            //UserName == datail[8] : NewsFragment에서는 그냥 사용자 이름 한번에 받는게 나을 듯 함
            //comments == detail[9] : 사진에 등록되어 있는 댓글의 개수
            //width == detail[10] : 사진 너비
            //height == detail[11] : 사진 높이
            //sigPosition == detail[12] : 낙관 위치

            //임시 코드임 삭제 예정 *************************
            if(detail[10].equals("None"))
                detail[10] = "500";
            if(detail[11].equals("None"))
                detail[11] = "500";
            if(detail[12].equals("None"))
                detail[12] = "1";
            //*********************************************

            ListCard card = new ListCard(getActivity());
            card.setId(Long.parseLong(detail[0]));
            card.setImage(Constant.MAIN_URL + "/view/" + detail[1]);
            card.setUserID(detail[2]); //UserID
            card.setTime(detail[3]);
            card.setFavorite(Integer.parseInt(detail[4]));
            card.setTitle(detail[5]);
            card.setText(detail[6]);
            card.setMyFavorite(detail[7]);//내가 좋아요 했던 사진이면 "True", 아니면 "False"
            card.setUserName(detail[8]);
            card.setCommentsNum(Integer.parseInt(detail[9]));
            card.setWidth(Integer.parseInt(detail[10]));
            card.setHeight(Integer.parseInt(detail[11]));
            card.setSigPosition(Integer.parseInt(detail[12]));

            card.setTag(card);
            cards.add(getCard(card));
        }

        return cards;

    }

    private List<BaseCard> getCards(String data) {
        List<BaseCard> cards = getCard(data);
        //마지막 사진의 시간을 기록한다
        if (cards.size()>0) {

            String[] timeTemp = ((ListCard)cards.get(cards.size()-1)).getTime().split("\\.");
            timeTemp[0] = timeTemp[0].replaceAll(" ", "-");
            timeTemp[0] = timeTemp[0].replaceAll(":", "-");
            Log.d("adf", timeTemp[0]);
            lastImageTime = timeTemp[0];
        }

        return cards;
    }

    abstract ListCard getCard(ListCard c);
    abstract String getUrlTag();
    abstract void setAtFirst();
    abstract void restoreAfterFirst();
    abstract int getLayout();
}
