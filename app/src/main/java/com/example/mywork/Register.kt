package com.example.mywork

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)
@Serializable
data class RegisterResponse(
    val message: String,
    val error: String,
    val email: String,
    val success: Boolean
)
@Serializable
data class UsernameCheckRequest(val UsernameCheckRequest: String)
@Serializable
data class UsernameCheckResponse(
    val available: Boolean,
    val message: String
)

@Serializable
data class SetUsernameRequest(
    val email: String,
    val username: String
)
@Serializable
data class SetUsernameResponse(
    val message: String,
val username:Boolean)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val status: Boolean,
    val message: String?,
    val token: String?
)

