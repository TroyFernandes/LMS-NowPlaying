package com.example.lmsnowplaying.network.logitechmediaserver

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("jsonrpc.js")
    suspend fun getPlayer(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("jsonrpc.js")
    suspend fun getPlayerFriendlyName(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("jsonrpc.js")
    suspend fun getCurrentSong(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("jsonrpc.js")
    suspend fun getSongInfo(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("jsonrpc.js")
    suspend fun playPause(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("jsonrpc.js")
    suspend fun next(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("jsonrpc.js")
    suspend fun prev(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("jsonrpc.js")
    suspend fun status(@Body requestBody: RequestBody): Response<ResponseBody>

}
