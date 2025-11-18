package com.example.mywork

import android.app.Application
import android.content.Context.MODE_PRIVATE
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
                val response = RetrofitClient.api.entranceAccount(LoginRequest(email, password))
                val ok = response.status == true
                if (ok) {
                    val hash = PasswordUtils.hashPassword(password)
                    sharedPreferences.edit()
                        .putString("email", email)
                        .putString("password", hash)
                        .putBoolean("is_registered", true).apply()
                }
                withContext(Dispatchers.Main) {
                    onResult(ok, response.message ?: "Ошибка входа")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onResult(false, "Ошибка входа")
                }
            }
        }
    }
}