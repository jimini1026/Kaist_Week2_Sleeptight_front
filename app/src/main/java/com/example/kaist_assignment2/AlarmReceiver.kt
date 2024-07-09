package com.example.kaist_assignment2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val userId = intent.getStringExtra("userId")
        val currentDate = intent.getStringExtra("currentDate")
        val sleepTime = intent.getStringExtra("sleepTime")
        val selectedHour = intent.getIntExtra("selectedHour", -1)
        val selectedMinute = intent.getIntExtra("selectedMinute", -1)

        showNotification(context, "일어나세요!!!", userId, currentDate, sleepTime, selectedHour, selectedMinute)
    }

    private fun showNotification(context: Context, message: String, userId: String?, currentDate: String?, sleepTime: String?, selectedHour: Int, selectedMinute: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification Channel 생성 (Android 8.0 이상에서 필요)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "alarm_channel"
            val channelName = "Alarm Channel"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림을 클릭했을 때 이동할 액티비티 설정
        val intent = Intent(context, AlarmActivity::class.java).apply {
            putExtra("userId", userId)
            putExtra("currentDate", currentDate)
            putExtra("sleepTime", sleepTime)
            putExtra("selectedHour", selectedHour)
            putExtra("selectedMinute", selectedMinute)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT // FLAG_IMMUTABLE 추가
        )

        // Notification Builder 생성
        val notificationBuilder = NotificationCompat.Builder(context, "alarm_channel")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Sleeptight Alarm Notification")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        // Notification 표시
        notificationManager.notify(1, notificationBuilder.build())
    }
}
