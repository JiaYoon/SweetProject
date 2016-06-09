package com.dal_a.snapshoot;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dal_a.snapshoot.model.BaseCard;
import com.dal_a.snapshoot.model.ListCard;
import com.dal_a.snapshoot.model.OnButtonPressListener;
import com.dal_a.snapshoot.view.ImageRecyclerViewAdapter;
import com.dal_a.snapshoot.view.MyRecyclerView;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends ListFragment {

//    private SwipeRefreshLayout mSwipeRefreshLayout;
//    private ProgressBar mProgressBar;
//    private MyRecyclerView mRecyclerView;
//    private static String lastImageTime = "";//사진을 동적으로 로드하기 위한 변수
//
    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
//

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_news, container, false);
        mRecyclerView = (MyRecyclerView) root.findViewById(R.id.news);
        setupRecyclerView(mRecyclerView);

//        //어떤 사진을 클릭했을 때
//        mRecyclerView.addOnItemTouchListener(new MyRecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseCardView view, int position) {
//
//            }
//
//            @Override
//            public void onItemLongClick(BaseCardView view, int position) {
//
//            }
//        });


        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new setImages().execute();
            }
        });

        mProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        //new setImages().execute();

        return root;
    }
//
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
    void setupRecyclerView(MyRecyclerView recyclerView) {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ImageRecyclerViewAdapter(getActivity()));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (layoutManager.findLastVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1) {
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
//        NewsCard card = new NewsCard(getActivity());
//        card.setId(Long.parseLong(detail[0]));
//        card.setImage(Constant.MAIN_URL + "/view/" + detail[1]);
//        card.setUserID(detail[2]); //UserID
//        card.setTime(detail[3]);
//        card.setFavorite(Integer.parseInt(detail[4]));
//        card.setTitle(detail[5]);
//        card.setText(detail[6]);
//        card.setMyFavorite(detail[7]);//내가 좋아요 했던 사진이면 "True", 아니면 "False"
//        card.setUserName(detail[8]);
//        card.setCommentsNum(Integer.parseInt(detail[9]));
//        card.setTag(card);

        card.setLayout(getLayout());
        card.setImageListener(new OnButtonPressListener() {
            @Override
            public void onButtonPressedListener(View view, BaseCard card) {
                Intent intent = new Intent(getActivity(), DetailPhotoActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("path", ((ListCard) card).getImage());
                bundle.putLong("id", ((ListCard) card).getId());
                bundle.putString("UserID", ((ListCard) card).getUserID());
                bundle.putString("Time", ((ListCard) card).getTime());
                bundle.putInt("Favorite", ((ListCard) card).getFavorite());
                bundle.putString("Title", ((ListCard) card).getTitle());
                bundle.putString("Text", ((ListCard) card).getText());
                bundle.putString("MyFavorite", ((ListCard) card).getMyFavorite());
                bundle.putInt("CommentsNum", ((ListCard) card).getCommentsNum());
                bundle.putInt("Width",((ListCard) card).getWidth());
                bundle.putInt("Height",((ListCard) card).getHeight());
                bundle.putInt("SigPosition",((ListCard) card).getSigPosition());
                intent.putExtras(bundle);
                getActivity().startActivity(intent, bundle);
//                Constant.clickedCard.clear();
                Constant.clickedCard.add(card.getId());
            }
        });
        card.setLikeListener(new OnButtonPressListener() {
            @Override
            public void onButtonPressedListener(View view, BaseCard card) {
                new ToggleFavorite().execute(((ListCard) card).getId());
                if (view.isSelected()) {

//                        ((ImageView) view).setSelected(false);
//                        ((ImageView) view).setImageResource(R.mipmap.heart);
                    ((ListCard) card).addFavorite(-1);
                    ((ListCard) card).setMyFavorite("False");
                    mRecyclerView.changeCard(card);
                    ((ListActivity)getActivity()).updateViewPager();

//                    ((ImageRecyclerViewAdapter) (mRecyclerView.getAdapter())).changeCard(card.getId(), card);
//                    ((ImageRecyclerViewAdapter) (mRecyclerView.getAdapter())).changeCard(card.getId(), card);


                } else {
//                        ((ImageView) view).setSelected(true);
//                        ((ImageView) view).setImageResource(R.mipmap.heartfull);
                    ((ListCard) card).addFavorite(1);
                    ((ListCard) card).setMyFavorite("True");
                    mRecyclerView.changeCard(card);
                    ((ListActivity)getActivity()).updateViewPager();

                }
            }
        });
        return card;
    }

    @Override
    String getUrlTag() {
        return "timeline";
    }

    @Override
    void setAtFirst() {
        Log.d("adssd", "NEWS");
        new setImages().execute();
    }

    @Override
    void restoreAfterFirst() {
        int size = Constant.clickedCard.size();
        for(int i = 0; i < size; i++){
            new changeImage().execute(Constant.clickedCard.get(i));
        }
        Constant.clickedCard.clear();
        if(Constant.uploadSuccess){
            new setImages().execute();
            Constant.uploadSuccess = false;
        }
    }

    @Override
    int getLayout() {
        return R.layout.card_news;
    }

    //    private class addImages extends AsyncTask<Void, Void, List<BaseCard>> {
//        private final String TAG = "addImages";
//
//        @Override
//        protected void onPreExecute() {
//            mSwipeRefreshLayout.setRefreshing(false);
//            mProgressBar.setVisibility(View.VISIBLE);
//
//            super.onPreExecute();
//        }
//
//        @Override
//        protected List<BaseCard> doInBackground(Void... params) {
//            /*********** 로그인 확인 ************/
//            String currentUserFacebookID = "0";
//            SharedPreferences settings = getActivity().getSharedPreferences(Constant.PREFS_NAME, 0);
//            if (settings.getBoolean(Constant.PREFS_LOGIN_BOOLEAN, false) == true)
//                //로그인 O
//                currentUserFacebookID = Profile.getCurrentProfile().getId();
//
//            String timelineData = HttpRequest.get(Constant.MAIN_URL + "/timeline/" + currentUserFacebookID + "/" + lastImageTime).body();
//
//            return getCard(timelineData);
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
//        private final String TAG = "setImages";
//
//        @Override
//        protected void onPreExecute() {
//
//            //setImages에서는 lastImageTime을 현재시간으로 바꿔줌
//            Calendar calendar = Calendar.getInstance();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//            lastImageTime = dateFormat.format(calendar.getTime());
//            Log.d(TAG, "lastImageTime : " + lastImageTime);
//
//            super.onPreExecute();
//        }
//
//        @Override
//        protected List<BaseCard> doInBackground(Void... params) {
//            /*********** 로그인 확인 ************/
//            String currentUserFacebookID = "0";
//            SharedPreferences settings = getActivity().getSharedPreferences(Constant.PREFS_NAME, 0);
//            if (settings.getBoolean(Constant.PREFS_LOGIN_BOOLEAN, false) == true)
//                //로그인 O
//                currentUserFacebookID = Profile.getCurrentProfile().getId();
//
//            String timelineData = HttpRequest.get(Constant.MAIN_URL + "/timeline/" + currentUserFacebookID + "/" + lastImageTime).body();
//
//            Log.d(TAG, "timelineData : " + timelineData);
//            return getCard(timelineData);
//
//        }
//
//        @Override
//        protected void onPostExecute(List<BaseCard> result) {
//            mProgressBar.setVisibility(View.GONE);
//            mSwipeRefreshLayout.setRefreshing(false);
//            setCards(result);
//            super.onPostExecute(result);
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
//        String TAG = "NewsGetCard";
//        //Query the applications
//        ArrayList<BaseCard> cards = new ArrayList<>();
//        if (data.equals("No Data"))//데이터가 없을 때
//            return cards;
//        //데이터 parsing -----------------------------------------------------------------------------------------------
//        //날라오는 데이터의 형식
//        //(str(image.key)+"**"+str(image.blobKey)+"**"+str(image.UserID)+"**"+str(image.Time)+"**"+str(image.favorite))+"**"+str(image.title)+"**"+str(image.text)"|"
//        //1. | 단위로 열개의 데이터를 쪼갠다.
//        String[] images = data.split("\\|");
//        Log.d(TAG, "Image의 개수 : " + images.length);
//        //2. 각각의 이미지를 순회한다.
//        for (int i = 0; i < images.length; i++) {
//            //3. ** 단위로 데이터를 쪼갠다.
//            Log.d(TAG, images[i]);
//            String[] detail = images[i].split("\\*\\*");
//            Log.d(TAG, "Image당 detail의 개수 : " + detail.length);
//
//            //key == detail[0]
//            //blobKey == detail[1]
//            //UserID == detail[2]
//            //Time == detail[3]
//            //favorite == detail[4]
//            //title == detail[5]
//            //text == detail[6]
//            //myFavorite == detail[7] : 내가 좋아요 했던 사진이면 "True", 아니면 "False"
//            //UserName == datail[8] : NewsFragment에서는 그냥 사용자 이름 한번에 받는게 나을 듯 함
//            //comments == detail[9] : 사진에 등록되어 있는 댓글의 개수
//            NewsCard card = new NewsCard(getActivity());
//            card.setId(Long.parseLong(detail[0]));
//            card.setImage(Constant.MAIN_URL + "/view/" + detail[1]);
//            card.setUserID(detail[2]); //UserID
//            card.setTime(detail[3]);
//            card.setFavorite(Integer.parseInt(detail[4]));
//            card.setTitle(detail[5]);
//            card.setText(detail[6]);
//            card.setMyFavorite(detail[7]);//내가 좋아요 했던 사진이면 "True", 아니면 "False"
//            card.setUserName(detail[8]);
//            card.setCommentsNum(Integer.parseInt(detail[9]));
//            card.setTag(card);
//            card.setImageListener(new OnButtonPressListener() {
//                @Override
//                public void onButtonPressedListener(View view, BaseCard card) {
//                    Intent intent = new Intent(getActivity(), DetailPhotoActivity.class);
//                    Bundle bundle = new Bundle();
//
//                    bundle.putString("path", ((NewsCard) card).getImage());
//                    bundle.putLong("id", ((NewsCard) card).getId());
//                    bundle.putString("UserID", ((NewsCard) card).getUserID());
//                    bundle.putString("Time", ((NewsCard) card).getTime());
//                    bundle.putInt("Favorite", ((NewsCard) card).getFavorite());
//                    bundle.putString("Title", ((NewsCard) card).getTitle());
//                    bundle.putString("Text", ((NewsCard) card).getText());
//                    bundle.putString("MyFavorite", ((NewsCard) card).getMyFavorite());
//                    bundle.putInt("CommentsNum",((NewsCard) card).getCommentsNum());
//                    intent.putExtras(bundle);
//                    getActivity().startActivity(intent, bundle);
//                    Constant.clickedCard.clear();
//                    Constant.clickedCard.add(card.getId());
//                }
//            });
//            card.setLikeListener(new OnButtonPressListener() {
//                @Override
//                public void onButtonPressedListener(View view, BaseCard card) {
//                    new ToggleFavorite().execute(((NewsCard) card).getId());
//                    if (view.isSelected()) {
//
////                        ((ImageView) view).setSelected(false);
////                        ((ImageView) view).setImageResource(R.mipmap.heart);
//                        ((NewsCard) card).addFavorite(-1);
//                        ((NewsCard) card).setMyFavorite("False");
//                        ((ImageRecyclerViewAdapter) (mRecyclerView.getAdapter())).changeCard(card.getId(), card);
//
//                    } else {
////                        ((ImageView) view).setSelected(true);
////                        ((ImageView) view).setImageResource(R.mipmap.heartfull);
//                        ((NewsCard) card).addFavorite(1);
//                        ((NewsCard) card).setMyFavorite("True");
//                        ((ImageRecyclerViewAdapter) (mRecyclerView.getAdapter())).changeCard(card.getId(), card);
//                    }
//                }
//            });
//            cards.add(card);
//
//
//            //마지막 사진의 시간을 기록한다
//            if (i == images.length - 1) {
//                String[] timeTemp = detail[3].split("\\.");
//                timeTemp[0] = timeTemp[0].replaceAll(" ", "-");
//                timeTemp[0] = timeTemp[0].replaceAll(":", "-");
//                Log.d("adf", timeTemp[0]);
//                lastImageTime = timeTemp[0];
//            }
//        }
//
//        return cards;
//    }
}
