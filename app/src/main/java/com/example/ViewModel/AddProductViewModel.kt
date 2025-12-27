package com.example.ViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.Data.Product

class AddProductViewModel : ViewModel() {
    // URI выбранного изображения товара (фото из галереи)
    var selectedImageUri: Uri? = null
    // Описание товара (может быть пустым)
var description: String? = null
    // Цена товара
    var price: Int? = null
    // Количество товара на складе
    var quantity: Int? = null
    // Название выбранной категории товара
    var selectedCategoryName: String? = null
    //  картинка выбранной категории
    var selectedCategoryImage: Int? = null
    // Список всех добавленных товаров (отображается в магазине)
    val products = mutableListOf<Product>()
}