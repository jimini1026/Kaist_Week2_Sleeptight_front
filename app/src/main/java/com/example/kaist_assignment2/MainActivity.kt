package com.example.kaist_assignment2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)
        adapter = ViewPagerAdapter(this, userName, userId)

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Tab ${(position + 1)}"
        }.attach()
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
                2 -> CalendarFragment.newInstance()
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }
    }

    // 사용자가 존재하는지 확인하는 메소드
    private fun checkUserExistence(apiService: ApiService, userId: String, userName: String?) {
        val call = apiService.getUser(userId)
        Toast.makeText(this@MainActivity, "check UserExistence USER_ID : '$userId'", Toast.LENGTH_SHORT).show()

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

    // song 데이터를 서버에서 가져오는 함수
    private fun fetchSongData(apiService: ApiService, userId: String, song: String) {
        val call = apiService.getSongsData(userId, song)

        call.enqueue(object : Callback<List<UserSongsData>> {
            override fun onResponse(call: Call<List<UserSongsData>>, response: Response<List<UserSongsData>>) {
                if (response.isSuccessful) {
                    val songData = response.body()
                    if (songData != null) {
                        // songData 사용
                        Log.d("MainActivity", "Song data: $songData")
                        Toast.makeText(this@MainActivity, "Song data fetched successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "No song data found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MainActivity", "Error response: $errorBody")
                    Toast.makeText(this@MainActivity, "Failed to fetch song data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<UserSongsData>>, t: Throwable) {
                Log.e("MainActivity", "Failed to send GET request: ${t.message}")
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 모든 song 데이터를 서버에서 가져오는 함수
    private fun fetchAllSongsData(apiService: ApiService, userId: String) {
        val call = apiService.getSongsDataByID(userId)

        call.enqueue(object : Callback<List<UserSongsData>> {
            override fun onResponse(call: Call<List<UserSongsData>>, response: Response<List<UserSongsData>>) {
                if (response.isSuccessful) {
                    val songData = response.body()
                    if (songData != null) {
                        // 서버 응답을 로그에 출력하여 확인
                        Log.d("MainActivity", "Raw server response: ${response.raw()}")
                        Log.d("MainActivity", "Parsed song data: $songData")

                        // 데이터를 로그에 출력
                        for (song in songData) {
                            Log.d("MainActivity", "User ID: ${song.userId}, Song: ${song.song}, PlayNum: ${song.playNum}")
                        }
                        Toast.makeText(this@MainActivity, "All song data fetched successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "No song data found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MainActivity", "Error response: $errorBody")
                    Toast.makeText(this@MainActivity, "Failed to fetch all song data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<UserSongsData>>, t: Throwable) {
                Log.e("MainActivity", "Failed to send GET request: ${t.message}")
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // song 데이터를 삽입하는 함수
    private fun insertSongData(apiService: ApiService, userId: String, song: String, playNum: Int) {
        val userSongsData = UserSongsData(userId, song, playNum)
        val call = apiService.postSongsData(userSongsData)

        // 비동기적으로 요청 실행
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "Song data inserted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MainActivity", "Error response: $errorBody")
                    Toast.makeText(this@MainActivity, "Failed to insert song data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("MainActivity", "Failed to send POST request: ${t.message}")
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // song 데이터를 삭제하는 함수
    private fun deleteSongData(apiService: ApiService, userId: String, song: String) {
        val userSongsData = UserSongsData(userId, song, 0) // playNum은 삭제할 때 필요 없으므로 0으로 설정
        val call = apiService.deleteSongsData(userSongsData)

        // 비동기적으로 요청 실행
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "Song data deleted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MainActivity", "Error response: $errorBody")
                    Toast.makeText(this@MainActivity, "Failed to delete song data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("MainActivity", "Failed to send DELETE request: ${t.message}")
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateSongData(apiService: ApiService, userId: String, song: String, playNum: Int) {
        val userSongsData = UserSongsData(userId, song, playNum)
        val call = apiService.updateSongsData(userSongsData)

        // 비동기적으로 요청 실행
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "Song data updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MainActivity", "Error response: $errorBody")
                    Toast.makeText(this@MainActivity, "Failed to update song data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("MainActivity", "Failed to send POST request: ${t.message}")
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
