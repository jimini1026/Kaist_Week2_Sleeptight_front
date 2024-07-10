package com.example.kaist_assignment2.com.example.kaist_assignment2

data class RecentSongData(
    val userId: String,
    val song: String,
    val playNum: Int,
    val albumArtUri: String? // 앨범 아트 URI 추가
)