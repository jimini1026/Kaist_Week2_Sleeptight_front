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

        // Retrofit 인터페이스 구현체 생성
        val apiService = retrofit.create(ApiService::class.java)

        // 서버에서 데이터를 가져오는 요청
        val call = apiService.getUsers()

        // 비동기적으로 요청 실행
        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body() // 서버에서 받아온 User 리스트

                    // 사용자 데이터 처리 예시: 첫 번째 사용자의 이름 출력
                    val firstUserName = users?.get(0)?.user_name ?: "No users found"
                    Toast.makeText(this@MainActivity, "First user name: $firstUserName", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MainActivity", "Error response: $errorBody")
                    Toast.makeText(this@MainActivity, "Failed to get users", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("MainActivity", "Failed to fetch data: ${t.message}")
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        val userId = intent.getStringExtra("USER_ID")
        val userName = intent.getStringExtra("USER_NAME")

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
                2 -> UserFragment.newInstance(userName)
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }
    }
}
