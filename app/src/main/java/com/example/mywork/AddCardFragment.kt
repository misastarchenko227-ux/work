package com.example.mywork

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class AddCardFragment : Fragment() {
    private lateinit var etCardNumber: EditText
    private lateinit var etExpiryDate: EditText
    private lateinit var etCvv: EditText
    private lateinit var etCardHolder: EditText
    private lateinit var btnAddCard: Button
    private val luhnChecker = AddCardBase()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_card_fragment, container, false)
        etCardHolder = view.findViewById(R.id.etCardHolder)
        etCardNumber = view.findViewById(R.id.etCardNumber)
        etExpiryDate = view.findViewById(R.id.etExpiryDate)
        etCvv = view.findViewById(R.id.etCvv)
        btnAddCard = view.findViewById(R.id.btnAddCard)
        etCardHolder.addTextChangedListener(CardNumberFormattingWatcher())
        btnAddCard.setOnClickListener {
            val number = etCardNumber.text.toString()
            val isValid = luhnChecker.isValidLuhn(number)
            if (!isValid) {
                etCardNumber.error = "Номер карты некорректен"
                return@setOnClickListener

            }
            Toast.makeText(requireContext(), "Карта успешно добавлена!", Toast.LENGTH_SHORT).show()
        }
        return view
    }
}