package com.example.kaist_assignment2.retrofit

import com.example.kaist_assignment2.retrofit.ApiService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://172.10.7.133" // 서버 주소

    val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd")
        .create()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}