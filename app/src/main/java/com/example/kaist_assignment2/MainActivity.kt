package com.example.kaist_assignment2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.kaist_assignment2.retrofit.ApiService
import com.example.kaist_assignment2.retrofit.User
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("http://172.10.7.133") // 서버의 기본 URL
            .addConverterFactory(GsonConverterFactory.create()) // Gson 변환기 사용
            .build()

        // intent로 userinfo 받아오기
        val userId = intent.getStringExtra("USER_ID")
        val userName = intent.getStringExtra("USER_NAME")

        // Retrofit 인터페이스 구현체 생성
        val apiService = retrofit.create(ApiService::class.java)

        // 사용자 데이터가 존재하는지 확인하는 메소드 호출
        if (!userId.isNullOrBlank()) {
            checkUserExistence(apiService, userId, userName)
        }

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)
        adapter = ViewPagerAdapter(this, userName)

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Tab ${(position + 1)}"
        }.attach()
    }

    private inner class ViewPagerAdapter(activity: AppCompatActivity, private val userName: String?) :
        FragmentStateAdapter(activity) {

        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> UserFragment.newInstance(userName)
                1 -> UserFragment.newInstance(userName)
                2 -> CalendarFragment.newInstance()
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }
    }

    // 사용자가 존재하는지 확인하는 메소드
    private fun checkUserExistence(apiService: ApiService, userId: String, userName: String?) {
        val call = apiService.getUser(userId)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        Toast.makeText(this@MainActivity, "User exists", Toast.LENGTH_SHORT).show()
                    } else {
                        // 사용자가 존재하지 않으면 데이터를 서버에 추가
                        if (!userName.isNullOrBlank()) {
                            insertDataToServer(apiService, userId, userName)
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MainActivity", "Error response: $errorBody")
                    Toast.makeText(this@MainActivity, "Failed to check user existence", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("MainActivity", "Failed to send GET request: ${t.message}")
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // user 데이터를 서버에 넣는 함수
    private fun insertDataToServer(apiService: ApiService, userId: String, userName: String) {
        val userData = User(userId, userName)
        val call = apiService.postUser(userData)

        // 비동기적으로 요청 실행
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "User Created successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MainActivity", "Error response: $errorBody")
                    Toast.makeText(this@MainActivity, "Failed to create user", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("MainActivity", "Failed to send POST request: ${t.message}")
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
