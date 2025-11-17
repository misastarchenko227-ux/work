package com.example.mywork

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(app: Application) : AndroidViewModel(app) {

    private val sharedPreferences =
        app.getSharedPreferences("user_data", Context.MODE_PRIVATE)

    fun registerUser(
        email: String,
        password: String,
        name: String,
        onResult: (Boolean, String) -> Unit
    ) {
        // Запускаем корутину в ViewModel
        viewModelScope.launch(Dispatchers.IO) {
            try {

                // 🟢 Отправляем запрос регистрации на сервер
                val response = RetrofitClient.api.register(
                    RegisterRequest(email, password, name)
                )


                val ok = response.success
                    ?: response.message?.contains("успешн", ignoreCase = true)
                    ?: false

                // 🟢 Если регистрация успешна — хешируем пароль и сохраняем
                if (ok) {
                    val hash = PasswordUtils.hashPassword(password)

                    sharedPreferences.edit()
                        .putString("email", email)
                        .putString("password", hash)
                        .putString("name", name)
                        .putBoolean("is_registered", true)
                        .apply()
                }

                // 🟢 Возвращаем результат на главный поток
                withContext(Dispatchers.Main) {
                    onResult(ok, response.message ?: "Нет сообщения")
                }

            } catch (e: Exception) {
                // 🔴 Обработка ошибок — возвращаем на UI поток
                withContext(Dispatchers.Main) {
                    onResult(false, "Ошибка: ${e.localizedMessage}")
                }
            }
        }
    }
}

