package com.sec.test;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class JavaActivity extends AppCompatActivity {

    public static final String CHAT = "chat";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createChannel();
        showNotification();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = CHAT;
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_MAX;
            createNotificationChannel(channelId, channelName, importance);
        }
    }

    /**
     * 创建通知渠道
     *
     * @param channelId   渠道ID，需保证全局唯一性
     * @param channelName 渠道名称，是给用户看的，需要能够表达清楚这个渠道的用途
     * @param importance  重要等级，决定通知的不同行为
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    private void showNotification() {
        String senderStr = "设置通知栏标题";
        String contentStr = "设置通知栏内容";
        int pushId = 1;

        findViewById(R.id.button).setOnClickListener(view -> {
            // 判断
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = mNotificationManager.getNotificationChannel(CHAT);
                if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                    Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                    startActivity(intent);
                    Toast.makeText(this, "请手动将通知打开", Toast.LENGTH_SHORT).show();
                }
            }

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(JavaActivity.this, CHAT);
            Intent notificationIntent = new Intent(JavaActivity.this, JavaActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(JavaActivity.this, 0, notificationIntent, 0);
            mBuilder.setContentTitle(senderStr)//设置通知栏标题
                    .setContentText(contentStr)
                    .setContentIntent(intent) //设置通知栏单击意图
                    .setTicker(senderStr + ":" + contentStr) //通知首次出现在通知栏，带上升动画效果的
                    .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                    .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用 defaults 属性，可以组合
                    .setSmallIcon(R.mipmap.ic_launcher)//设置通知小 ICON
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));//设置通知小 ICON
            Notification notify = mBuilder.build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            mNotificationManager.notify(pushId, notify);
        });
    }

}
