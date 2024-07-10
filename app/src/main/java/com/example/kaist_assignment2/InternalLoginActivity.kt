package com.example.kaist_assignment2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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



            // 여기서 userId와 userName을 사용하여 로그인 처리를 수행합니다.
            // 예를 들어, 로그인이 성공했다고 가정하고 MainActivity로 데이터를 전달합니다.
            // MainActivity로 전환
            val resultIntent = Intent(this@InternalLoginActivity, MainActivity::class.java).apply {
                putExtra("USER_ID", userId)
                putExtra("USER_NAME", userName)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish() // 현재 액티비티를 종료합니다.
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 여기에 뒤로 가기 버튼을 눌렀을 때 실행할 코드를 작성합니다.
                // 예를 들어, 애니메이션을 추가하거나 원하는 동작을 수행할 수 있습니다.
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

}
