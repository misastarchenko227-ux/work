package com.example.ViewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.Data.LoginRequest
import com.example.Data.LoginResponse
import com.example.mywork.PasswordUtils
import com.example.mywork.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val sharedPreferences =
        app.getSharedPreferences("user_data", Context.MODE_PRIVATE)

    fun repeatEntrance(
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = loginRequest(email, password)
                handleResponse(response, email, password, onResult)
            } catch (e: Exception) {
                handleError(e, onResult)
            }
        }
    }

    // -------------------- NETWORK --------------------

    private suspend fun loginRequest(
        email: String,
        password: String
    ): LoginResponse {
        Log.d("LoginViewModel", "Попытка входа: $email")
        return RetrofitClient.api
            .entranceAccount(LoginRequest(email, password))
    }

    // -------------------- RESPONSE --------------------

    private suspend fun handleResponse(
        response: LoginResponse,
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {
        Log.d(
            "LOGIN_DEBUG",
            "Ответ сервера: status=${response.status}, message=${response.message}"
        )

        if (response.status == true) {
            saveUserData(email, password)
            returnResult(onResult, true, "ok")
        } else {
            val message = parseErrorMessage(response.message)
            returnResult(onResult, false, message)
        }
    }

    // -------------------- SAVE --------------------

    private fun saveUserData(email: String, password: String) {
        val hash = PasswordUtils.hashPassword(password)

        sharedPreferences.edit()
            .putString("email", email)
            .putString("password", hash)
            .putBoolean("is_registered", true)
            .apply()
    }

    // -------------------- HELPERS --------------------

    private fun parseErrorMessage(message: String?): String {
        if (message.isNullOrEmpty()) return "Ошибка входа"

        val wrongPassword =
            message.contains("password", ignoreCase = true) ||
                    message.contains("неверн", ignoreCase = true)

        return if (wrongPassword) {
            "Пароль неверный"
        } else {
            message
        }
    }

    private suspend fun returnResult(
        onResult: (Boolean, String) -> Unit,
        success: Boolean,
        message: String
    ) {
        withContext(Dispatchers.Main) {
            onResult(success, message)
        }
    }

    private suspend fun handleError(
        e: Exception,
        onResult: (Boolean, String) -> Unit
    ) {
        Log.e("LOGIN_ERROR", "Ошибка входа", e)
        returnResult(onResult, false, "Ошибка входа")
    }
}
