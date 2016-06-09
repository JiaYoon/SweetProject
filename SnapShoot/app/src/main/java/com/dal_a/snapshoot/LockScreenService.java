package com.dal_a.snapshoot;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Parcel;
import android.os.SystemClock;

public class LockScreenService extends Service {

    BroadcastListener broadcastListener = null;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);

        //노티피에서 실행 중이라는 것을 알려줄 경우 죽지 않음(고의적인 경우 제외)
        Notification notification = new Notification(R.drawable.loading, "서비스 실행됨", System.currentTimeMillis());
        notification.setLatestEventInfo(getApplicationContext(), "Screen Service", "Foreground로 실행됨", null);
        notification.priority = Notification.PRIORITY_MIN;
        startForeground(1, notification);

        unregisterRestartService();//죽지 않는 서비스

        //Notification을 위한 브로드캐스트 리시버
        if (broadcastListener == null)
            broadcastListener = new BroadcastListener(getApplicationContext());


        registerReceiver(broadcastListener, new IntentFilter(Intent.ACTION_SCREEN_OFF));//리시버 등록

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(broadcastListener);

        registerRestartService();//서비스를 다시 살리기 위한 함수
    }


    /**
     * ----------서비스가 죽었을 때 다시 살리기 위한 함수들---------- *
     */
    public void registerRestartService() {
        Intent intent = new Intent(LockScreenService.this, BroadcastListener.class);
        intent.setAction(BroadcastListener.ACTION_RESTART_PERSISTENTSERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(
                LockScreenService.this, 0, intent, 0);
        long currentTime = SystemClock.elapsedRealtime();//현재 시간
        currentTime += 1 * 1000;
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME, currentTime, 60 * 1000, sender);
    }

    public void unregisterRestartService() {
        Intent intent = new Intent(LockScreenService.this, BroadcastListener.class);
        intent.setAction(BroadcastListener.ACTION_RESTART_PERSISTENTSERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(
                LockScreenService.this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }


}
