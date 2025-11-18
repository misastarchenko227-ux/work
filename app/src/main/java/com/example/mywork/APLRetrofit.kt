package com.example.mywork
import retrofit2.Call

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface APLRetrofit {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
    @POST("checkUsername")
    suspend fun checkUsername(@Body request:UsernameCheckRequest):UsernameCheckResponse
    @POST("set_username")
    suspend fun setUsername(@Body request: SetUsernameRequest): SetUsernameResponse
    @GET("login")
    suspend fun entranceAccount(@Body request: LoginRequest): LoginResponse

}