package com.dal_a.snapshoot;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.dal_a.snapshoot.Http.HttpRequest;
import com.dal_a.snapshoot.card.CommentCard;
import com.dal_a.snapshoot.card.SocialLoginCard;
import com.dal_a.snapshoot.model.BaseCard;
import com.dal_a.snapshoot.model.BaseCardView;
import com.dal_a.snapshoot.view.MyRecyclerItemClickListener;
import com.dal_a.snapshoot.view.MyRecyclerView;
import com.dal_a.snapshoot.view.MyRecyclerViewAdapter;
import com.facebook.Profile;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {

    private MyRecyclerView mRecyclerView;
    String imageId;
    private EditText inputCom;

    public static CommentFragment newInstance(String imageId) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString("imageId", imageId);

        fragment.setArguments(args);
        return fragment;
    }

    public CommentFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageId = getArguments().getString("imageId");

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_comment, container, false);

        mRecyclerView = (MyRecyclerView)root.findViewById(R.id.comments);
        setupRecyclerView(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new MyRecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(BaseCardView view, int position) {

            }

            @Override
            public void onItemLongClick(BaseCardView view, int position) {

            }
        });

        new setComments().execute(imageId);


        inputCom = (EditText)root.findViewById(R.id.inputComment);
        final Button sendCom = (Button)root.findViewById(R.id.sendComment);
        sendCom.setEnabled(false);

        /*********** 로그인 확인 ************/
        SharedPreferences settings = getActivity().getSharedPreferences(Constant.PREFS_NAME, 0);
        if(settings.getBoolean(Constant.PREFS_LOGIN_BOOLEAN, false) == false) {
            //로그인 X
            inputCom.setEnabled(false);
            inputCom.setHint("로그인 후 이용가능합니다");
        }


        inputCom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int input = inputCom.getText().length();
                if (input > 0) {
                    sendCom.setEnabled(true);
                }else{
                    sendCom.setEnabled(false);
                }
            }
        });

        sendCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputCom.getText().toString();
                new sendComment().execute(imageId, input);
            }
        });


        return root;
    }


    private void setupRecyclerView(MyRecyclerView recyclerView) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setAdapter(new MyRecyclerViewAdapter(getActivity()));
    }

    protected void setCards(List<BaseCard> cards){
        ((MyRecyclerViewAdapter)(mRecyclerView.getAdapter())).setAllCards(cards);
    }

    private class setComments extends AsyncTask<String, Void, List<BaseCard>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<BaseCard> doInBackground(String... params) {
            //Query the applications
            String mineData = HttpRequest.get(Constant.MAIN_URL + "/getComment/"+params[0]).body();
            //Query the applications
            ArrayList<BaseCard> cards = new ArrayList<>();

            //데이터 parsing -----------------------------------------------------------------------------------------------
            //날라오는 데이터의 형식
            //(str(comment.key)+"**"+str(user.UserName)+"**"+str(comment.content)+"**"+str(comment.Time)
            //(str(image.key)+" "+str(image.blobKey)+"**"+str(image.UserID)+"**"+str(image.Time)+"**"+str(image.favorite))+"**"+str(image.title)+"**"+str(image.text)"|"
            //1. | 단위로 열개의 데이터를 쪼갠다.
            String[] comments = mineData.split("\\|");
            //2. 각각의 이미지를 순회한다.
            if(comments[0].equals(""))  return cards;
            for(int i=0;i<comments.length;i++){
                //3. 공백문자(space) 단위로 데이터를 쪼갠다.
                String[] detail = comments[i].split("\\*\\*");

                //UserName == detail[1]
                //content == detail[2]
                //Time == detail[3]

                CommentCard card = new CommentCard(getActivity());
                card.setId(Long.parseLong(detail[0]));
                card.setName(detail[1]);
                card.setText(detail[2]);
                card.setTime(detail[3]);
                card.setTag(card);
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

    private class sendComment extends AsyncTask<String, Void, String> {

        private String currentUserFacebookID;

        final static String TAG = "sendComment";
        @Override
        protected void onPreExecute() {
            ((Button)getActivity().findViewById(R.id.sendComment)).setEnabled(false);

            currentUserFacebookID = Profile.getCurrentProfile().getId();
//            comment = ((EditText)(getActivity().findViewById(R.id.comment))).getText().toString();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpRequest request = HttpRequest.post(Constant.MAIN_URL+"/addComment");
            //public HttpRequest part(final String name, final String filename, final String contentType, final String part)
            request.part("UserID",currentUserFacebookID);//facebook ID
            request.part("ImageID",params[0]);
            request.part("content", params[1]);

            if (request.ok()) {//전송
                return params[0];
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result != null){
                inputCom.clearFocus();
                inputCom.setText(null);
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                new setComments().execute(result);

            }
            super.onPostExecute(result);
        }
    }

}
