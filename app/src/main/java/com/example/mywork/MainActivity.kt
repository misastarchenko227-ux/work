package com.example.mywork
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs = getSharedPreferences("user_data",Context.MODE_PRIVATE)
        val rememberMe = prefs.getBoolean("remember_me", false)
        if (rememberMe){
            val intent = Intent( this, screnShop::class.java)
            startActivity(intent)
            finish()
        }else{
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginUser())
                .commit()
        }
    }
}
