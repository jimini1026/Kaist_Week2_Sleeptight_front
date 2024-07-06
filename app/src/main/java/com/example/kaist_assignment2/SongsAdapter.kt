package com.example.kaist_assignment2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongsAdapter(private val songs: List<Song>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(song: Song)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song, itemClickListener)
//        holder.songTitle.text = song.title
//        holder.artistName.text = song.artist
//        holder.albumArt.setImageResource(song.albumArt)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val songTitle: TextView = itemView.findViewById(R.id.song_title)
//        val artistName: TextView = itemView.findViewById(R.id.artist_name)
//        val albumArt: ImageView = itemView.findViewById(R.id.album_art)
        private val songTitle: TextView = itemView.findViewById(R.id.song_title)
        private val artistName: TextView = itemView.findViewById(R.id.artist_name)
        private val albumArt: ImageView = itemView.findViewById(R.id.album_art)


        fun bind(song: Song, clickListener: OnItemClickListener) {
            songTitle.text = song.title
            artistName.text = song.artist
            albumArt.setImageResource(song.albumArt)
            itemView.setOnClickListener {
                clickListener.onItemClick(song)
            }
        }
    }
}
