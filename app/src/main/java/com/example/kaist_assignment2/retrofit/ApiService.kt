package com.example.kaist_assignment2.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // users 관련 메소드
    @GET("/users/{user_id}")
    fun getUser(@Path("user_id") userId: String): Call<User>

    @POST("/users") // users end point 에 POST 요청을 보냄 (userId, userName)
    fun postUser(@Body user: User): Call<User>

    // sleepdata 관련 메소드
    @GET("/sleepdata/{user_id}")
    fun getSleepData(
        @Path("user_id") userId: String,
        @Query("date") date: String
    ): Call<UserSleepData>

//    해당하는 모든 (user_id) response
    @GET("/sleepdata/id/{user_id}")
    fun getSleepDataByID(
        @Path("user_id") userId: String,
    ): Call<List<UserSleepData>>

//    request의 해당하는 (user_id, date) INSERT
    @POST("/sleepdata")
    fun postSleepData(@Body user: UserSleepData): Call<UserSleepData>

    @POST("/sleepdata/delete")
//    deleteSleepData() request의 해당하는 (user_id, date) DELETE
    fun deleteSleepData(@Body user: UserSleepData): Call<UserSleepData>


//    // songdata 관련 메소드
    @GET("/songsdata")
    fun getSongsData(@Query("user_id") userId: String, @Query("song") song: String): Call<List<UserSongsData>>
//    getSongsData() 해당하는 SongsData response
//
    @GET("/songsdata/id")
    fun getSongsDataByID(@Query("user_id") userId: String): Call<List<UserSongsData>>

//    getSongsDataByID() 해당하는 모든 (user_id) response
//
    @POST("/songsdata")
    fun postSongsData(@Body userSongsData: UserSongsData): Call<Void>

//    postSongsData() request의 해당하는 (user_id, song) INSERT
//
    @POST("/songsdata/delete")
    fun deleteSongsData(@Body userSongsData: UserSongsData): Call<Void>
//    deleteSongsData() request의 해당하는 (user_id, song) DELETE
//
    @POST("/songsdata/update")
    fun updateSongsData(@Body userSongsData: UserSongsData): Call<Void>

//    updateSongsData() request의 해당하는 (user_id, song) UPDATE
}