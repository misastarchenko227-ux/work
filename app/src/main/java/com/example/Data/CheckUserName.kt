package com.example.Data

import kotlinx.serialization.Serializable

@Serializable
data class UsernameCheckRequest(
    val UsernameCheckRequest: String)
@Serializable
data class UsernameCheckResponse(
    val available: Boolean,
    val message: String
)