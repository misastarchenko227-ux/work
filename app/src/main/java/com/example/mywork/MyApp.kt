package com.example.mywork

import android.app.Application
import android.util.Log

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        val init = RetrofitClient.api
        Log.d("RetrofitInit", "🔥 RetrofitClient прогрет при старте")
    }
}