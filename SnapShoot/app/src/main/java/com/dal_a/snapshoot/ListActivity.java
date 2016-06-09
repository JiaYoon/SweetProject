package com.dal_a.snapshoot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.dal_a.snapshoot.Http.HttpRequest;
import com.dal_a.snapshoot.view.RippleDrawable;
import com.dal_a.snapshoot.view.TouchHandlingViewPager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private final String TAG = "ListActivity";
    private CallbackManager callbackManager;
    //now left right
    int page = 12;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Bundle bundle = new Bundle();
        bundle.putInt("page", page);

        outState.putBundle("save_data", bundle);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Facebook -------------------------------------------------
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.dal_a.snapshoot",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        callbackManager = CallbackManager.Factory.create();

        //-----------------------------------------------------------
        setContentView(R.layout.activity_list);

        if (savedInstanceState != null) {
            Bundle bundle = savedInstanceState.getBundle("save_data");
            page = bundle.getInt("page", 12);
        }


        //Statusbar 설정
        final View statusbar = findViewById(R.id.statusbar);
        if (Build.VERSION.SDK_INT < 19) {
            statusbar.setVisibility(View.GONE);
        }
        //Viewpager 설정
        final TouchHandlingViewPager viewPager = (TouchHandlingViewPager) findViewById(R.id.pager);
        final Adapter adapter = setupViewPager(viewPager);

        //Toolbar 설정
        // Title를 설정해준다
        // 배경을 설정해준다
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.drawer2);
        ab.setDisplayHomeAsUpEnabled(true);

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                switch (menuItem.getItemId()){
//                    case R.id.nav_home:
//
//                        break;
//                }
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });


        //left, right 버튼 설정
        // Title를 설정해준다
        TextView left = ((TextView) findViewById(R.id.left));
        TextView right = ((TextView) findViewById(R.id.right));
        final TextView title =(TextView)findViewById(R.id.title);

        setupPageDesign(adapter, toolbar, statusbar, left, right, title);

        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // 버튼을 누른다면, 누른 버튼의 컬러로 바꿔줘야됨. 백그라운드 컬러는 알아서 바뀜
                    // 또한 text를 바꿔야함(toolbar랑 누른 버튼이랑 서로 교환)
                    page = ((page % 100) / 10) * 100 + (page / 100) * 10 + page % 10;
                    toolbar.setTitle(adapter.getPageTitle(page / 100));
                    ((RippleDrawable) toolbar.getBackground())
                            .setColor(adapter.getPrimaryColor(page / 100))
                            .start(event.getRawX(), event.getRawY());

                    ((RippleDrawable) statusbar.getBackground())
                            .setColor(adapter.getPrimaryDarkColor(page / 100))
                            .start(event.getRawX(), event.getRawY());

                    ((TextView) v).setText(adapter.getPageTitle((page % 100) / 10));
                    viewPager.setCurrentItem(page / 100);
                }
                return true;
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    page = (page % 10) * 100 + ((page % 100) / 10) * 10 + page / 100;
                    toolbar.setTitle(adapter.getPageTitle(page / 100));
                    ((RippleDrawable) toolbar.getBackground())
                            .setColor(adapter.getPrimaryColor(page / 100))
                            .start(event.getRawX(), event.getRawY());

                    ((RippleDrawable) statusbar.getBackground())
                            .setColor(adapter.getPrimaryDarkColor(page / 100))
                            .start(event.getRawX(), event.getRawY());

                    ((TextView) v).setText(adapter.getPageTitle(page % 10));
                    viewPager.setCurrentItem(page / 100);
                }
                return true;
            }
        });

        final FloatingActionsMenu addButton = (FloatingActionsMenu)findViewById(R.id.add);
        addButton.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                /*********** 로그인 확인 ************/
                SharedPreferences settings = getSharedPreferences(Constant.PREFS_NAME, 0);
                boolean socialLogin = settings.getBoolean(Constant.PREFS_LOGIN_BOOLEAN, false);
                /*********** 로그인 확인 ************/

