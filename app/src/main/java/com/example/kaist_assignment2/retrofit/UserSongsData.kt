package com.example.kaist_assignment2.retrofit

import com.google.gson.annotations.SerializedName

data class UserSongsData(
    @SerializedName("user_id") val userId: String,
    @SerializedName("song") val song: String,
    @SerializedName("play_num") val playNum: Int,
    var artist: String = "",
    var albumArt: String = ""
)
