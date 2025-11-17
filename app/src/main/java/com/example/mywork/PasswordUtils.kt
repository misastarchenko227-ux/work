package com.example.mywork

import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import android.util.Base64

object PasswordUtils {
    fun generateSalt(length: Int = 16): ByteArray { //Создаёт массив случайных байт длиной length (по умолчанию 16 байт).
        val salt = ByteArray(length)
        SecureRandom().nextBytes(salt) //Использует SecureRandom() — криптографически стойкий генератор случайных чисел.
        return salt //Возвращает эту «соль».
    }
// Хеширует пароль с помощью PBKDF2WithHmacSHA256
    fun hashPassword(
        password: String,
        salt: ByteArray = generateSalt(),//Генерируем соль, если не передали вручную
        iterations: Int = 1_000,// Количество итераций хеширования (чем больше, тем безопаснее, но медленнее)
        keyLength: Int = 256// Длина выходного хеша (в битах)
    ): String {
    // Создаём спецификацию ключа для PBKDF2:
    // - password.toCharArray() — преобразуем строку пароля в массив символов
    // - salt — соль, добавляемая для уникальности хеша
    // - iterations — число итераций алгоритма
    // - keyLength — длина результата (256 бит = 32 байта)
        val spec = PBEKeySpec(password.toCharArray(), salt, iterations, keyLength)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256") //Получаем фабрику для алгоритма PBKDF2 с HMAC-SHA256
        val hash = factory.generateSecret(spec).encoded  // Генерируем хеш по указанной спецификации (возвращает байты)
        val saltBase64 = Base64.encodeToString(salt, Base64.NO_WRAP)  // Кодируем соль в Base64, чтобы хранить как строку
        val hashBase64 = Base64.encodeToString(hash, Base64.NO_WRAP)  // Кодируем хеш в Base64 для хранения
    // Склеиваем все данные в одну строку через двоеточие:
    // "итерации:соль:хеш"
        return ("$iterations:$saltBase64:$hashBase64")
    }

    //  Проверяет, совпадает ли введённый пароль с сохранённым хешем
    fun verifyPassword(password: String, storedHash: String): Boolean {
        val parts = storedHash.split(":") // Разделяем сохранённую строку на части: итерации, соль и хеш
        if (parts.size != 3) return false  // Если формат неправильный (частей не 3) — возвращаем false
        val iterations = parts[0].toInt() // Извлекаем количество итераций
        val salt = Base64.decode(parts[1], Base64.NO_WRAP) // Декодируем соль из Base64 обратно в байты
        val hash = Base64.decode(parts[2], Base64.NO_WRAP) // Декодируем сохранённый хеш из Base64
        // Создаём спецификацию ключа для PBKDF2 с теми же параметрами,
        // чтобы пересчитать хеш для введённого пользователем пароля
        val spec = PBEKeySpec(password.toCharArray(), salt, iterations, hash.size * 8)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")  // Получаем фабрику для PBKDF2 с HMAC-SHA256
        val testHash = factory.generateSecret(spec).encoded  // Генерируем новый хеш из введённого пароля
        return hash.contentEquals(testHash) // Сравниваем новый хеш с сохранённым — если совпадает, пароль правильный
    }
}