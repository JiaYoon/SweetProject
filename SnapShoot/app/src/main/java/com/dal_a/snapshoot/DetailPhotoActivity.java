package com.dal_a.snapshoot;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dal_a.snapshoot.Http.HttpRequest;
import com.dal_a.snapshoot.view.TouchImageView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

public class DetailPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_detail_photo);


        Bundle bundle = getIntent().getExtras();

        final String path = bundle.getString("path");
        final Long id = bundle.getLong("id");
        final String userID = bundle.getString("UserID");
        final String Time = bundle.getString("Time");
        final String Favorite = Integer.toString(bundle.getInt("Favorite", 0));
        final String Title = bundle.getString("Title", "제목이 없습니다");
        final String Text = bundle.getString("Text","설명이 없습니다");
        final String MyFavorite = bundle.getString("MyFavorite", "False"); //내가 좋아요 한 사진이면 True, 아니면 False
        final String CommentsNum = Integer.toString(bundle.getInt("CommentsNum", 0));
        final String Width = Integer.toString(bundle.getInt("Width", 500));
        final String Height = Integer.toString(bundle.getInt("Height", 500));
        final String SigPosition = Integer.toString(bundle.getInt("SigPosition", 1));
        String[] imageData = {path,id.toString(),userID,Time,Favorite,Title,Text,MyFavorite,CommentsNum,Width,Height,SigPosition};


        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);

        final boolean[] isPanelExpanded = {false};

        final TouchImageView image = (TouchImageView)findViewById(R.id.img);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        final SlidingUpPanelLayout panel = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        final TextView detail = (TextView)findViewById(R.id.detail);
        final TextView comment = (TextView)findViewById(R.id.comments);
        final FloatingActionButton like = (FloatingActionButton) findViewById(R.id.like);

        setupViewPager(viewPager, imageData);

        Glide.with(this)
                .load(path+"/1500/1500")
                .thumbnail(0.5f)
                .placeholder(R.drawable.loading)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        setSignaturePosition(Integer.parseInt(SigPosition));
                        return false;
                    }
                })
                .into(image);

        detail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewPager.setCurrentItem(0);
                if (isPanelExpanded[0]) return true;
                else return false;
            }

        });
        new setSignatureName().execute(userID);
        comment.setText(CommentsNum+"개의 댓글");
        comment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewPager.setCurrentItem(1);
                if (isPanelExpanded[0]) return true;
                else return false;
            }

        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    comment.setTypeface(null, Typeface.NORMAL);
                    detail.setTypeface(null, Typeface.BOLD);
                } else {
                    detail.setTypeface(null, Typeface.NORMAL);
                    comment.setTypeface(null, Typeface.BOLD);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if(MyFavorite.equals("True")){
            like.setIcon(R.mipmap.heartfull);
            like.setColorNormal(getResources().getColor(R.color.md_purple_400));
            like.setSelected(true);
        }else{
            like.setIcon(R.mipmap.heart);
            like.setColorNormal(getResources().getColor(R.color.md_blue_grey_500_75));
            like.setSelected(false);
        }
        //좋아요
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(Constant.PREFS_NAME, 0);
                if (settings.getBoolean(Constant.PREFS_LOGIN_BOOLEAN, false)) {
                    new ToggleFavorite().execute(id);
                    if (v.isSelected()) {
                        ((FloatingActionButton) v).setIcon(R.mipmap.heart);
                        ((FloatingActionButton) v).setColorNormal(getResources().getColor(R.color.md_blue_grey_500_75));
                        v.setSelected(false);
                    } else {
                        ((FloatingActionButton) v).setIcon(R.mipmap.heartfull);
                        ((FloatingActionButton) v).setColorNormal(getResources().getColor(R.color.md_purple_400));
                        v.setSelected(true);
                    }
                } else {

                }
            }
        });

        panel.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                if (like.getVisibility() == View.INVISIBLE)
                    like.setVisibility(View.VISIBLE);

                like.setAlpha(1.0f - v);
            }

            @Override
            public void onPanelCollapsed(View view) {
                like.setVisibility(View.VISIBLE);
                detail.setTypeface(null, Typeface.NORMAL);
                comment.setTypeface(null, Typeface.NORMAL);
                isPanelExpanded[0] = false;
            }

            @Override
            public void onPanelExpanded(View view) {
                isPanelExpanded[0] = true;
                like.setVisibility(View.INVISIBLE);
                if (viewPager.getCurrentItem() == 0) {
                    comment.setTypeface(null, Typeface.NORMAL);
                    detail.setTypeface(null, Typeface.BOLD);
                } else {
                    detail.setTypeface(null, Typeface.NORMAL);
                    comment.setTypeface(null, Typeface.BOLD);

                }

            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });


        TextView userName = (TextView)findViewById(R.id.userName);
