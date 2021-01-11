package com.sec.test

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder

class MainActivity : AppCompatActivity() {

    companion object {
        const val CHAT_CHANNEL = "chat"
        const val CHAT_CHANNEL_NAME = "聊天消息"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel(
            CHAT_CHANNEL,
            CHAT_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        findViewById<AppCompatButton>(R.id.button).setOnClickListener {
            sendNotification()
        }
    }

    private fun sendNotification() {
        val resultIntent = Intent(applicationContext, MainActivity::class.java)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val senderStr = "设置通知栏标题"
        val contentStr = "设置通知栏内容"
        val pushId = 1

        val builder = NotificationCompat.Builder(this, CHAT_CHANNEL)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(senderStr)
            .setContentText(contentStr)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Much longer text that cannot fit one line,Much longer text that cannot fit one line,Much longer text that cannot fit one line...")
            )
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
        NotificationManagerCompat.from(this).notify(pushId, builder.build())
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) {
        val channel = NotificationChannel(channelId, channelName, importance)
        NotificationManagerCompat.from(this).createNotificationChannel(channel)
    }

}