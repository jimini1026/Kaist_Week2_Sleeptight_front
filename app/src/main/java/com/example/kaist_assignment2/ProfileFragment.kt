package com.example.kaist_assignment2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.kaist_assignment2.retrofit.ApiService
import com.example.kaist_assignment2.retrofit.RetrofitClient
import com.example.kaist_assignment2.retrofit.UserSongsData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide


class ProfileFragment : Fragment() {

    companion object {
        fun newInstance(userName: String?, userId: String?) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putString("USER_NAME", userName)
                putString("USER_ID", userId)
            }
        }
    }

    private lateinit var recentSongsContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userName = arguments?.getString("USER_NAME")
        val userId = arguments?.getString("USER_ID")

        val tvProfileName: TextView = view.findViewById(R.id.tv_profile_name)
        val tvProfileId: TextView = view.findViewById(R.id.tv_profile_id)
        recentSongsContainer = view.findViewById(R.id.recent_songs_container)

        val profileImage: ImageView = view.findViewById(R.id.image_profile)
        profileImage.setOnClickListener {
            // 클릭 시 원하는 웹 페이지로 이동하는 코드
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jimini1026/Kaist_Assignment2"))
            startActivity(intent)
        }

        tvProfileName.text = userName
        tvProfileId.text = userId

        if (!userId.isNullOrEmpty()) {
            fetchRecentSongs(userId)
        }
    }

    private fun fetchRecentSongs(userId: String) {
        val apiService = RetrofitClient.apiService
        val call = apiService.getSongsDataByID(userId)

        call.enqueue(object : Callback<List<UserSongsData>> {
            override fun onResponse(call: Call<List<UserSongsData>>, response: Response<List<UserSongsData>>) {
                if (response.isSuccessful) {
                    val songs = response.body()
                    if (songs != null) {
                        val recentSongs = songs.sortedByDescending { it.playNum }.take(5)
//                        displayRecentSongs(recentSongs)
                        fetchAlbumArtForSongs(recentSongs)
                    } else {
                        Toast.makeText(context, "No recent songs found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(context, "Failed to fetch recent songs: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<UserSongsData>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchAlbumArtForSongs(songs: List<UserSongsData>) {
        val updatedSongs = songs.map { song ->
            val albumArtUri = getAlbumArtUri(song.song) ?: ""
            song.copy(albumArt = albumArtUri, artist = song.artist ?: "")
        }
        displayRecentSongs(updatedSongs)
    }

    private fun getAlbumArtUri(songTitle: String): String? {
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.TITLE} = ?"
        val selectionArgs = arrayOf(songTitle)

        val cursor = requireContext().contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        var albumArtUri: String? = null

        cursor?.use {
            if (it.moveToFirst()) {
                val albumId = it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                albumArtUri = Uri.parse("content://media/external/audio/albumart/$albumId").toString()
            }
        }

        return albumArtUri
    }

    private fun displayRecentSongs(songs: List<UserSongsData>) {
        recentSongsContainer.removeAllViews()
        val colors = arrayOf(
            "#C497FE",
            "#F7B3FE",
            "#f4a688",
            "#89E7FF",
            "#A98AFF"
        )
        for ((index, song) in songs.withIndex()) {
            val songView = LayoutInflater.from(context).inflate(R.layout.recent_song_item, recentSongsContainer, false)
            val albumArt: ImageView = songView.findViewById(R.id.albumArt)
            val songTitle: TextView = songView.findViewById(R.id.songTitle)
//            val songArtist: TextView = songView.findViewById(R.id.songArtist)
            val cardView: androidx.cardview.widget.CardView = songView.findViewById(R.id.cardView)

            songTitle.text = song.song
            songTitle.maxLines = 3
            songTitle.ellipsize = TextUtils.TruncateAt.END
//            songArtist.text = song.artist

            // 앨범 아트 로드
            Glide.with(this)
                .load(song.albumArt)
                .placeholder(R.drawable.album_art_1) // 기본 이미지
                .error(R.drawable.album_art_1) // 오류 시 이미지
                .into(albumArt)

            cardView.setCardBackgroundColor(Color.parseColor(colors[index % colors.size]))

            recentSongsContainer.addView(songView)
        }
    }
}