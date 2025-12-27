package com.example.Users
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ViewModel.LoginViewModel
import com.example.mywork.R
import com.example.mywork.ScrenAccount

class LoginUser : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()
    private val PREFS_NAME = "user_data"

    // UI
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var checkBox: CheckBox

    // Prefs
    private lateinit var prefs: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initPrefs()
        restoreRememberedUser()
        setupLoginClick()
    }

    // ---------- Инициализация UI ----------
    private fun initViews(view: View) {
        emailField = view.findViewById(R.id.email)
        passwordField = view.findViewById(R.id.Password)
        loginButton = view.findViewById(R.id.button)
        checkBox = view.findViewById(R.id.checkBox)
    }

    // ---------- SharedPreferences ----------
    private fun initPrefs() {
        prefs = requireActivity()
            .applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // ---------- Восстановление email ----------
    private fun restoreRememberedUser() {
        val remember = prefs.getBoolean("remember_me", false)
        if (remember) {
            emailField.setText(prefs.getString("emails", ""))
            passwordField.setText("")
            checkBox.isChecked = true
        }
    }

    // ---------- Кнопка входа ----------
    private fun setupLoginClick() {
        loginButton.setOnClickListener {
            if (!validateInput()) return@setOnClickListener
            loginUser()
        }
    }

    // ---------- Проверка данных ----------
    private fun validateInput(): Boolean {
        val email = emailField.text.toString().trim()
        val password = passwordField.text.toString().trim()

        return when {
            email.isEmpty() || !email.contains("@") -> {
                emailField.error = "Введите корректный Email"
                false
            }
            password.length < 7 -> {
                passwordField.error = "Минимум 7 символов"
                false
            }
            else -> true
        }
    }

    // ---------- Авторизация ----------
    private fun loginUser() {
        val email = emailField.text.toString().trim()
        val password = passwordField.text.toString().trim()

        loginButton.isEnabled = false

        viewModel.repeatEntrance(email, password) { success, message ->
            loginButton.isEnabled = true

            if (success) {
                saveRememberState(email)
                navigateToShop(prefs.getString("name", ""))
            } else {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                Log.d("LoginUser", message)
            }
        }
    }

    // ---------- Сохранение remember me ----------
    private fun saveRememberState(email: String) {
        prefs.edit().apply {
            if (checkBox.isChecked) {
                putString("emails", email)
                putBoolean("remember_me", true)
            } else {
                remove("emails")
                putBoolean("remember_me", false)
            }
            apply()
        }
    }

    // ---------- Переход в магазин ----------
    private fun navigateToShop(name: String?) {
        val intent = Intent(requireContext(), ScrenAccount::class.java)
        intent.putExtra("name", name)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}

