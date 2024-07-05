package com.example.kaist_assignment2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val kakaoLoginButton: Button = findViewById(R.id.kakao_login_button)
        kakaoLoginButton.setOnClickListener {
            val intent = Intent(this, AuthCodeHandlerActivity::class.java)
            startActivity(intent)
        }
    }
}
