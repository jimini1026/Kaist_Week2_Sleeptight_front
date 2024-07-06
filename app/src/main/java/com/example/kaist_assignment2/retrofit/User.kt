package com.example.kaist_assignment2.retrofit

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id") val userId: String,
    @SerializedName("user_name") val userName: String
)