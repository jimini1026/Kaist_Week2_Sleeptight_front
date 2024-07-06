package com.example.kaist_assignment2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // 알람이 울릴 때 실행할 동작 정의 (여기서는 간단히 Toast를 이용한 예시)
        Toast.makeText(context, "일어나세요!!!", Toast.LENGTH_SHORT).show()
    }
}