//        userName.setLayoutParams();
        FrameLayout.LayoutParams params;
        switch (Integer.parseInt(SigPosition)){
            case 0:
                params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.TOP|Gravity.LEFT;
                userName.setLayoutParams(params);
                break;
            case 1:
                 params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.TOP|Gravity.RIGHT;
                userName.setLayoutParams(params);
                break;
            case 2:
                 params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.BOTTOM|Gravity.LEFT;
                userName.setLayoutParams(params);
                break;
            case 3:
                 params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.BOTTOM|Gravity.RIGHT;
                userName.setLayoutParams(params);
                break;
        }

        image.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {
                setSignaturePosition(Integer.parseInt(SigPosition));
                like.setAlpha((1 - (image.getCurrentZoom()-1)/(image.getMaxZoom()-1)));
            }
        });
    }


    private Adapter setupViewPager(ViewPager viewPager, String[] imageData) {
        //넘어오는 데이터 String[] imageData = {path,id,UserID,Time,Favorite,Title,Text,MyFavorite};
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(InformationFragment.newInstance(imageData));
        adapter.addFragment(CommentFragment.newInstance(imageData[1]));

        viewPager.setAdapter(adapter);
        return adapter;

    }
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }


    //사진 업로드 클래스
    private class setSignatureName extends AsyncTask<String, Void, String> {
        private String TAG = "getUserInfo";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String uploadUrl = HttpRequest.get(Constant.MAIN_URL + "/getUserInfo/" + params[0]).body();
            if(!uploadUrl.equals("No Data")){
                String[] detail = uploadUrl.split("\\*\\*");
                //username == detail[0]
                //userintroduction == detail[1]
                //category == detail[2]
                //follower == detail[3] 팔로워의 수
                return detail[0];
            }

            return "";
        }

        @Override
        protected void onPostExecute(String a) {
            //낙관으로 사용자 이름 띄우기
            TextView userName = (TextView)DetailPhotoActivity.this.findViewById(R.id.userName);
            userName.setText(a);

            super.onPostExecute(a);
        }
    }

    void setSignaturePosition(int SigPosition){
        float redundanty, redundantx;
        TouchImageView image = (TouchImageView)findViewById(R.id.img);
        //낙관을 선택한 위치( 0 = leftTop, 1 = rightTop, 2 = leftBottom, 3 = rightBottom )
        if(image.getZoomedRect().height() >= 1.0){
            // 낙관이 움직여야함
            //top + bottom -
            //bottom       findViewById(R.id.userName).setTranslationY(redundanty <240? -120 : (-redundanty / 2));
            switch (SigPosition){
                case 0:
                case 1:
                    redundanty = image.getHeight() - image.getImageHeight();
                    findViewById(R.id.userName).setTranslationY(redundanty / 2);
                    break;
                case 2:
                case 3:
                    //bottom
                    redundanty = image.getHeight() - image.getImageHeight();
                    findViewById(R.id.userName).setTranslationY(redundanty <240? -120 : (-redundanty / 2));
                    break;
            }
        }

        if(image.getZoomedRect().width() >= 1.0) {
            // 낙관이 움직여야함
            //left + right -
            switch (SigPosition) {
                case 0:
                case 2:
                    redundantx = image.getWidth() - image.getImageWidth();
                    findViewById(R.id.userName).setTranslationX(redundantx / 2);
                    break;
                case 1:
                case 3:
                    //bottom
                    redundantx = image.getWidth() - image.getImageWidth();
                    findViewById(R.id.userName).setTranslationX(-redundantx / 2);
                    break;
            }
        }
    }
}
