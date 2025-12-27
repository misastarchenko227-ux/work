package com.example.Shop

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.Data.Product
import com.example.ViewModel.AddProductViewModel
import com.example.mywork.R

class AddProduct : Fragment(R.layout.fragment_add_product) {

    private val viewModel: AddProductViewModel by viewModels()
    private lateinit var photoProduct: ImageView
    private lateinit var textAddPhoto: TextView
    private lateinit var addCategory: TextView
    private lateinit var photoCategory: ImageView
    private lateinit var saveAddProductsButton: Button
    private lateinit var nameProduct: EditText
    private lateinit var descriptionProduct: EditText
    private lateinit var priceProduct: EditText
    private lateinit var quantityProduct: EditText
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoProduct = view.findViewById(R.id.imageView)
        textAddPhoto = view.findViewById(R.id.textAddPhoto)
        addCategory = view.findViewById(R.id.add_categories)
        photoCategory = view.findViewById(R.id.PhotoCategor)
        saveAddProductsButton = view.findViewById(R.id.save_addProducts)
        nameProduct = view.findViewById(R.id.nameProduct)
        descriptionProduct = view.findViewById(R.id.descriptionProduct)
        priceProduct = view.findViewById(R.id.priceProduct)
        quantityProduct = view.findViewById(R.id.quantityProduct)


        // ---------- Image Picker ----------
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    viewModel.selectedImageUri = uri
                    photoProduct.setImageURI(uri)
                    photoProduct.visibility = View.VISIBLE
                    textAddPhoto.visibility = View.GONE
                }
            }

        textAddPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        photoProduct.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // ---------- Category navigation ----------
        addCategory.setOnClickListener {
            findNavController().navigate(R.id.AddCategories)
        }

        // ---------- Result from AddCategories ----------
        parentFragmentManager.setFragmentResultListener(
            "category_result",
            viewLifecycleOwner
        ) { _, result ->
            val name = result.getString("name")
            val image = result.getInt("image")

            viewModel.selectedCategoryName = name
            viewModel.selectedCategoryImage = image

            addCategory.text = name
            photoCategory.setImageResource(image)
        }

        // ---------- Restore UI state ----------
        restoreImage(photoProduct, textAddPhoto)
        restoreCategory(addCategory, photoCategory)

        saveAddProductsButton.setOnClickListener {
            val name = nameProduct.text.toString().trim()
            if (validateInput()) {
                saveToViewModel()
                finishAddingProduct()
            }


        }
    }

    private fun restoreImage(photoProduct: ImageView, textAddPhoto: TextView) {
        viewModel.selectedImageUri?.let { uri ->
            photoProduct.setImageURI(uri)
            photoProduct.visibility = View.VISIBLE
            textAddPhoto.visibility = View.GONE
        }
    }

    private fun restoreCategory(addCategory: TextView, photoCategory: ImageView) {
        viewModel.selectedCategoryName?.let {
            addCategory.text = it
        }

        viewModel.selectedCategoryImage?.let {
            photoCategory.setImageResource(it)
        }
    }

    private fun validateInput(): Boolean {
        // Название
        val name = nameProduct.text.toString().trim()
        if (name.isEmpty()) {
            nameProduct.error = "Введите название товара"
            nameProduct.requestFocus()
            return false
        }

        // Фото
        if (viewModel.selectedImageUri == null) {
            Toast.makeText(
                requireContext(),
                "добавьте фото товара",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        // Категория
        if (viewModel.selectedCategoryName == null) {
            Toast.makeText(
                requireContext(),
                "выберите категорию",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        // Цена
        val price = priceProduct.text.toString().trim().toIntOrNull()
        if (price == null || price <= 0) {
            priceProduct.error = "Введите корректную цену"
            priceProduct.requestFocus()
            return false
        }

        // Количество
        val quantity = quantityProduct.text.toString().trim().toIntOrNull()
        if (quantity == null || quantity <= 0) {
            quantityProduct.error = "Введите корректное количество"
            quantityProduct.requestFocus()
            return false
        }

        return true
    }

    private fun saveToViewModel() {
        viewModel.description = descriptionProduct.text.toString().trim()
        viewModel.price = priceProduct.text.toString().trim().toInt()
        viewModel.quantity = quantityProduct.text.toString().trim().toInt()
    }

    private fun  finishAddingProduct() {
        val product = Product(
            imageUri = viewModel.selectedImageUri!!,
            name = nameProduct.text.toString().trim(),
            description = viewModel.description,
            price = viewModel.price!!,
            quantity = viewModel.quantity!!,
            categoryName = viewModel.selectedCategoryName!!,
            categoryImage = viewModel.selectedCategoryImage!!
        )
        parentFragmentManager.setFragmentResult(
            "new_product",
            bundleOf("product" to product)
        )
        findNavController().popBackStack()
        Toast.makeText(
            requireContext(),
            "Товар успешно добавлен",
            Toast.LENGTH_LONG
        ).show()

    }

}