//                boolean loggedIn = false;
//                if (AccessToken.getCurrentAccessToken() != null)
//                    loggedIn = true;
                if (socialLogin) {//페이스북 로그인이 되어있을 때
                    Log.d(TAG, "Facebook Login");
                    Intent intent = new Intent(ListActivity.this, SelectPhotoActivity.class);
                    startActivity(intent);
                    addButton.collapse();
                } else {//페이스북 로그인이 되어 있지 않을 때
                    Log.d(TAG, "Facebook Logout");
                    findViewById(R.id.untouchable).setVisibility(View.VISIBLE);//로그인 버튼 등장
                }
                Constant.clickedCard.clear();
            }

            @Override
            public void onMenuCollapsed() {
                findViewById(R.id.untouchable).setVisibility(View.INVISIBLE);

            }
        });

        findViewById(R.id.untouchable).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setVisibility(View.INVISIBLE);
                addButton.collapse();
                return true;
            }

        });

        //facebook Login Button
        final LoginButton loginButton = (LoginButton)ListActivity.this.findViewById(R.id.facebookLogin);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d(TAG, "Facebook Login Succeess");

                String UserID = loginResult.getAccessToken().getUserId();
                String UserToken = loginResult.getAccessToken().getToken();
                //카테고리 선택 액티비티로 연결한다
                Intent intent = new Intent(ListActivity.this, SelectCategoryActivity.class);
                intent.putExtra("UserID", UserID);
                intent.putExtra("UserToken", UserToken);
                startActivity(intent);
                findViewById(R.id.untouchable).setVisibility(View.INVISIBLE);//로그인 버튼을 없애버린다
                addButton.collapse();

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Facebook Login Canceled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d(TAG, "Facebook Login Error");
            }
        });

        FloatingActionButton button1 = (FloatingActionButton) findViewById(R.id.facebook);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();//Facebook LoginButton이 눌린것 같은 효과를 낸다
            }
        });
//        FloatingActionButton button2 = (FloatingActionButton) findViewById(R.id.facebookLogout);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG,"Facebook Logout button clicked");
//                //테스트를 위한 로그아웃 버튼
//                LoginManager mLoginManager = LoginManager.getInstance();
//                mLoginManager.logOut();
//
//                SharedPreferences settings = getSharedPreferences(Constant.PREFS_NAME, 0);
//                SharedPreferences.Editor editor = settings.edit();
//                editor.putBoolean(Constant.PREFS_LOGIN_BOOLEAN, false);
//
//            }
//        });

    }


    // 초기엔, 백그라운드 컬러만 있으면 됨
    // 그냥 컬러는 필요없음
    private void setupPageDesign(Adapter adapter, Toolbar toolbar, View statusbar, TextView left, TextView right, TextView title) {

        getSupportActionBar().setTitle(adapter.getPageTitle(page / 100));
        statusbar.setBackground(new RippleDrawable(getResources().getColor(R.color.md_blue_grey_900)));
        toolbar.setBackground(new RippleDrawable(getResources().getColor(R.color.md_blue_grey_800)));

//        title.setText(adapter.getPageTitle(page/100));
        left.setText(adapter.getPageTitle((page % 100) / 10));
//        left.setTag(1);
        right.setText(adapter.getPageTitle(page % 10));
//        right.setTag(2);
    }


    public void updateViewPager(){
        ((LikeFragment)((Adapter)((ViewPager)findViewById(R.id.pager)).getAdapter()).getItem(1)).setAtFirst();
    }


    private Adapter setupViewPager(TouchHandlingViewPager viewPager) {
        viewPager.setPagingDisabled();
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(NewsFragment.newInstance(), "News", getResources().getColor(R.color.md_purple_300), getResources().getColor(R.color.md_purple_500));
        adapter.addFragment(LikeFragment.newInstance(), "Like", getResources().getColor(R.color.md_blue_300), getResources().getColor(R.color.md_blue_500));
        adapter.addFragment(MineFragment.newInstance(), "Mine", getResources().getColor(R.color.md_orange_300), getResources().getColor(R.color.md_orange_500));

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        return adapter;

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();
        private final List<Integer> mFragmentPrimary = new ArrayList<>();
        private final List<Integer> mFragmentPrimaryDark = new ArrayList<>();


        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title, int primary, int primarydark) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
            mFragmentPrimary.add(primary);
            mFragmentPrimaryDark.add(primarydark);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

        public int getPrimaryColor(int position) {
            return mFragmentPrimary.get(position);
        }

        public int getPrimaryDarkColor(int position) {
            return mFragmentPrimaryDark.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((DrawerLayout) findViewById(R.id.drawer_layout)).openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



    //카테고리를 추가하기 위한 임시 클래스 -> 나중에 관리자 페이지로 대체할 예정
    class AddCategory extends AsyncTask<Void, Void, Void> {
        private String TAG = "ToggleFavorite";
        //private ProgressDialog mDlg;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpRequest request = HttpRequest.post(Constant.MAIN_URL + "/updateCategory");
            //public HttpRequest part(final String name, final String filename, final String contentType, final String part)
            request.part("Index", 0);
            request.part("Name","가");
            request.part("TestImageURL","http://yeji.esy.es/%EC%82%B0.jpg");
            if (request.ok()) {//전송
                Log.d(TAG, "AddCategory Succeed");//업로드 성공시
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void a) {
            super.onPostExecute(a);
        }
    }

}
