package com.example.kaist_assignment2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var splashLogo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashLogo = findViewById(R.id.splash_logo)

        val fadeOutDuration = 1500L // 애니메이션 지속 시간 설정 (1초)

        // 애니메이션을 직접 구현할 수도 있지만, 이 예제에서는 단순히 시간 지연을 통해 처리합니다.
        splashLogo.postDelayed({
            navigateToNextActivity()
        }, fadeOutDuration)
    }

    private fun navigateToNextActivity() {
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) // 페이드 인/아웃 효과
        finish() // 현재 액티비티 종료
    }
}
