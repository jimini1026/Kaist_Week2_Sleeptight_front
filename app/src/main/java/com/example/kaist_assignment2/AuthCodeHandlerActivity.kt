package com.example.kaist_assignment2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class AuthCodeHandlerActivity : AppCompatActivity() {

    private val TAG = "AuthCodeHandlerActivity"

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "로그인 실패 $error")
        } else if (token != null) {
            Log.e(TAG, "로그인 성공 ${token.accessToken}")

            // 사용자 정보 가져오기
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e(TAG, "사용자 정보 요청 실패", error)
                } else if (user != null) {
                    val userId = "kakao_" + user.id.toString()
                    val userName = user.kakaoAccount?.profile?.nickname

                    // MainActivity로 전환
                    val resultIntent = Intent(this@AuthCodeHandlerActivity, MainActivity::class.java).apply {
                        putExtra("USER_ID", userId)
                        putExtra("USER_NAME", userName)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish() // 현재 액티비티를 종료
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 카카오톡 설치 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            // 카카오톡 로그인
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                // 로그인 실패 부분
                if (error != null) {
                    Log.e(TAG, "로그인 실패 $error")
                    // 사용자가 취소
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    // 다른 오류
                    else {
                        UserApiClient.instance.loginWithKakaoAccount(
                            this,
                            callback = mCallback
                        ) // 카카오 이메일 로그인
                    }
                }
                // 로그인 성공 부분
                else if (token != null) {
                    Log.e(TAG, "로그인 성공 ${token.accessToken}")
                    // 사용자 정보 요청 및 MainActivity로 전환
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e(TAG, "사용자 정보 요청 실패", error)
                        } else if (user != null) {
                            val userId = "kakao_" + user.id.toString()
                            val userName = user.kakaoAccount?.profile?.nickname


                            // LoginActivity로 전환
                            val intent = Intent(this, LoginActivity::class.java).apply {
                                putExtra("USER_ID", userId)
                                putExtra("USER_NAME", userName)
                            }
                            setResult(Activity.RESULT_OK, intent)
                            finish() // 현재 액티비티를 종료
                        }
                    }
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback) // 카카오 이메일 로그인
        }
    }
}
