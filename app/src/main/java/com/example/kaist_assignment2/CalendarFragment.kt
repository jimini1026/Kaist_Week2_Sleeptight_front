package com.example.kaist_assignment2

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kaist_assignment2.retrofit.ApiService
import com.example.kaist_assignment2.retrofit.RetrofitClient
import com.example.kaist_assignment2.retrofit.UserSongsData
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class CalendarFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    companion object {
        private const val ARG_USER_NAME = "user_name"
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userName: String?, userId: String?): CalendarFragment {
            val fragment = CalendarFragment()
            val args = Bundle()
            args.putString(ARG_USER_NAME, userName)
            args.putString(ARG_USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        val calendarView: MaterialCalendarView = view.findViewById(R.id.calendarView)

        val userName = arguments?.getString(ARG_USER_NAME)
        val userId = arguments?.getString(ARG_USER_ID)
        Toast.makeText(context, "Username: $userName, UserID: $userId", Toast.LENGTH_LONG).show()

        // 데코레이터 추가
        val sundayDecorator = SundayDecorator()
        val saturdayDecorator = SaturdayDecorator()
        val todayDecorator = ToDayDecorator(requireContext())

        // 이벤트 날짜 리스트
        val calendarList = mutableListOf<CalendarDay>()
        calendarList.add(CalendarDay.today())
        calendarList.add(CalendarDay.from(2022, 10, 25))
        val eventDecorator = EventDecorator(calendarList, requireContext(), Color.BLUE)

        calendarView.addDecorators(sundayDecorator, saturdayDecorator, todayDecorator, eventDecorator)

        userId?.let { fetchTopSongsData(RetrofitClient.apiService, it) }

        sharedViewModel.updateSongsEvent.observe(viewLifecycleOwner) {
            if (it) {
                userId?.let { fetchTopSongsData(RetrofitClient.apiService, it) }
            }
        }

        return view
    }

    inner class ToDayDecorator(context: Context) : DayViewDecorator {
        private var date = CalendarDay.today()
        private val drawable = context.resources.getDrawable(R.drawable.data_select_deco, null)

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day?.equals(date) == true
        }

        override fun decorate(view: DayViewFacade?) {
            drawable?.let {
                view?.setBackgroundDrawable(it)
            }
        }
    }

    inner class SundayDecorator : DayViewDecorator {
        private val calendar = Calendar.getInstance()

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            day?.copyTo(calendar)
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            return weekDay == Calendar.SUNDAY
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(ForegroundColorSpan(Color.RED))
        }
    }

    inner class SaturdayDecorator : DayViewDecorator {
        private val calendar = Calendar.getInstance()

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            day?.copyTo(calendar)
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            return weekDay == Calendar.SATURDAY
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(ForegroundColorSpan(Color.BLUE))
        }
    }

    inner class EventDecorator(private val dates: Collection<CalendarDay>, context: Context, private val color: Int) : DayViewDecorator {
        private val datesSet = HashSet(dates)

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return datesSet.contains(day)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(DotSpan(5F, color))
        }
    }

    private fun fetchTopSongsData(apiService: ApiService, userId: String) {
        val songsFromDevice = getSongsFromDevice()

        val call = apiService.getSongsDataByID(userId)
        call.enqueue(object : Callback<List<UserSongsData>> {
            override fun onResponse(call: Call<List<UserSongsData>>, response: Response<List<UserSongsData>>) {
                if (response.isSuccessful) {
                    val songData = response.body()
                    if (songData != null) {
                        val topSongs = songData.sortedByDescending { it.playNum }.take(3)
                        val topSongsWithInfo = topSongs.map { userSong ->
                            val matchingSong = songsFromDevice.find { it.title == userSong.song }
                            userSong.apply {
                                artist = matchingSong?.artist ?: "Unknown Artist"
                                albumArt = matchingSong?.albumArt ?: "https://example.com/default_album_art.jpg"
                            }
                        }
                        displayTopSongs(topSongsWithInfo)
                    } else {
                        Toast.makeText(context, "No song data found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("CalendarFragment", "Error response: $errorBody")
                    Toast.makeText(context, "Failed to fetch song data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<UserSongsData>>, t: Throwable) {
                Log.e("CalendarFragment", "Failed to send GET request: ${t.message}")
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchAdditionalSongInfo(topSongs: List<UserSongsData>) {
        // 실제 아티스트 이름과 앨범 아트 데이터를 가져올 수 있는 로직을 추가합니다.
        // 이 예제에서는 간단히 아티스트 이름과 앨범 아트를 설정합니다.
        for (song in topSongs) {
            song.artist = "Example Artist" // 실제 데이터를 여기에 설정합니다.
            song.albumArt = "https://example.com/album_art.jpg" // 실제 데이터를 여기에 설정합니다.
        }

        displayTopSongs(topSongs)
    }

    private fun displayTopSongs(topSongs: List<UserSongsData>) {
        val topSongsContainer: LinearLayout? = requireView().findViewById(R.id.topSongsContainer)
        topSongsContainer?.removeAllViews()

        for ((index, song) in topSongs.withIndex()) {
            val songView = LayoutInflater.from(context).inflate(R.layout.song_item, topSongsContainer, false)
            val songRank: TextView = songView.findViewById(R.id.songRank)
            val songTitle: TextView = songView.findViewById(R.id.songTitle)
            val songArtist: TextView = songView.findViewById(R.id.songArtist)
            val albumArt: ImageView = songView.findViewById(R.id.albumArt)
            val songPlayNum: TextView = songView.findViewById(R.id.songPlayNum)

            // 실제 데이터를 여기에 반영합니다.
            songRank.text = "${index + 1}"
            songTitle.text = song.song
            songArtist.text = song.artist // 이 부분은 실제 아티스트 데이터를 사용할 수 있으면 수정합니다.
            songPlayNum.text = "Plays: ${song.playNum}"

            // Glide를 사용하여 앨범 아트를 설정합니다.
            Glide.with(this)
                .load(song.albumArt) // 이 부분은 실제 앨범 아트 데이터를 사용할 수 있으면 수정합니다.
                .apply(RequestOptions().placeholder(R.drawable.album_art_1).error(R.drawable.album_art_1))
                .into(albumArt)

            topSongsContainer?.addView(songView)
        }
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
}