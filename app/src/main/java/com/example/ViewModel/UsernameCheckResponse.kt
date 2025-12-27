package com.example.ViewModel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.Data.SetUsernameRequest
import com.example.Data.UsernameCheckRequest
import com.example.mywork.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AccountViewModel(private val app: Application) : AndroidViewModel(app) {

    private val sharedPreferences = app.getSharedPreferences("user_data", Context.MODE_PRIVATE)

    fun addName(email: String, username: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 🔹 Проверяем, свободно ли имя
                val response = RetrofitClient.api.checkUsername(UsernameCheckRequest(username))
                val ok = response.available ||
                        response.message.contains("успешн", ignoreCase = true)

                if (ok) {
                    // 🔹 Сохраняем имя на сервере
                    val setResponse = RetrofitClient.api.setUsername(
                        SetUsernameRequest(
                            email,
                            username
                        )
                    )

                    if (setResponse.username) {
                        // 🔹 Если сервер подтвердил — сохраняем локально
                        withContext(Dispatchers.Main) {
                            sharedPreferences.edit().putString("username", username).apply()
                            Toast.makeText(app, "Имя успешно сохранено ✅", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(app, setResponse.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(app, "Это имя уже занято ❌", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(app, "Ошибка сети. Повторите попытку.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

