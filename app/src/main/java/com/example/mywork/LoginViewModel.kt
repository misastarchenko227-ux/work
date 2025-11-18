package com.example.mywork

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(app: Application) : AndroidViewModel(app) {
    private val sharedPreferences = app.getSharedPreferences("user_data", MODE_PRIVATE)
    fun repeatEntrance(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("LoginViewModel", "Попытка входа с $email и $password")
                val response = RetrofitClient.api.entranceAccount(LoginRequest(email, password))
                Log.d("LOGIN_DEBUG", "Ответ сервера: status=${response.status}, message=${response.message}")
                val ok = response.status == true
                val message = response.message ?: ""

                if (ok) {
                    // Успешный вход — сохраняем данные
                    val hash = PasswordUtils.hashPassword(password)
                    sharedPreferences.edit()
                        .putString("email", email)
                        .putString("password", hash)
                        .putBoolean("is_registered", true)
                        .apply()

                    withContext(Dispatchers.Main) {
                        onResult(true, "ok") // успешный вход
                    }
                } else {

                    // Проверка: неправильный пароль
                    val wrongPassword = message.contains("password", ignoreCase = true) ||
                            message.contains("неверн", ignoreCase = true)

                    withContext(Dispatchers.Main) {
                        if (wrongPassword) {
                            onResult(false, "Пароль неверный")
                        } else {
                            onResult(false, message.ifEmpty { "Ошибка входа" })
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("LOGIN_DEBUG2", "Ошибка: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    onResult(false, "Ошибка входа")
                }
            }
        }
    }

}