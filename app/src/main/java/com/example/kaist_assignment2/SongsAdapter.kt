package com.example.kaist_assignment2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Color
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class SongsAdapter(private val songs: List<Song>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    private val selectedSongs = mutableSetOf<Song>()

    interface OnItemClickListener {
        fun onItemClick(song: Song)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song, itemClickListener, selectedSongs.contains(song))
        holder.itemView.setOnClickListener {
            if (selectedSongs.contains(song)) {
                selectedSongs.remove(song)
            } else {
                selectedSongs.add(song)
            }
            notifyItemChanged(position)
            itemClickListener.onItemClick(song)
        }
        holder.itemView.setBackgroundColor(if (selectedSongs.contains(song)) Color.LTGRAY else Color.TRANSPARENT)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val songTitle: TextView = itemView.findViewById(R.id.song_title)
        private val artistName: TextView = itemView.findViewById(R.id.artist_name)
        private val albumArt: ImageView = itemView.findViewById(R.id.album_art)


        fun bind(song: Song, clickListener: OnItemClickListener, isSelected: Boolean) {
            songTitle.text = song.title
            artistName.text = song.artist
            Glide.with(itemView.context)
                .load(song.albumArt)
                .apply(RequestOptions().placeholder(R.drawable.album_art_1).error(R.drawable.album_art_1))
                .into(albumArt)
            itemView.setBackgroundColor(if (isSelected) Color.LTGRAY else Color.TRANSPARENT)
        }
    }
}
