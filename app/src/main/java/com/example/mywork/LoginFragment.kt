package com.example.mywork
import android.animation.ValueAnimator
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var button: Button
    private lateinit var checkBox: CheckBox
    private lateinit var sharedPref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = view.findViewById(R.id.email)
        password = view.findViewById(R.id.Password)
        button = view.findViewById(R.id.button)
        checkBox = view.findViewById(R.id.checkBox)
        sharedPref = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        button.setOnClickListener {
            var emailText = email.text.toString().trim()
            var passwordText = password.text.toString().trim()
            if (emailText.isEmpty() || passwordText.isEmpty()) {

sharedPref.getString("emails",null)
            }

        }
    }
}