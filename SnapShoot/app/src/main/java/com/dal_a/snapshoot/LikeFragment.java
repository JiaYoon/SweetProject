package com.dal_a.snapshoot;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dal_a.snapshoot.Http.HttpRequest;
import com.dal_a.snapshoot.card.ArtistInformationCard;
import com.dal_a.snapshoot.card.SocialLoginCard;
import com.dal_a.snapshoot.model.BaseCard;
import com.dal_a.snapshoot.model.BaseCardView;
import com.dal_a.snapshoot.model.ListCard;
import com.dal_a.snapshoot.view.ImageRecyclerViewAdapter;
import com.dal_a.snapshoot.view.MyRecyclerItemClickListener;
import com.dal_a.snapshoot.view.MyRecyclerView;
import com.facebook.Profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LikeFragment extends ListFragment {

//    private SwipeRefreshLayout mSwipeRefreshLayout;
//    private ProgressBar mProgressBar;
//    private MyRecyclerView mRecyclerView;
//    private static String lastImageTime = "";//사진을 동적으로 로드하기 위한 변수


    public static LikeFragment newInstance() {
        LikeFragment fragment = new LikeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LikeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_like, container, false);
        mRecyclerView = (MyRecyclerView) root.findViewById(R.id.like);
        setupRecyclerView(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new MyRecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(BaseCardView view, int position) {
                Intent intent = new Intent(getActivity(), DetailPhotoActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("path", ((ListCard) view.getTag()).getImage());
                bundle.putLong("id", ((ListCard) view.getTag()).getId());
                bundle.putString("UserID", ((ListCard) view.getTag()).getUserID());
                bundle.putString("Time", ((ListCard) view.getTag()).getTime());
                bundle.putInt("Favorite", ((ListCard) view.getTag()).getFavorite());
                bundle.putString("Title", ((ListCard) view.getTag()).getTitle());
                bundle.putString("Text", ((ListCard) view.getTag()).getText());
                bundle.putString("MyFavorite",((ListCard) view.getTag()).getMyFavorite());
                bundle.putInt("CommentsNum", ((ListCard) view.getTag()).getCommentsNum());

                intent.putExtras(bundle);
                getActivity().startActivity(intent, bundle);
//                Constant.clickedCard.clear();
                Constant.clickedCard.add(((ListCard) view.getTag()).getId());

            }

            @Override
            public void onItemLongClick(BaseCardView view, int position) {

            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new setImages(){
                    @Override
                    protected List<BaseCard> doInBackground(Void... params) {
                        List<BaseCard> cards = super.doInBackground();

                        if(currentUserFacebookID.equals("0")){
                            SocialLoginCard card = new SocialLoginCard(getActivity());
                            cards.add(card);
                        }

                        return cards;
                    }

                }.execute();
            }
        });

        mProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);



        return root;

    }

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
    void setupRecyclerView(MyRecyclerView recyclerView) {

        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new ImageRecyclerViewAdapter(getActivity()));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int arr[] = new int[3];
                    layoutManager.findLastCompletelyVisibleItemPositions(arr);
                    if (arr[2] == recyclerView.getAdapter().getItemCount() - 1) {
                        new addImages().execute();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    ListCard getCard(ListCard card) {
        card.setLayout(getLayout());
        return card;
    }

    @Override
    String getUrlTag() {
        return "like";
    }

    @Override
    void setAtFirst() {
        Log.d("adssd", "Like");
        new setImages(){
            @Override
            protected List<BaseCard> doInBackground(Void... params) {
                List<BaseCard> cards = super.doInBackground();

                if(currentUserFacebookID.equals("0")){
                    SocialLoginCard card = new SocialLoginCard(getActivity());
                    cards.add(card);
                }

                return cards;
            }

        }.execute();
    }

    @Override
    void restoreAfterFirst() {
        new setImages(){
            @Override
            protected List<BaseCard> doInBackground(Void... params) {
                List<BaseCard> cards = super.doInBackground();

                if(currentUserFacebookID.equals("0")){
                    SocialLoginCard card = new SocialLoginCard(getActivity());
                    cards.add(card);
                }

                return cards;
            }

        }.execute();
    }

    @Override
    int getLayout() {
        return R.layout.card_mine;
    }

//
//
//    protected void setCards(List<BaseCard> cards) {
//        ((ImageRecyclerViewAdapter) (mRecyclerView.getAdapter())).setAllCards(cards);
//    }
//
//    protected void addCards(List<BaseCard> cards) {
//        ((ImageRecyclerViewAdapter) (mRecyclerView.getAdapter())).addAllCards(cards);
//    }
//
//
//    protected void changeCard(BaseCard card) {
//        ((ImageRecyclerViewAdapter) (mRecyclerView.getAdapter())).changeCard(card.getId(), card);
//    }
//
//
//    private class addImages extends AsyncTask<Void, Void, List<BaseCard>> {
//        private String TAG = "MineFragmentAddImages";
//
//        @Override
//        protected void onPreExecute() {
//            mSwipeRefreshLayout.setRefreshing(false);
//            mProgressBar.setVisibility(View.VISIBLE);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected List<BaseCard> doInBackground(Void... params) {
//            /*********** 로그인 확인 ************/
//            SharedPreferences settings = getActivity().getSharedPreferences(Constant.PREFS_NAME, 0);
//            if(settings.getBoolean(Constant.PREFS_LOGIN_BOOLEAN, false) == false) {
//                //로그인 X
//                ArrayList<BaseCard> cards = new ArrayList<>();
//
//                SocialLoginCard card = new SocialLoginCard(getActivity());
//                cards.add(card);
//                return cards;
//            }
//
//            String currentUserFacebookID = Profile.getCurrentProfile().getId();
//
//            String mineData = HttpRequest.get(Constant.MAIN_URL + "/like/" + currentUserFacebookID + "/" + lastImageTime).body();
//            return getCard(mineData);
//        }
//
//        @Override
//        protected void onPostExecute(List<BaseCard> result) {
//            mProgressBar.setVisibility(View.GONE);
//
//            addCards(result);
//            super.onPostExecute(result);
//        }
//    }
//
//    private class setImages extends AsyncTask<Void, Void, List<BaseCard>> {
//        private String TAG = "MineFragmentAddImages";
//
//        @Override
//        protected void onPreExecute() {
//
//            //setImages에서는 lastImageTime을 현재시간으로 바꿔줌
//            Calendar calendar = Calendar.getInstance();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//            lastImageTime = dateFormat.format(calendar.getTime());
//            super.onPreExecute();
//        }
//
//        @Override
//        protected List<BaseCard> doInBackground(Void... params) {
//
//            Log.d(TAG, "로그인 안됨");
//            /*********** 로그인 확인 ************/
//            SharedPreferences settings = getActivity().getSharedPreferences(Constant.PREFS_NAME, 0);
//            if(settings.getBoolean(Constant.PREFS_LOGIN_BOOLEAN, false) == false) {
//                //로그인 X
//                ArrayList<BaseCard> cards = new ArrayList<>();
//
//                SocialLoginCard card = new SocialLoginCard(getActivity());
//                cards.add(card);
//                return cards;
//            }
//
//            String currentUserFacebookID = Profile.getCurrentProfile().getId();
//            String mineData = HttpRequest.get(Constant.MAIN_URL + "/like/" + currentUserFacebookID + "/" + lastImageTime).body();
//
//            return getCard(mineData);
//        }
//
//        @Override
//        protected void onPostExecute(List<BaseCard> result) {
//            mProgressBar.setVisibility(View.GONE);
//            setCards(result);
//            super.onPostExecute(result);
//            mSwipeRefreshLayout.setRefreshing(false);
//
//        }
//    }
//
//
//    private class changeImage extends AsyncTask<Long, Void, List<BaseCard>> {
//        private final String TAG = "setImages";
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected List<BaseCard> doInBackground(Long... params) {
//            /*********** 로그인 확인 ************/
//            String currentUserFacebookID = "0";
//            SharedPreferences settings = getActivity().getSharedPreferences(Constant.PREFS_NAME, 0);
//            if (settings.getBoolean(Constant.PREFS_LOGIN_BOOLEAN, false) == true)
//                //로그인 O
//                currentUserFacebookID = Profile.getCurrentProfile().getId();
//
//            String timelineData = HttpRequest.get(Constant.MAIN_URL + "/getSingleImageInfo/" + params[0]+"/" +currentUserFacebookID).body();
//
//
//            return getCard(timelineData);
//
//        }
//
//        @Override
//        protected void onPostExecute(List<BaseCard> result) {
//            if(result.size()>0)
//                changeCard(result.get(0));
//            super.onPostExecute(result);
//        }
//    }
//
//    List<BaseCard> getCard(String data) {
//        String TAG = "LikeGetCard";
//        ArrayList<BaseCard> cards = new ArrayList<>();
//        if (data.equals("No Data")) {//데이터가 없을 때
//            Log.d(TAG, "No Data");
//            return cards;
//        }
//
//        //데이터 parsing -----------------------------------------------------------------------------------------------
//        //날라오는 데이터의 형식
//        //(str(image.key)+" "+str(image.blobKey)+"**"+str(image.UserID)+"**"+str(image.Time)+"**"+str(image.favorite))+"**"+str(image.title)+"**"+str(image.text)"|"
//        //1. | 단위로 열개의 데이터를 쪼갠다.
//        String[] images = data.split("\\|");
//        Log.d(TAG, "Image의 개수 : " + images.length);
//        //2. 각각의 이미지를 순회한다.
//        if(images[0].equals(""))  return cards;
//
//        for (int i = 0; i < images.length; i++) {
//            //3. ** 단위로 데이터를 쪼갠다.
//            String[] detail = images[i].split("\\*\\*");
//            Log.d(TAG, "Image당 detail의 개수 : " + detail.length);
//            //key == detail[0]
//            //blobKey == detail[1]
//            //UserID == detail[2]
//            //Time == detail[3]
//            //favorite == detail[4]
//            //title == detail[5]
//            //text == detail[6]
//            //myFavorite == detail[7]
//            //commentsNum == detail[8]
//            MineCard card = new MineCard(getActivity());
//            card.setId(Long.parseLong(detail[0]));
//            card.setImage(Constant.MAIN_URL + "/view/" + detail[1]);
//            card.setUserID(detail[2]);
//            card.setTime(detail[3]);
//            card.setFavorite(Integer.parseInt(detail[4]));
//            card.setTitle(detail[5]);
//            card.setText(detail[6]);
//            card.setMyFavorite(detail[7]);
//            card.setCommentsNum(Integer.parseInt(detail[9]));
//            card.setTag(card);
//            cards.add(card);
//
//            //마지막 사진의 시간을 기록한다
//            if (i == images.length - 1) {
//                String[] timeTemp = detail[3].split("\\.");
//                timeTemp[0] = timeTemp[0].replaceAll(" ", "-");
//                timeTemp[0] = timeTemp[0].replaceAll(":", "-");
//                lastImageTime = timeTemp[0];
//            }
//        }
//
//        return cards;
//
//    }
////
////    private class setImages extends AsyncTask<Void, Void, List<BaseCard>> {
////        private String TAG = "LikeFragmentSetImage";
////        private String currentUserFacebookID;
////        @Override
////        protected void onPreExecute() {
////            mSwipeRefreshLayout.setRefreshing(false);
////            mProgressBar.setVisibility(View.VISIBLE);
////            try {
////                currentUserFacebookID = Profile.getCurrentProfile().getId();
////            }catch (Exception e){
////
////            }
////            super.onPreExecute();
////        }
////
////        @Override
////        protected List<BaseCard> doInBackground(Void... params) {
////            //Query the applications
////            Log.d(TAG,Constant.MAIN_URL + "/like/" + currentUserFacebookID);
////            String mineData = HttpRequest.get(Constant.MAIN_URL + "/like/" + currentUserFacebookID).body();
////            //Query the applications
////            ArrayList<BaseCard> cards = new ArrayList<>();
////            if(mineData.equals("No Data"))//데이터가 없을 때
////                return cards;
////
////
////            //데이터 parsing -----------------------------------------------------------------------------------------------
////            //날라오는 데이터의 형식
////            //(str(image.key)+" "+str(image.blobKey)+"**"+str(image.UserID)+"**"+str(image.Time)+"**"+str(image.favorite))+"**"+str(image.title)+"**"+str(image.text)"|"
////            //1. | 단위로 열개의 데이터를 쪼갠다.
////            String[] images = mineData.split("\\|");
////            Log.d(TAG,"Image의 개수 : "+images.length);
////            //2. 각각의 이미지를 순회한다.
////            for(int i=0;i<images.length;i++){
////                //3. ** 단위로 데이터를 쪼갠다.
////                String[] detail = images[i].split("\\*\\*");
////                Log.d(TAG,"Image당 detail의 개수 : "+detail.length);
////                //key
////                String imageID = "";
////                for(int j=13;j<detail[0].length()-1;j++)
////                    imageID += detail[0].charAt(j);
////                //blobKey == detail[1]
////                //UserID == detail[2]
////                //Time == detail[3]
////                //favorite == detail[4]
////                //title == detail[5]
////                //text == detail[6]
////                MineCard card = new MineCard(getActivity());
////                card.setId(imageID);
////                card.setImage(Constant.MAIN_URL + "/view/" + detail[1]);
////                card.setLikes(Integer.parseInt(detail[4]));
////                card.setTag(card);
////                cards.add(card);
////            }
////
////            return cards;
////
////        }
////
////        @Override
////        protected void onPostExecute(List<BaseCard> result) {
////            mProgressBar.setVisibility(View.GONE);
////
////            setCards(result);
////            super.onPostExecute(result);
////        }
////    }

}

