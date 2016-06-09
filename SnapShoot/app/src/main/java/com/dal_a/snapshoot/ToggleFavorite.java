package com.dal_a.snapshoot;

import android.os.AsyncTask;
import android.util.Log;

import com.dal_a.snapshoot.Http.HttpRequest;
import com.facebook.Profile;

/**
 * Created by GA on 2015. 9. 19..
 */

//좋아요
class ToggleFavorite extends AsyncTask<Long, Void, Void> {
    private String TAG = "ToggleFavorite";
    //private ProgressDialog mDlg;
    private Long currentUserFacebookID;
    @Override
    protected void onPreExecute() {
        currentUserFacebookID = Long.parseLong(Profile.getCurrentProfile().getId());
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Long... params) {

        Log.d(TAG, "path : " + params[0]);

        HttpRequest request = HttpRequest.post(Constant.MAIN_URL + "/toggleFavorite");
        //public HttpRequest part(final String name, final String filename, final String contentType, final String part)
        request.part("UserID", currentUserFacebookID);
        request.part("ImageID", params[0]);
        if (request.ok()) {//전송
            Log.d(TAG, "AddLike Succeed");//업로드 성공시
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void a) {
        super.onPostExecute(a);
    }
}
