package com.example.mywork

import android.util.Log
import com.example.mywork.RetrofitClient.json
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager



object RetrofitClient {

 private val contentType = "application/json".toMediaType()

 @OptIn(ExperimentalSerializationApi::class)
 private val json = Json {
  ignoreUnknownKeys = true
  encodeDefaults = true
  isLenient = true
 }

 private val logging = HttpLoggingInterceptor().apply {
  level = HttpLoggingInterceptor.Level.BODY
 }

 /**
  * ⚠️ Клиент, который доверяет всем сертификатам.
  * Использовать только для ТЕСТА (в продакшене нельзя!)
  */
 private fun getUnsafeOkHttpClient(): OkHttpClient {
  return try {
   val trustAllCerts = arrayOf<TrustManager>(
    object : X509TrustManager {
     override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
     override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
     override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    }
   )

   val sslContext = SSLContext.getInstance("SSL")
   sslContext.init(null, trustAllCerts, SecureRandom())
   val sslSocketFactory = sslContext.socketFactory

   OkHttpClient.Builder()
    .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
    .hostnameVerifier { _, _ -> true } // Игнорируем проверку хоста
    .addInterceptor(logging)
    .build()
  } catch (e: Exception) {
   throw RuntimeException(e)
  }
 }

 /**
  * ✅ Инициализация Retrofit API клиента
  */
 val api: APLRetrofit by lazy {
  Retrofit.Builder()
   .baseUrl("https://flask-server-1ajx.onrender.com/") // 🔹 Укажи свой URL или Render
   .addConverterFactory(json.asConverterFactory(contentType))
   .client(getUnsafeOkHttpClient())
   .build()
   .create()
 }
}
