// AlarmActivity.kt
package com.example.kaist_assignment2

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        // Activity에서 설정한 레이아웃의 뷰들을 초기화할 수 있음
    }

    // "지금 일어날게요" 버튼 클릭 시 호출될 메서드
    fun onWakeUpClicked(view: android.view.View) {
        Toast.makeText(this, "지금 일어날게요 버튼 클릭", Toast.LENGTH_SHORT).show()
        // MainActivity로 돌아가는 코드
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // "5분만 더 잘게요" 버튼 클릭 시 호출될 메서드
    fun onSleepFiveMoreClicked(view: android.view.View) {
        Toast.makeText(this, "5분만 더 잘래요 버튼 클릭", Toast.LENGTH_SHORT).show()
        // 5분 뒤에 알람 설정하고 MainActivity로 돌아가는 코드
        setAlarmAfterFiveMinutes()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setAlarmAfterFiveMinutes() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // FLAG_IMMUTABLE 추가
        )

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 5)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}
