package com.dal_a.snapshoot;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;

import java.io.File;
import java.util.Date;

/**
 * Created by dh_st_000 on 2015-09-12.
 */

public class BroadcastListener extends BroadcastReceiver {
    private static String TAG = "BroadcastListener";
    public static String ACTION_RESTART_PERSISTENTSERVICE = "ACTION.Restart.SanpLockService";//서비스 재시작을 위해

    private static Context mContext;

    public BroadcastListener() {
    }

    public BroadcastListener(Context context) {
        mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //서비스 재시작을 위해 ---------------------------------------
        //앱이 종료되었거나 스마트폰이 재부팅 되었을 때
        if (action.equals(ACTION_RESTART_PERSISTENTSERVICE) ||
                action.equals(Intent.ACTION_BOOT_COMPLETED)) {

            //서비스를 시작하기 위한 새로운 인텐트
            Intent i = new Intent(context, LockScreenService.class);
            context.startService(i);//죽었던 서비스를 다시 시작한다
        }
        //화면이 꺼지면 락스크린을 띄워준다
        else if(action.equals(Intent.ACTION_SCREEN_OFF)){
            Intent i = new Intent(context, LockScreenActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
