package com.example.na_sun.taskapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import io.realm.Realm;

public class TaskAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //       Log.d("taskapp","onReceive");
        //通知の設定
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);//落ちる
        builder.setSmallIcon(R.drawable.small_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.large_icon));
        builder.setWhen(System.currentTimeMillis());
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);

        //EXTRA_TASKからidを取得、インスタンスをとる
        int taskId = intent.getIntExtra(MainActivity.EXTRA_TASK,-1);
        Realm realm = Realm.getDefaultInstance();
        Task task = realm.where(Task.class).equalTo("id",taskId).findFirst();

        //タスクの情報を設定する
        builder.setTicker(task.getTitle());
        builder.setContentTitle(task.getTitle());
        builder.setContentText(task.getContents());

        //通知タップでアプリ起動
        Intent startAppIntent = new Intent(context,MainActivity.class);
        startAppIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,startAppIntent,0);
        builder.setContentIntent(pendingIntent);

        //通知の表示
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(task.getId(),builder.build());
        realm.close();




    }
}
