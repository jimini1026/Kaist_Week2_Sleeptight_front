package com.example.kaist_assignment2


import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MusicFragment : Fragment(), SongsAdapter.OnItemClickListener {

    private val REQUEST_CODE_READ_MEDIA_AUDIO = 1

    companion object {
        fun newInstance(): MusicFragment {
            return MusicFragment()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                setupRecyclerView(view)
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        // RecyclerView 설정
//        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        recyclerView.adapter = SongsAdapter(getSampleSongs(), this)

        // fragment_music 레이아웃 파일을 인플레이트합니다.
        val view = inflater.inflate(R.layout.fragment_music, container, false)

        // 권한 확인
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            setupRecyclerView(view)
        }

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

    private fun setupRecyclerView(view: View?) {
        view ?: return
        // RecyclerView 설정
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = SongsAdapter(getSongsFromDevice(), this)
    }

    private fun getSongsFromDevice(): List<Song> {
        val songs = mutableListOf<Song>()
        val projection = arrayOf(
                MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val cursor = requireContext().contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )

        cursor?.use {
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn)
                val path = it.getString(dataColumn)
                songs.add(Song(title, artist, R.drawable.album_art_1, path))
            }
        }

        return songs
    }

    override fun onItemClick(song: Song) {
        Toast.makeText(context, "Clicked: ${song.title} by ${song.artist}", Toast.LENGTH_SHORT).show()
        // 재생할 파일을 준비한다.
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(song.filePath)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    // 샘플 데이터 생성
//    private fun getSampleSongs(): List<Song> {
//        return listOf(
//            Song("Song Title 1", "Artist 1", R.drawable.album_art_1),
//            Song("Song Title 2", "Artist 2", R.drawable.album_art_2),
//            Song("Song Title 3", "Artist 1", R.drawable.album_art_1),
//            Song("Song Title 4", "Artist 2", R.drawable.album_art_2),
//            Song("Song Title 5", "Artist 1", R.drawable.album_art_1),
//            Song("Song Title 6", "Artist 2", R.drawable.album_art_2),
//            Song("Song Title 7", "Artist 1", R.drawable.album_art_1),
//            Song("Song Title 8", "Artist 2", R.drawable.album_art_2),
//            Song("Song Title 9", "Artist 1", R.drawable.album_art_1),
//            Song("Song Title 10", "Artist 2", R.drawable.album_art_2),
//            Song("Song Title 11", "Artist 1", R.drawable.album_art_1),
//            Song("Song Title 12", "Artist 2", R.drawable.album_art_2),
//            Song("Song Title 13", "Artist 1", R.drawable.album_art_1),
//            Song("Song Title 14", "Artist 2", R.drawable.album_art_2),
//            Song("Song Title 15", "Artist 1", R.drawable.album_art_1),
//            Song("Song Title 16", "Artist 2", R.drawable.album_art_2),
//        )
//    }
}