package com.example.mywork
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class RegisterUser : Fragment(R.layout.fragment_new_acaunt) {

    private val viewModel: RegisterViewModel by viewModels()
    private val PREFS_NAME = "user_data"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailField: EditText = view.findViewById(R.id.Email)
        val passwordField: EditText = view.findViewById(R.id.Password)
        val nameField: EditText = view.findViewById(R.id.UserName)
        val registerButton: Button = view.findViewById(R.id.registration)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)

        // Используем applicationContext для стабильности
        val prefs = requireActivity().applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // DEBUG: вывести текущее состояние prefs
        val dbgRemember = prefs.getBoolean("remember_me", false)
        val dbgEmail = prefs.getString("email", null)
        Log.d("RegisterUser", "Prefs on start: remember=$dbgRemember email=$dbgEmail")

        // Автовход, если пользователь был запомнен
        if (dbgRemember) {
            val savedName = prefs.getString("name", "")
            Log.d("AutoLogin", "Автовход для $savedName")
            navigateToShop(savedName)
            return
        }

        // Подставляем сохранённые значения (если пользователь ранее только сохранил, но не автологин)
        emailField.setText(prefs.getString("emails", ""))
        passwordField.setText(prefs.getString("passwords", ""))
        nameField.setText(prefs.getString("names", ""))
        checkBox.isChecked = dbgRemember

        registerButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val name = nameField.text.toString().trim()

            when {
                email.isEmpty() || !email.contains("@") -> {
                    emailField.error = "Введите корректный Email"
                    return@setOnClickListener
                }
                password.length < 7 -> {
                    passwordField.error = "Минимум 7 символов"
                    return@setOnClickListener
                }
                name.isEmpty() -> {
                    nameField.error = "Введите имя"
                    return@setOnClickListener
                }
            }

            progressBar.visibility = View.VISIBLE
            registerButton.isEnabled = false

            viewModel.registerUser(email, password, name) { success, message ->
                progressBar.visibility = View.GONE
                registerButton.isEnabled = true

                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

                if (success) {
                    val editor = prefs.edit()
                    if (checkBox.isChecked) {
                        editor.putString("emails", email)
                        editor.putString("passwords", password)
                        editor.putString("names", name)
                        editor.putBoolean("remember_me", true)
                        // commit() чтобы синхронно сохранить перед переходом
                        val committed = editor.commit()
                        Log.d("RegisterUser", "Saved prefs committed=$committed email=$email")
                    } else {
                        // Удаляем только наши ключи, не чистим весь файл
                        editor.remove("email")
                        editor.remove("password")
                        editor.remove("name")
                        editor.putBoolean("remember_me", false)
                        editor.commit()
                    }
                    editor.apply()

                    navigateToShop(name)
                }
            }
        }
    }

    private fun navigateToShop(name: String?) {
        val intent = Intent(requireContext(), screnShop::class.java)
        intent.putExtra("name", name)
        // 🟢 Очистить всю историю и создать новый стек
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }

}

