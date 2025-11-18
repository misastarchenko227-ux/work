package com.example.mywork
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

class LoginUser : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()
    private val PREFS_NAME = "user_data"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailField: EditText = view.findViewById(R.id.email)
        val passwordField: EditText = view.findViewById(R.id.Password)
        val loginButton: Button = view.findViewById(R.id.button)
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)

        val prefs = requireActivity()
            .applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val remember = prefs.getBoolean("remember_me", false)

        if (remember) {
            val savedEmail = prefs.getString("emails", "")
            emailField.setText(savedEmail)
            passwordField.setText("") // пароль не показываем
            checkBox.isChecked = true
        }

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            when {
                email.isEmpty() || !email.contains("@") -> {
                    emailField.error = "Введите корректный Email"
                    return@setOnClickListener
                }
                password.length < 7 -> {
                    passwordField.error = "Минимум 7 символов"
                    return@setOnClickListener
                }
            }


            loginButton.isEnabled = false

            viewModel.repeatEntrance(email, password) { success, message ->
                loginButton.isEnabled = true

                if (success) {
                    val editor = prefs.edit()
                    if (checkBox.isChecked) {
                        editor.putString("emails", email)
                        editor.putBoolean("remember_me", true)
                    } else {
                        editor.remove("emails")
                        editor.putBoolean("remember_me", false)
                    }
                    editor.apply()

                    navigateToShop(prefs.getString("name", ""))

                } else {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    Log.d("LoginUser", message)
                }
            }
        }
    }

    private fun navigateToShop(name: String?) {
        val intent = Intent(requireContext(), screnShop::class.java)
        intent.putExtra("name", name)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
