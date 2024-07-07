package com.example.kaist_assignment2


import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MusicFragment : Fragment(), SongsAdapter.OnItemClickListener {

    private val REQUEST_CODE_READ_MEDIA_AUDIO = 1
    private var playbackDurationInMillis: Int = 0
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var mediaPlayer: MediaPlayer? = null


    companion object {
        fun newInstance(): MusicFragment {
            return MusicFragment()
        }
    }

    private val selectedSongs = mutableListOf<Song>()

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

        val saveButton: Button = view.findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            val minutes = minutePicker.value
            val seconds = secondPicker.value
            playbackDurationInMillis = (minutes * 60 + seconds) * 1000
            startTimer(minutePicker, secondPicker)
            playSelectedSongs()
        }

        handler = Handler(Looper.getMainLooper())

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopPlayback()
    }

    private fun playSelectedSongs() {
        if (selectedSongs.isEmpty()) {
            Toast.makeText(context, "No songs selected", Toast.LENGTH_SHORT).show()
            return
        }

        val mediaPlayer = MediaPlayer()
        playNextSongWithTimer(mediaPlayer, 0, playbackDurationInMillis)
    }

    private fun playNextSongWithTimer(mediaPlayer: MediaPlayer, index: Int, remainingTime: Int) {
        if (index >= selectedSongs.size || remainingTime <= 0) {
            stopPlayback()
//            mediaPlayer.release()
            Toast.makeText(context, "Playback time is over", Toast.LENGTH_SHORT).show()
            return
        }

        val song = selectedSongs[index]
        mediaPlayer.reset()
        mediaPlayer.setDataSource(song.filePath)
        mediaPlayer.prepare()
        mediaPlayer.start()

        val songDuration = mediaPlayer.duration
        val playTime = if (remainingTime < songDuration) remainingTime else songDuration

//        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            playNextSongWithTimer(mediaPlayer, index + 1, remainingTime - playTime)
        }, playTime.toLong())
    }

    private fun startTimer(minutePicker: NumberPicker, secondPicker: NumberPicker) {
        runnable = object : Runnable {
            override fun run() {
                val totalSeconds = minutePicker.value * 60 + secondPicker.value
                if (totalSeconds > 0) {
                    val newTotalSeconds = totalSeconds - 1
                    val minutes = newTotalSeconds / 60
                    val seconds = newTotalSeconds % 60
                    minutePicker.value = minutes
                    secondPicker.value = seconds
                    handler.postDelayed(this, 1000)
                } else {
                    stopPlayback()
                    Toast.makeText(context, "Playback time is over", Toast.LENGTH_SHORT).show()
                }
            }
        }
        handler.post(runnable)
    }

    private fun stopPlayback() {
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacks(runnable)
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
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
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
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (it.moveToNext()) {
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn)
                val path = it.getString(dataColumn)
                val albumId = it.getLong(albumIdColumn)
                val albumArtUri = Uri.parse("content://media/external/audio/albumart/$albumId")
                songs.add(Song(title, artist, albumArtUri.toString(), path))
            }
        }

        return songs
    }

    override fun onItemClick(song: Song) {
        if (selectedSongs.contains(song)) {
            selectedSongs.remove(song)
            Toast.makeText(context, "${song.title} removed from playlist", Toast.LENGTH_SHORT).show()
        } else {
            selectedSongs.add(song)
            Toast.makeText(context, "${song.title} added to playlist", Toast.LENGTH_SHORT).show()
        }
    }
}