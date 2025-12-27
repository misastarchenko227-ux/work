package com.example.mywork
import com.example.Data.LoginRequest
import com.example.Data.LoginResponse
import com.example.Data.RegisterRequest
import com.example.Data.RegisterResponse
import com.example.Data.SetUsernameRequest
import com.example.Data.SetUsernameResponse
import com.example.Data.UsernameCheckRequest
import com.example.Data.UsernameCheckResponse

import retrofit2.http.Body
import retrofit2.http.POST

interface APLRetrofit {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
    @POST("checkUsername")
    suspend fun checkUsername(@Body request: UsernameCheckRequest): UsernameCheckResponse
    @POST("set_username")
    suspend fun setUsername(@Body request: SetUsernameRequest): SetUsernameResponse
    @POST("login")
    suspend fun entranceAccount(@Body request: LoginRequest): LoginResponse

}