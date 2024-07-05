package com.example.kaist_assignment2
import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao Sdk 초기화
        KakaoSdk.init(this, "8a3cbb6c2264d32bc8d795ac5bff0eb9")
    }
}