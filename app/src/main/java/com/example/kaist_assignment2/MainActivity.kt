package com.example.kaist_assignment2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
