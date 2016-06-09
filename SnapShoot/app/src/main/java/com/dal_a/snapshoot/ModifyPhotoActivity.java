package com.dal_a.snapshoot;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dal_a.snapshoot.Http.HttpRequest;
import com.dal_a.snapshoot.view.FitImageView;
import com.facebook.Profile;

import java.io.File;
import java.io.IOException;

public class ModifyPhotoActivity extends AppCompatActivity {

    String path;
    boolean sendEnable = false;
    private int sigPosition;//낙관을 선택한 위치( 0 = leftTop, 1 = rightTop, 2 = leftBottom, 3 = rightBottom )

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putInt("sigPosition",sigPosition);
        outState.putBundle("save_data", bundle);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_photo);
        AndroidBug5497Workaround.assistActivity(this);

        if (savedInstanceState != null) {
            Bundle bundle = savedInstanceState.getBundle("save_data");
            path = bundle.getString("page");
            sigPosition = bundle.getInt("sigPosition");
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("공유 하기");
        getSupportActionBar().setSubtitle("낙관의 위치를 선택해주세요");

        if (Build.VERSION.SDK_INT >= 21 || Build.VERSION.SDK_INT < 19) {
            findViewById(R.id.statusbar).setVisibility(View.GONE);
        }

        path = getIntent().getExtras().getString("path");

        FitImageView image = (FitImageView) findViewById(R.id.image);
        Log.d("path", path);
        Glide.with(ModifyPhotoActivity.this)
                .load(path)
                .placeholder(R.drawable.loading)
                .into(image);

        Log.d("width", "" + image.getLayoutParams().width);

        //사용자 이름으로낙관 띄우기
        new setSignatureName().execute();

//        // 사진 업로드
//        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String title = ((EditText) ModifyPhotoActivity.this.findViewById(R.id.title)).getText().toString();
//                String text = ((EditText) ModifyPhotoActivity.this.findViewById(R.id.text)).getText().toString();
//                String[] params = {path, title, text};
//                if (title.equals("") || text.equals(""))//에러
//                    Toast.makeText(ModifyPhotoActivity.this, "사진 제목과 설명을 입력해주세요", Toast.LENGTH_SHORT).show();
//                else//정상업로드
//                    new UploadImageToServer().execute(params);
//            }
//        });

        final EditText titleText = (EditText) findViewById(R.id.title);

        titleText.addTextChangedListener(new TextWatcher() {
            @Override

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int l = titleText.getText().length();
                if (l > 5) {
                    getSupportActionBar().setSubtitle("감동의 순간을 표현해주세요");
                    sendEnable = false;
                    invalidateOptionsMenu();
                } else if (l == 0) {
                    getSupportActionBar().setSubtitle("짧은 제목을 달아주세요");
                    sendEnable = false;
                    invalidateOptionsMenu();
                } else {
                    getSupportActionBar().setSubtitle("조금만 더 길게요");
                    sendEnable = false;
                    invalidateOptionsMenu();
                }
            }
        });

        final EditText expression = (EditText) findViewById(R.id.text);
        expression.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int l = expression.getText().length();
                if (l > 5) {
                    getSupportActionBar().setSubtitle("오른쪽 버튼을 눌러 전시해보세요!");
                    sendEnable = true;
                    invalidateOptionsMenu();
                } else if (l == 0) {
                    getSupportActionBar().setSubtitle("감동의 순간을 표현해주세요");
                    sendEnable = false;
                    invalidateOptionsMenu();
                } else {
                    getSupportActionBar().setSubtitle("조금만 더 길게요");
                    sendEnable = false;
                    invalidateOptionsMenu();
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
    }


    //사진 업로드 클래스
    private class UploadImageToServer extends AsyncTask<String, Void, Void> {
        private String TAG = "UploadImageToServer";
        private ProgressDialog mDlg;
        private String title = "";
        private String text = "";
        private String currentUserFacebookID;

        @Override
        protected void onPreExecute() {
            mDlg = new ProgressDialog(ModifyPhotoActivity.this);
            mDlg.setMessage("사진을 업로드 하는 중입니다..");
            mDlg.setCanceledOnTouchOutside(false);
            mDlg.show();

            currentUserFacebookID = Profile.getCurrentProfile().getId();

            Log.d(TAG, title + " " + text + " " + currentUserFacebookID);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            //사진의 너비, 높이 구하기
            int[] widthHeight = getWidthHeight(params[0]);//[0] : width, [1] : height
            //낙관 위치 가져오기
            // 낙관을 선택한 위치( 0 = leftTop, 1 = rightTop, 2 = leftBottom, 3 = rightBottom )
            if(sigPosition < 0 || sigPosition > 3)
                sigPosition = 0;//default값 설정

            String uploadUrl = HttpRequest.get(Constant.MAIN_URL + "/uploadUrl").body();
            Log.d(TAG, "uploadUrl : " + uploadUrl);

            HttpRequest request = HttpRequest.post(uploadUrl);
            //public HttpRequest part(final String name, final String filename, final String contentType, final String part)
            request.part("image", "image", "multipart/form-data", new File(params[0]));
            request.part("userID", currentUserFacebookID);//facebook ID
            request.part("title", params[1]);//사진의 제목
            request.part("text", params[2]);//사진 설명
            request.part("width", widthHeight[0]);
            request.part("height", widthHeight[1]);
            request.part("sigPosition", sigPosition);
            if (request.ok()) {//전송
                Log.d(TAG, "Upload Succeed");//업로드 성공시
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void a) {
            mDlg.dismiss();
            Toast.makeText(getApplicationContext(), "사진 업로드가 완료되었습니다", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "사진 업로드 완료");
            Constant.uploadSuccess = true;
//            startActivity(new Intent(ModifyPhotoActivity.this, ListActivity.class));
            finish();
            int list = Constant.actList.size();
            for (int i = 0; i < list; i++) {
                Constant.actList.get(i).finish();
            }

            super.onPostExecute(a);
        }


        //화면 크기,사진 크기에 따라 Options.inSampleSize 값을 어떻게 해야하는지 알려주는 함수
        private int calculateInSampleSize(int width, int height, int reqWidth, int reqHeight) {
            //모든 사진에 대해서 width가 height보다 크다. 따라서 스마트폰 가로모드에서는 width, height 값을 바꿀 필요가 없다!
            if (reqWidth < reqHeight && width > height) {//스마트폰 세로모드에서, 가로 사진 로드시
                int temp = width;
                width = height;
                height = temp;
            }
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;


                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }


            return inSampleSize;
        }

        private int[] getWidthHeight(String path) {

            //사진 크기를 알기 위해 inJustDecodeBounds=true 를 설정한다
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int[] widthHeight = new int[2];

            int degree = GetExifOrientation(path);//사진의 방향을 알기 위한 함수
            if (degree == 90 || degree == 270) {//세로 사진
                widthHeight[0] = options.outHeight;
                widthHeight[1] = options.outWidth;
                Log.d(TAG,"세로사진 - Width : "+widthHeight[0]+" Height : "+widthHeight[1]);
            } else {//가로 사진
                widthHeight[0] = options.outWidth;
                widthHeight[1] = options.outHeight;
                Log.d(TAG,"가로 사진 - Width : "+widthHeight[0]+" Height : "+widthHeight[1]);
            }

            return widthHeight;//사진의 너비, 높이를 반환한다.
        }

        //상황에 따른 적절한 사진을 얻는다
        private Bitmap decodeSampledBitmapFromPath(String path, int reqWidth, int reqHeight) {

            //사진 크기를 알기 위해 inJustDecodeBounds=true 를 설정한다
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // inSampleSize를 계산한다
            options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, reqWidth, reqHeight);
            // 비트맵 생성 후 반환
            options.inJustDecodeBounds = false;
            final Bitmap beforeRotate = BitmapFactory.decodeFile(path, options);

            //사진 방향 파악
            int degree = GetExifOrientation(path);
            //회전된 비트맵 반환
            return GetRotatedBitmap(beforeRotate, degree);
        }

        //사진의 촬영 방향을 알아내는 함수
        private synchronized int GetExifOrientation(String filepath) {
            int degree = 0;
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(filepath);
            } catch (IOException e) {
                Log.e("CONSTANT", "exif를 읽을수 없음");
                e.printStackTrace();
            }
            if (exif != null) {
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
                if (orientation != -1) {
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            degree = 90;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            degree = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            degree = 270;
                            break;
                    }
                }
            }
            return degree;
        }

        //사진을 회전시키는 함수
        private synchronized Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees) {
            if (degrees != 0 && bitmap != null) {
                Matrix m = new Matrix();
                m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
                try {
                    Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                    if (bitmap != b2) {
                        bitmap.recycle();
                        bitmap = b2;
                    }
                } catch (OutOfMemoryError ex) {
                    // We have no memory to rotate. Return the original bitmap.
                }
            }
            return bitmap;
        }
    }

    //사진 업로드 클래스
    private class setSignatureName extends AsyncTask<Void, Void, String> {
        private String TAG = "getUserInfo";
        private String currentUserFacebookID = "0";

        @Override
        protected void onPreExecute() {
            currentUserFacebookID = Profile.getCurrentProfile().getId();

            Log.d(TAG, currentUserFacebookID);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String uploadUrl = HttpRequest.get(Constant.MAIN_URL + "/getUserInfo/"+currentUserFacebookID).body();
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
            TextView userName = (TextView)ModifyPhotoActivity.this.findViewById(R.id.userName);
            userName.setText(a);

            super.onPostExecute(a);
        }
    }

        public void clickImageToSign(View v) {
        View signature = findViewById(R.id.userName);
        findViewById(R.id.lefttop).setAlpha(0.5f);
        findViewById(R.id.righttop).setAlpha(0.5f);
        findViewById(R.id.leftbottom).setAlpha(0.5f);
        findViewById(R.id.rightbottom).setAlpha(0.5f);
        //낙관을 선택한 위치( 0 = leftTop, 1 = rightTop, 2 = leftBottom, 3 = rightBottom )
        switch (v.getId()) {
            case R.id.lefttop:
                v.setAlpha(1.0f);
                signature.setLayoutParams(new FrameLayout.LayoutParams(signature.getWidth(), signature.getHeight(), Gravity.LEFT | Gravity.TOP));
                sigPosition = 0;
                break;
            case R.id.righttop:
                v.setAlpha(1.0f);
                signature.setLayoutParams(new FrameLayout.LayoutParams(signature.getWidth(), signature.getHeight(), Gravity.RIGHT | Gravity.TOP));
                sigPosition = 1;
                break;
            case R.id.leftbottom:
                v.setAlpha(1.0f);
                signature.setLayoutParams(new FrameLayout.LayoutParams(signature.getWidth(), signature.getHeight(), Gravity.LEFT | Gravity.BOTTOM));
                sigPosition = 2;
                break;
            case R.id.rightbottom:
                v.setAlpha(1.0f);
                signature.setLayoutParams(new FrameLayout.LayoutParams(signature.getWidth(), signature.getHeight(), Gravity.RIGHT | Gravity.BOTTOM));
                sigPosition = 3;
                break;
        }
        getSupportActionBar().setSubtitle("짧은 제목을 달아주세요");
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
                String title = ((EditText) ModifyPhotoActivity.this.findViewById(R.id.title)).getText().toString();
                String text = ((EditText) ModifyPhotoActivity.this.findViewById(R.id.text)).getText().toString();
                String[] params = {path, title, text};
                if (title.equals("") || text.equals(""))//에러
                    Toast.makeText(ModifyPhotoActivity.this, "사진 제목과 설명을 입력해주세요", Toast.LENGTH_SHORT).show();
                else//정상업로드
                    new UploadImageToServer().execute(params);

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
