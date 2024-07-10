package com.example.kaist_assignment2

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kaist_assignment2.retrofit.ApiService
import com.example.kaist_assignment2.retrofit.RetrofitClient
import com.example.kaist_assignment2.retrofit.UserSleepData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AlarmActivity : AppCompatActivity() {

    private var userId: String? = null
    private var currentDateStr: String? = null
    private var sleepTime: String? = null
    private var selectedHour: Int = -1
    private var selectedMinute: Int = -1
    private var wakeHour: Int = -1
    private var wakeMinute: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        userId = intent.getStringExtra("userId")
        currentDateStr = intent.getStringExtra("currentDate")
        sleepTime = intent.getStringExtra("sleepTime")
        selectedHour = intent.getIntExtra("selectedHour", -1)
        selectedMinute = intent.getIntExtra("selectedMinute", -1)
        wakeHour = selectedHour
        wakeMinute = selectedMinute

        // Activity에서 설정한 레이아웃의 뷰들을 초기화할 수 있음
    }

    fun onWakeUpClicked(view: android.view.View) {
        if (wakeHour != -1 && wakeMinute != -1) {
            // Insert the sleep data into the database
            insertSleepData()
        } else {
            // Return to MainActivity immediately if wake time is not set
            returnToMainActivity()
        }
    }

    private fun insertSleepData() {
        // 현재 날짜와 시간을 포함한 ISO 8601 형식으로 변환
        val dateFormat_fordate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat_fordate.parse(currentDateStr)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        val predWakeTime = String.format("%02d:%02d", selectedHour, selectedMinute)
        val realWakeTime = String.format("%02d:%02d", wakeHour, wakeMinute)

        val sleepTimeFormatted = "${dateFormat.format(currentDate).substring(0, 11)}$sleepTime:00Z"
        val predWakeTimeFormatted = "${dateFormat.format(currentDate).substring(0, 11)}$predWakeTime:00Z"
        val realWakeTimeFormatted = "${dateFormat.format(currentDate).substring(0, 11)}$realWakeTime:00Z"


        val sleepData = UserSleepData(
            userId = userId ?: "",
            date = currentDate,
            sleepTime = sleepTimeFormatted,
            predWakeTime = predWakeTimeFormatted,
            realWakeTime = realWakeTimeFormatted
        )


        val apiService = RetrofitClient.apiService


        apiService.postSleepData(sleepData).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                } else {
                    Toast.makeText(this@AlarmActivity, "Failed to insert sleep data of $userId", Toast.LENGTH_SHORT).show()
                    Log.e("PostSleepData", "Failed to insert sleep data: ${response.code()} ${response.message()}")
                }
                returnToMainActivity()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AlarmActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("PostSleepData", "Error: ${t.message}", t)
                returnToMainActivity()
            }
        })

    }

    private fun returnToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun onSleepFiveMoreClicked(view: android.view.View) {

        // 현재 시간에 5분을 더하고 selectedHour와 selectedMinute 변수 업데이트
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 5)
        wakeHour = calendar.get(Calendar.HOUR_OF_DAY)
        wakeMinute = calendar.get(Calendar.MINUTE)

        if (wakeHour != -1 && wakeMinute != -1) {
            val wakeTime = String.format("%02d:%02d", wakeHour, wakeMinute)
            Toast.makeText(this, "SleepTime: $sleepTime Selected Time: $wakeTime", Toast.LENGTH_LONG).show()
        }
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
