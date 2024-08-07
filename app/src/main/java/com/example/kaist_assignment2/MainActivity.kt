package com.example.kaist_assignment2

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.kaist_assignment2.retrofit.ApiService
import com.example.kaist_assignment2.retrofit.RetrofitClient
import com.example.kaist_assignment2.retrofit.User
import com.example.kaist_assignment2.retrofit.UserSongsData
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
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerProfile: LinearLayout

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // intent로 userinfo 받아오기
        val userId = intent.getStringExtra("USER_ID")
        val userName = intent.getStringExtra("USER_NAME")

        // Retrofit 인터페이스 구현체 생성
        val apiService = RetrofitClient.apiService

        // 사용자 데이터가 존재하는지 확인하는 메소드 호출
        if (!userId.isNullOrBlank()) {
            checkUserExistence(apiService, userId, userName)
//            fetchSongData(apiService, "test_id", "test_song")
//            fetchAllSongsData(apiService, "test_id")
//            insertSongData(apiService, "test_id", "test_song2", 7)
//            deleteSongData(apiService, "test_id", "test_song2")
//            updateSongData(apiService, "test_id", "test_song2", 100)
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        drawerProfile = findViewById(R.id.drawer_profile)

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)
        adapter = ViewPagerAdapter(this, userName, userId)

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Alarm"
//                    tab.setIcon(R.drawable.ic_alarm) // 첫 번째 탭에 아이콘 설정
                }

                1 -> {
                    tab.text = "Songs"
//                    tab.setIcon(R.drawable.ic_music) // 두 번째 탭에 아이콘 설정
                }

                2 -> {
                    tab.text = "History"
//                    tab.setIcon(R.drawable.ic_history) // 세 번째 탭에 아이콘 설정
                }
//
//                3 -> {
//                    tab.view.visibility = View.GONE // Hide the Profile tab
//                }
            }
        }.attach()

        // Load ProfileFragment into the drawer layout
        val profileFragment = ProfileFragment.newInstance(userName, userId)
        supportFragmentManager.beginTransaction().replace(R.id.drawer_profile, profileFragment).commit()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            private var previousPosition = -1

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 2) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END)
                    drawerLayout.closeDrawer(GravityCompat.END)
                }
                previousPosition = position
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                // 이전 탭이 history 탭이고 현재 탭이 history 탭이며 positionOffset이 0.0이고 positionOffsetPixels이 0일 때만 drawer를 염
                if (previousPosition == 2 && position == 2 && positionOffset == 0.0f && positionOffsetPixels == 0) {
                    // History 탭에서 오른쪽으로 스와이프할 때 드로어 열기
                    drawerLayout.openDrawer(GravityCompat.END)
                }
            }
        })
    }

    private inner class ViewPagerAdapter(activity: AppCompatActivity, private val userName: String?, private val userId: String?) :
        FragmentStateAdapter(activity) {

        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> UserFragment.newInstance(userName, userId)
                1 -> MusicFragment.newInstance(userName, userId)
                2 -> CalendarFragment.newInstance(userName, userId)
//                3 -> ProfileFragment.newInstance(userName, userId)
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
