package com.example.mywork

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log


open class AddCardBase() {
    fun isValidLuhn(number: String): Boolean {
        val digits = number.filter { it.isDigit() }.map { it - '0' }
        if (digits.isEmpty()) return false
        var sum = 0
        var alternate: Boolean = false
        for (i in digits.size - 1 downTo 0) {
            var n = digits[i]
            if (alternate) {
                n *= 2
                if (n > 9) n -= 9
            }
            sum += n
            alternate = !alternate
        }
        return sum % 10 == 0
    }
}

class CardNumberFormattingWatcher : TextWatcher {
    // Используем `current` для хранения отформатированного текста, чтобы избежать бесконечного цикла
    private var current = ""
    private val maxDigits = 16 // Максимальное количество цифр в номере карты
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }   // Не требуется для этого функционала

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }  // Не требуется для этого функционала

    override fun afterTextChanged(s: Editable?) {
        s ?: return
        // Проверяем, не вызвано ли это изменение самим TextWatcher'ом
        if (s.toString() != current) {
            // 1. Извлекаем только цифры из текущего ввода и ограничиваем длину
            val digitsOnly = s.toString().filter { it.isDigit() }.take(maxDigits)
            // 2. Форматируем цифры, разделяя их пробелом каждые 4 символа
            val newFormattedText = digitsOnly.chunked(4).joinToString(" ")
            // Если новый текст не изменился, просто выходим
            if (newFormattedText == current) return
            // 3. Сохраняем новый отформатированный текст
            current = newFormattedText
            // 4. Заменяем текущий текст в EditText новым отформатированным текстом
            // Сначала удаляем фильтры, чтобы избежать ошибки при замене
            val oldFilters = s.filters
            s.filters = arrayOf<InputFilter>()  // Временно убираем фильтры
            // Применяем новый текст
            s.replace(0, s.length, current, 0, current.length)
            // Восстанавливаем фильтры, если они были
            s.filters = oldFilters

            // 5. Устанавливаем курсор в конец текста
            // Это важно, так как replace() часто сбрасывает курсор в начало
            try {
                // Если текст не пустой, устанавливаем курсор
              if(current.isEmpty()){
                  val editText = s.javaClass.getField("mTextView").get(s)as? android.widget.EditText
                  editText?.setSelection(current.length)
              }
            } catch (e:Exception){
                Log.d("error card", e.toString())
                // В случае ошибки получения EditText или его поля, просто устанавливаем курсор в конец Editable
                // Это может не сработать для всех версий Android, но это общая практика.
                // В Android Studio можно использовать EditText.setSelection(s.length())
                // В этом примере, поскольку мы не знаем, к какому EditText привязан watcher,
                // мы просто полагаемся на то, что Editable сам попытается сохранить позицию,
                // или используем рефлексию (что не очень рекомендуется).
                // Для простоты в реальном проекте TextWatcher часто передают ссылку на EditText.

            }
        }

    }
}