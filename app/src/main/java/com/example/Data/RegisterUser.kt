package com.example.Data

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
