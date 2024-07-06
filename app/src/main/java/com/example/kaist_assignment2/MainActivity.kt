package com.example.kaist_assignment2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userInfoText: TextView = findViewById(R.id.user_info_text)

        // Intent로 전달받은 데이터를 TextView에 표시
        val userId = intent.getStringExtra("USER_ID")
        val userName = intent.getStringExtra("USER_NAME")

        if (!userId.isNullOrBlank()) {
            val userInfo = "User ID: $userId\n User Name: $userName"
            userInfoText.text = userInfo
        }
    }
}