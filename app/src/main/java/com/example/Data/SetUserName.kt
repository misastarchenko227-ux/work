package com.example.Data

import kotlinx.serialization.Serializable

@Serializable
data class SetUsernameRequest(
    val email: String,
    val username: String
)
@Serializable
data class SetUsernameResponse(
    val message: String,
    val username:Boolean)