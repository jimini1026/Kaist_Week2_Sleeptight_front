package com.example.kaist_assignment2.retrofit

import com.google.gson.annotations.SerializedName
import java.sql.Time
import java.util.*

data class UserSleepData(
    @SerializedName("user_id") val userId: String,
    @SerializedName("date") val date: Date,
    @SerializedName("sleeptime") val sleepTime: Time,
    @SerializedName("predwaketime") val predWakeTime: Time,
    @SerializedName("realwaketime") val realWakeTime: Time
)