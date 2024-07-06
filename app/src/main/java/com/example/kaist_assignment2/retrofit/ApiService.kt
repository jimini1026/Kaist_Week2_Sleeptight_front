package com.example.kaist_assignment2.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // users 관련 메소드
    @GET("/users/{user_id}")
    fun getUser(@Path("user_id") userId: String): Call<User>

    @POST("/users") // users end point 에 POST 요청을 보냄 (userId, userName)
    fun postUser(@Body user: User): Call<User>

//    // sleepdata 관련 메소드
//    @GET("/sleepdata")
//    getSleepData() 해당하는 SleepData response
//
//    @GET("/sleepdata/id")
//    getSleepDataByID() 해당하는 모든 (user_id) response
//
//    @POST("/sleepdata")
//    postSleepData() request의 해당하는 (user_id, date) INSERT
//
//    @POST("/sleepdata/delete")
//    deleteSleepData() request의 해당하는 (user_id, date) DELETE


//    // songdata 관련 메소드
//    @GET("/songsdata")
//    getSongsData() 해당하는 SongsData response
//
//    @GET("/songsdata/id")
//    getSongsDataByID() 해당하는 모든 (user_id) response
//
//    @POST("/songsdata")
//    postSongsData() request의 해당하는 (user_id, song) INSERT
//
//    @POST("/songsdata/delete")
//    deleteSongsData() request의 해당하는 (user_id, song) DELETE
//
//    @POST("/songsdata/update")
//    updateSongsData() request의 해당하는 (user_id, song) UPDATE
}