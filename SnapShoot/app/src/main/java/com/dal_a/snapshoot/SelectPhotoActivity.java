package com.dal_a.snapshoot;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dal_a.snapshoot.card.LocalPhotoCard;
import com.dal_a.snapshoot.model.BaseCard;
import com.dal_a.snapshoot.model.BaseCardView;
import com.dal_a.snapshoot.view.MyRecyclerItemClickListener;
import com.dal_a.snapshoot.view.MyRecyclerView;
import com.dal_a.snapshoot.view.MyRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        Constant.actList.add(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("사진 선택");
        getSupportActionBar().setSubtitle("업로드 할 사진을 선택해주세요");

       if(Build.VERSION.SDK_INT >= 21 || Build.VERSION.SDK_INT < 19){
            findViewById(R.id.statusbar).setVisibility(View.GONE);
        }

        new SetImages().execute();
        MyRecyclerView recyclerView = (MyRecyclerView)findViewById(R.id.recyclerview);
        setupRecyclerView(recyclerView);
        recyclerView.addOnItemTouchListener(new MyRecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(BaseCardView view, int position) {
                Log.d("position", String.valueOf(view.getTag()) + "");
                Intent intent = new Intent(SelectPhotoActivity.this, ModifyPhotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("path", String.valueOf(view.getTag()));
                intent.putExtras(bundle);
                startActivity(intent, bundle);
            }

            @Override
            public void onItemLongClick(BaseCardView view, int position) {
                Log.d("position", position + "");
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        int list = Constant.actList.size();
        Log.d("asdasda", "adssada");
        for(int i = 0; i < list; i++){
            Constant.actList.remove(i);
        }

    }



    private void setupRecyclerView(MyRecyclerView recyclerView) {

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new MyRecyclerViewAdapter(this));
    }



    private void setCards(List<BaseCard> cards) {
        MyRecyclerView recyclerView = (MyRecyclerView)findViewById(R.id.recyclerview);
        ((MyRecyclerViewAdapter)(recyclerView.getAdapter())).addAllCards(cards);
    }


    private class SetImages extends AsyncTask<Void, Void, List<BaseCard>> {

        Cursor cursor;
        @Override
        protected List<BaseCard> doInBackground(Void... params) {
            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Images.ImageColumns.DATE_MODIFIED + " ASC");
            ArrayList<BaseCard> cards = new ArrayList<>();

            if(cursor.moveToLast()) {
                do{
                    LocalPhotoCard card = new LocalPhotoCard(getApplicationContext());
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                    card.setTag(path);
                    card.setImage(path);
                    String paths[] = path.split("\\/");
                    card.setTitle(paths[paths.length -2]);
                    cards.add(card);
                }while (cursor.moveToPrevious());
            }

            cursor.close();

            return cards;
        }

        @Override
        protected void onPostExecute(List<BaseCard> result){
            setCards(result);
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
