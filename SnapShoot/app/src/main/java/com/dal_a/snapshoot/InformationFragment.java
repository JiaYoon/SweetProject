package com.dal_a.snapshoot;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dal_a.snapshoot.Http.HttpRequest;
import com.dal_a.snapshoot.card.ArtistInformationCard;
import com.dal_a.snapshoot.card.PhotoInformationCard;
import com.dal_a.snapshoot.model.SquareImageCard;
import com.dal_a.snapshoot.model.BaseCard;
import com.dal_a.snapshoot.model.BaseCardView;
import com.dal_a.snapshoot.view.MyRecyclerItemClickListener;
import com.dal_a.snapshoot.view.MyRecyclerView;
import com.dal_a.snapshoot.view.MyRecyclerViewAdapter;
import com.facebook.Profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {

    private MyRecyclerView mRecyclerView;
    private static String[] params;
    public static InformationFragment newInstance(String[] params) {
        //넘어오는 데이터 String[] params = {path,id,UserID,Time,Favorite,Title,Text,MyFavorite};
        InformationFragment.params = params;//static을 안쓰는 더좋은 방법 고민해보기
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public InformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_information, container, false);

        mRecyclerView = (MyRecyclerView)root.findViewById(R.id.photos);
        setupRecyclerView(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new MyRecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(BaseCardView view, int position) {

            }

            @Override
            public void onItemLongClick(BaseCardView view, int position) {

            }
        });

        new setImages().execute();

        return root;
    }

    private void setupRecyclerView(MyRecyclerView recyclerView) {

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MyRecyclerViewAdapter(getActivity()));
    }

    protected void setCards(List<BaseCard> cards){
        ((MyRecyclerViewAdapter)(mRecyclerView.getAdapter())).addAllCards(cards);
    }


    private class setImages extends AsyncTask<Void, Void, List<BaseCard>> {
        String currentUserFacebookID = "0";
        @Override
        protected void onPreExecute() {
            /*********** 로그인 확인 ************/
            currentUserFacebookID = "0";
            SharedPreferences settings = getActivity().getSharedPreferences(Constant.PREFS_NAME, 0);
            if(settings.getBoolean(Constant.PREFS_LOGIN_BOOLEAN, false)) {
                currentUserFacebookID = Profile.getCurrentProfile().getId();
            }

            super.onPreExecute();

        }

        @Override
        protected List<BaseCard> doInBackground(Void... params) {
            //Query the applications
            String userInfo = HttpRequest.get(Constant.MAIN_URL + "/getUserInfo/"+InformationFragment.params[2]).body();
            //Result = user[0].UserName+"**"+user[0].UserIntroduction+"**"+user[0].Category+"**"+str(user[0].Follower)
            String[] result = userInfo.split("\\*\\*");
            //UserName = result[0]
            //UserIntroduction = result[1]
            //Category = result[2] (사진을 올린 사용자가 좋아하는 카테고리)
            //Follower = result[3] (사진을 올린 사용자의 팔로워수)

            ArrayList<BaseCard> cards = new ArrayList<>();

            //넘어오는 데이터 String[] params = {path,id,UserID,Time,Favorite,Title,Text,MyFavorite,Follows};
            //사진에 대한 정보를 보여주는 카드
            PhotoInformationCard photoInformation = new PhotoInformationCard(getActivity());
            photoInformation.setTitle(InformationFragment.params[5]);
            photoInformation.setArtist(result[0]);
            photoInformation.setEct(InformationFragment.params[3]);
            photoInformation.setExplanation(InformationFragment.params[6]);
            cards.add(photoInformation);


            ArtistInformationCard artistInformation = new ArtistInformationCard(getActivity());
            artistInformation.setName(result[0]);
            artistInformation.setExplanation(result[1]);
            artistInformation.setFollows(result[3]);
            artistInformation.setToUserID(InformationFragment.params[2]);
            artistInformation.setFromUserID(currentUserFacebookID);
            artistInformation.setLayout(R.layout.card_artist_information);
            cards.add(artistInformation);


            // 사진 업로더의 이전 사진을 보여주는 부분
            //setImages에서는 lastImageTime을 현재시간으로 바꿔줌
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String currentTime = dateFormat.format(calendar.getTime());
            String mineData = HttpRequest.get(Constant.MAIN_URL + "/mine/" + InformationFragment.params[2] + "/" + currentTime).body();
            String[] images = mineData.split("\\|");
            for (int i = 0; i < images.length; i++) {
                //3. ** 단위로 데이터를 쪼갠다.
                String[] detail = images[i].split("\\*\\*");
                //key == detail[0]
                //blobKey == detail[1]
                //UserID == detail[2]
                //Time == detail[3]
                //favorite == detail[4]
                //title == detail[5]
                //text == detail[6]
                SquareImageCard card = new SquareImageCard(getActivity());
                card.setId(Long.parseLong(detail[0]));
                card.setTag("SqaureImageCard");
                card.setImage(Constant.MAIN_URL + "/view/" + detail[1] + "/500/500");//100,100은 각각 너비,높이
                cards.add(card);
            }


            return cards;

        }

        @Override
        protected void onPostExecute(List<BaseCard> result) {
            setCards(result);

            super.onPostExecute(result);
        }
    }
}
