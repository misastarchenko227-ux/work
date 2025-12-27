package com.example.Users.Account
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import coil.load
import com.example.ViewModel.AccountViewModel
import com.example.mywork.R
import kotlinx.coroutines.launch

class Account : Fragment(R.layout.fragment_account) {

    private val viewModel: AccountViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
    }
private lateinit var myOrders: Button
    private lateinit var avatarView: ImageView
    private lateinit var cardButton: Button
    private lateinit var nameText: TextView
    private lateinit var emailText: TextView
    private lateinit var usernameEdit: EditText
    private lateinit var saveNameButton: Button
    private lateinit var sharedPref: SharedPreferences

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                avatarView.load(it)
                saveAvatar(it)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🔹 Инициализация view
        avatarView = view.findViewById(R.id.avatarView)
        cardButton = view.findViewById(R.id.add_a_card)
        nameText = view.findViewById(R.id.textname)
        emailText = view.findViewById(R.id.textEmail)
        usernameEdit = view.findViewById(R.id.editTextText)
        saveNameButton = view.findViewById(R.id.buttonName)
        myOrders = view.findViewById(R.id.my_orders)


        // 🔹 Получение SharedPreferences
        sharedPref = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)

        // Показываем пользователя из SharedPreferences
        nameText.text = sharedPref.getString("names", "")
        emailText.text = sharedPref.getString("email", "")

        // Загрузка сохранённого аватара
        loadAvatar()
        // 🔹 Смена аватара
        changingAvatar()
        // 🔹 Сохранение имени
        saveName()

        // Кнопка добавления карты
        addCard()
        myOrderProduct()

    }

    private fun saveAvatar(uri: Uri) {
        sharedPref.edit().putString("avatar_uri", uri.toString()).apply()
    }

    private fun loadAvatar() {
        val savedUri = sharedPref.getString("avatar_uri", null)

        if (!savedUri.isNullOrEmpty()) {
            avatarView.load(savedUri) {
                crossfade(true)
                error(R.drawable.stream)
            }
        } else {
            avatarView.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.stream)
            )
        }
    }

    private fun changingAvatar() {
        avatarView.setOnClickListener {
            imagePicker.launch("image/*")
        }
    }

    private fun saveName() {
        saveNameButton.setOnClickListener {
            val username = usernameEdit.text.toString().trim()
            val email = sharedPref.getString("email", null)

            if (username.isEmpty()) {
                usernameEdit.error = "Введите имя"
                return@setOnClickListener
            }

            saveNameButton.isEnabled = false
            saveNameButton.alpha = 0.5f

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.addName(username, email ?: "", username)
                sharedPref.edit().putString("names", username).apply()
                nameText.text = username
                saveNameButton.isEnabled = true
                saveNameButton.alpha = 1f
            }
        }
    }

    private fun addCard() {
        cardButton.setOnClickListener {
            Toast.makeText(requireContext(), "Переход к добавлению карты", Toast.LENGTH_SHORT)
                .show()
        }
    }
    private fun myOrderProduct(){
        myOrders.setOnClickListener {
            findNavController().navigate(R.id.orderProduct)
        }
    }

}