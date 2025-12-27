package com.example.Shop.infoProduct

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.mywork.R
import androidx.fragment.app.Fragment
import com.example.Data.Product

class InfoProduct: Fragment(R.layout.fragment_info_product) {
    private lateinit var imageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var quantityTextView: TextView
    private lateinit var categoryTextView: TextView
    private lateinit var categoryImageView: ImageView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.InfoImage)
        nameTextView = view.findViewById(R.id.infoName)
        descriptionTextView = view.findViewById(R.id.infoDescription)
        priceTextView = view.findViewById(R.id.infoPrice)
        quantityTextView = view.findViewById(R.id.infoQuantity)
        categoryTextView = view.findViewById(R.id.infoCategoryText)
        categoryImageView = view.findViewById(R.id.infoCategoryPhoto)
        receiveProduct()
    }

    private fun receiveProduct() {
        parentFragmentManager.setFragmentResultListener(
            "open_product",
            viewLifecycleOwner
        ) { _, bundle ->
            val product = bundle.getParcelable<Product>("product")
            product?.let { showProduct(it) }
        }
    }
    private fun showProduct(product: Product) {
        imageView.setImageURI(product.imageUri)

        nameTextView.text = product.name
        descriptionTextView.text = product.description ?: "Описание отсутствует"
        priceTextView.text = "Цена: ${product.price} руб"
        quantityTextView.text = "В наличии: ${product.quantity}"

        categoryTextView.text = product.categoryName ?: "Без категории"

        product.categoryImage?.let {
            categoryImageView.setImageResource(it)
        }
    }
    }
