package com.example.kaist_assignment2.retrofit

import com.google.gson.annotations.SerializedName
import java.sql.Time
import java.util.*

data class UserSleepData(
    @SerializedName("user_id") val userId: String,
    @SerializedName("date") val date: Date,
    @SerializedName("sleeptime") val sleepTime: String,
    @SerializedName("pred_waketime") val predWakeTime: String,
    @SerializedName("real_waketime") val realWakeTime: String
)