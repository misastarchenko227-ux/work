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
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mywork.R
import com.example.ViewModel.RegisterViewModel
import com.example.mywork.ScrenAccount

class RegisterUser : Fragment(R.layout.fragment_new_acaunt) {

    private val viewModel: RegisterViewModel by viewModels()
    private val PREFS_NAME = "user_data"

    // UI
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var nameField: EditText
    private lateinit var registerButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var checkBox: CheckBox

    // Preferences
    private lateinit var prefs: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initPrefs()
        checkAutoLogin()
        restoreSavedFields()
        setupRegisterClick()
    }

    // -------------------- INIT --------------------

    private fun initViews(view: View) {
        emailField = view.findViewById(R.id.Email)
        passwordField = view.findViewById(R.id.Password)
        nameField = view.findViewById(R.id.UserName)
        registerButton = view.findViewById(R.id.registration)
        progressBar = view.findViewById(R.id.progressBar)
        checkBox = view.findViewById(R.id.checkBox)
    }

    private fun initPrefs() {
        prefs = requireActivity()
            .applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // -------------------- AUTO LOGIN --------------------

    private fun checkAutoLogin() {
        val remember = prefs.getBoolean("remember_me", false)
        val savedName = prefs.getString("name", "")

        if (remember) {
            navigateToShop(savedName)
        }
    }

    private fun restoreSavedFields() {
        emailField.setText(prefs.getString("emails", ""))
        passwordField.setText(prefs.getString("passwords", ""))
        nameField.setText(prefs.getString("names", ""))
        checkBox.isChecked = prefs.getBoolean("remember_me", false)
    }

    // -------------------- REGISTER --------------------

    private fun setupRegisterClick() {
        registerButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val name = nameField.text.toString().trim()

            if (!validateInput(email, password, name))
                return@setOnClickListener

            setLoading(true)

            viewModel.registerUser(email, password, name) { success, message ->
                setLoading(false)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

                if (success) {
                    saveUserData(email, password, name)
                    navigateToShop(name)
                }
            }
        }
    }

    private fun validateInput(email: String, password: String, name: String): Boolean {
        return when {
            email.isEmpty() || !email.contains("@") -> {
                emailField.error = "Введите корректный Email"
                false
            }
            password.length < 7 -> {
                passwordField.error = "Минимум 7 символов"
                false
            }
            name.isEmpty() -> {
                nameField.error = "Введите имя"
                false
            }
            else -> true
        }
    }

    private fun setLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        registerButton.isEnabled = !isLoading
    }

    // -------------------- SAVE DATA --------------------

    private fun saveUserData(email: String, password: String, name: String) {
        val editor = prefs.edit()

        if (checkBox.isChecked) {
            editor.putString("emails", email)
            editor.putString("passwords", password)
            editor.putString("names", name)
            editor.putBoolean("remember_me", true)
        } else {
            editor.clear()
        }

        editor.apply()
    }

    // -------------------- NAVIGATION --------------------

    private fun navigateToShop(name: String?) {
        val intent = Intent(requireContext(), ScrenAccount::class.java)
        intent.putExtra("name", name)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}