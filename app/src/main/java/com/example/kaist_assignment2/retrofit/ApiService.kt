package com.example.kaist_assignment2.retrofit

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("/users") // users 엔드포인트에 GET 요청을 보냄
    fun getUsers(): Call<List<User>> // Call 객체로 비동기적으로 User 리스트를 반환
}