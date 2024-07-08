package com.example.kaist_assignment2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private val REQUEST_CODE_AUTH = 100
    lateinit var userInfoText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val kakaoLoginButton: Button = findViewById(R.id.kakao_login_button)
        userInfoText = findViewById(R.id.user_info_text)

        kakaoLoginButton.setOnClickListener {
            val intent = Intent(this, AuthCodeHandlerActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_AUTH)
        }

        val internalLoginButton: Button = findViewById(R.id.internal_login_button)

        internalLoginButton.setOnClickListener {
            val intent = Intent(this, InternalLoginActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_AUTH)
        }
    }

    // 다른 액티비티에서 돌아온 결과를 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_AUTH && resultCode == Activity.RESULT_OK) {
            val userId = data?.getStringExtra("USER_ID")
            val userName = data?.getStringExtra("USER_NAME")

            if (!userId.isNullOrBlank()) {
                // Toast 메시지로도 정보 표시
                val toastMessage = "User ID: $userId\n User Name: $userName"
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()

                // MainActivity로 사용자 정보 전달
                val resultIntent = Intent(this, MainActivity::class.java)
                resultIntent.putExtra("USER_ID", userId)
                resultIntent.putExtra("USER_NAME", userName)
                startActivity(resultIntent)
                finish()
            } else {
                Toast.makeText(this, "No info", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
