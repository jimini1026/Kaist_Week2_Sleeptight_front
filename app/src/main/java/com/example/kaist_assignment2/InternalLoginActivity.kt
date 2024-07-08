package com.example.kaist_assignment2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InternalLoginActivity : AppCompatActivity() {

    private lateinit var userIdEditText: EditText
    private lateinit var userNameEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_internal_login)

        userIdEditText = findViewById(R.id.user_id_edit_text)
        userNameEditText = findViewById(R.id.user_name_edit_text)
        val loginButton: Button = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val userId = userIdEditText.text.toString()
            val userName = userNameEditText.text.toString()

            // Toast 메시지로 userId와 userName 출력
            val toastMessage = "User ID: $userId User Name: $userName"
            Toast.makeText(this@InternalLoginActivity, toastMessage, Toast.LENGTH_SHORT).show()

            // 여기서 userId와 userName을 사용하여 로그인 처리를 수행합니다.
            // 예를 들어, 로그인이 성공했다고 가정하고 MainActivity로 데이터를 전달합니다.
            // MainActivity로 전환
            val resultIntent = Intent(this@InternalLoginActivity, MainActivity::class.java).apply {
                putExtra("USER_ID", userId)
                putExtra("USER_NAME", userName)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish() // 현재 액티비티를 종료합니다.
        }
    }
}
