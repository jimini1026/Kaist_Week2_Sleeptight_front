package com.example.kaist_assignment2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MusicFragment : Fragment() {

    companion object {
        fun newInstance(): MusicFragment {
            return MusicFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_music 레이아웃 파일을 인플레이트합니다.
        val view = inflater.inflate(R.layout.fragment_music, container, false)

        // RecyclerView 설정
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = SongsAdapter(getSampleSongs())

        // NumberPicker 설정
        val minutePicker: NumberPicker = view.findViewById(R.id.minute_picker)
        val secondPicker: NumberPicker = view.findViewById(R.id.second_picker)

        minutePicker.minValue = 0
        minutePicker.maxValue = 59

        secondPicker.minValue = 0
        secondPicker.maxValue = 59

        // 초기값 설정 (예: 0분 0초)
        minutePicker.value = 0
        secondPicker.value = 0

        return view
    }

    // 샘플 데이터 생성
    private fun getSampleSongs(): List<Song> {
        return listOf(
            Song("Song Title 1", "Artist 1", R.drawable.album_art_1),
            Song("Song Title 2", "Artist 2", R.drawable.album_art_2),
            Song("Song Title 1", "Artist 1", R.drawable.album_art_1),
            Song("Song Title 2", "Artist 2", R.drawable.album_art_2),
            Song("Song Title 1", "Artist 1", R.drawable.album_art_1),
            Song("Song Title 2", "Artist 2", R.drawable.album_art_2),
            Song("Song Title 1", "Artist 1", R.drawable.album_art_1),
            Song("Song Title 2", "Artist 2", R.drawable.album_art_2),
            Song("Song Title 1", "Artist 1", R.drawable.album_art_1),
            Song("Song Title 2", "Artist 2", R.drawable.album_art_2),
            Song("Song Title 1", "Artist 1", R.drawable.album_art_1),
            Song("Song Title 2", "Artist 2", R.drawable.album_art_2),
            Song("Song Title 1", "Artist 1", R.drawable.album_art_1),
            Song("Song Title 2", "Artist 2", R.drawable.album_art_2),
            Song("Song Title 1", "Artist 1", R.drawable.album_art_1),
            Song("Song Title 2", "Artist 2", R.drawable.album_art_2),
        )
    }
}