package com.dal_a.snapshoot;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dal_a.snapshoot.Http.HttpRequest;
import com.dal_a.snapshoot.card.SelectableCard;
import com.dal_a.snapshoot.model.BaseCard;
import com.dal_a.snapshoot.model.BaseCardView;
import com.dal_a.snapshoot.view.MyRecyclerItemClickListener;
import com.dal_a.snapshoot.view.MyRecyclerView;
import com.dal_a.snapshoot.view.MyRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectCategoryActivity extends AppCompatActivity {


    ArrayList<Integer> selected = new ArrayList<>();

    String UserID;
    String UserToken;

    boolean sendEnable = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList("selected", selected);
        bundle.putString("userid", UserID);
        bundle.putString("usertoken", UserToken);

        outState.putBundle("save_data", bundle);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        if (savedInstanceState != null) {
            Bundle bundle = savedInstanceState.getBundle("save_data");
            selected = bundle.getIntegerArrayList("selected");
            UserID = bundle.getString("userid");
            UserToken = bundle.getString("usertoken");
        }
        //Get UserID From ListActivity
        Intent intent = getIntent();
        UserID = intent.getStringExtra("UserID");
        UserToken = intent.getStringExtra("UserToken");

        //Statusbar 설정
        final View statusbar = findViewById(R.id.statusbar);
        if (Build.VERSION.SDK_INT < 19) {
            statusbar.setVisibility(View.GONE);
        }


        //Toolbar 설정
        // Title를 설정해준다
        // 배경을 설정해준다
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("회원 가입(1/2)");
        getSupportActionBar().setSubtitle("어떤 사진가인가요?");


        MyRecyclerView mRecyclerView = (MyRecyclerView)findViewById(R.id.recyclerview);
        setupRecyclerView(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new MyRecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(BaseCardView view, int position) {
                if (view.findViewById(R.id.upper).isSelected()) {
                    view.findViewById(R.id.upper).setSelected(false);
                    ((SelectableCard) (view.getTag())).setSelecting(false);
                    selected.remove((Object) position);
                    if(selected.size() == 0) {
                        sendEnable = false;
                        invalidateOptionsMenu();
                    }

                } else {
                    view.findViewById(R.id.upper).setSelected(true);
                    ((SelectableCard) (view.getTag())).setSelecting(true);
                    selected.add(position);
                    sendEnable = true;
                    invalidateOptionsMenu();

                }
            }

            @Override
            public void onItemLongClick(BaseCardView view, int position) {

            }
        });

//        //카테고리 선택 완료
//        Button selectComplete = (Button)findViewById(R.id.selectComplete);
//        selectComplete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(selected.size() == 0){
//                    Toast.makeText(SelectCategoryActivity.this,"하나 이상의 카테고리를 선택하세요",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                Intent intent = new Intent(SelectCategoryActivity.this,ProfileActivity.class);
//                intent.putIntegerArrayListExtra("Selected",selected);
//                intent.putExtra("UserID",UserID);
//                intent.putExtra("UserToken",UserToken);
//                startActivity(intent);
//                SelectCategoryActivity.this.finish();
//            }
//        });

        new setImage().execute();
    }


    private void setupRecyclerView(MyRecyclerView recyclerView) {

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new MyRecyclerViewAdapter(this));
    }

    protected void setCards(List<BaseCard> cards){
        ((MyRecyclerViewAdapter)(((RecyclerView)findViewById(R.id.recyclerview)).getAdapter())).setAllCards(cards);
    }

    private class setImage extends AsyncTask<Void, Void, List<BaseCard>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<BaseCard> doInBackground(Void... params) {
            String categoryData = HttpRequest.get(Constant.MAIN_URL + "/getCategory").body();
            ArrayList<BaseCard> cards = new ArrayList<>();

            String[] categories = categoryData.split("\\|");
            for(int i=0;i<categories.length;i++){
                String[] detail = categories[i].split("\\*\\*");
                // index = detail[0]
                // name = detail[1]
                // imageURL = detail[2]
                SelectableCard card = new SelectableCard(getApplicationContext());
                if(selected.contains((Object)i))
                    card.setSelecting(true);
                else
                    card.setSelecting(false);
                card.setImage(detail[2]);
                card.setName(detail[1]);
                card.setIndex(Integer.parseInt(detail[0]));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modify_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
//                if(selected.size() == 0){
//                    Toast.makeText(SelectCategoryActivity.this,"하나 이상의 카테고리를 선택하세요",Toast.LENGTH_SHORT).show();
//                    return true;
//                }

                Intent intent = new Intent(SelectCategoryActivity.this,ProfileActivity.class);
                intent.putIntegerArrayListExtra("Selected",selected);
                intent.putExtra("UserID",UserID);
                intent.putExtra("UserToken",UserToken);
                startActivity(intent);
                SelectCategoryActivity.this.finish();

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
