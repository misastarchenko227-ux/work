package com.example.Shop

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Adapter.Adapter_shop
import com.example.Data.Product

import com.example.ViewModel.AddProductViewModel
import com.example.mywork.R

class ShopFragment : Fragment(R.layout.fragment_shop) {
    // Адаптер RecyclerView — отвечает за отображение списка товаров
    private lateinit var adapter: Adapter_shop
    // ViewModel — хранит список товаров и переживает повороты экрана
private val viewModel: AddProductViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // добовления RecyclerView
        initRecycler(view)
        //добовление товаров в RecyclerView
        setupResultListener()
    }

    private fun initRecycler(view: View) {
        // Передаём список товаров из ViewModel в адаптер
        adapter = Adapter_shop(viewModel.products){

            openProductDetails(it )
        }
        // Получаем RecyclerView из разметки
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv)
        // Сетка из 3 колонок
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        // Подключаем адаптер к RecyclerView
        recyclerView.adapter = adapter
    }

    private fun setupResultListener() {
        // Слушаем результат, отправленный из другого фрагмента
        parentFragmentManager.setFragmentResultListener(
            "new_product",
            viewLifecycleOwner
        ) { _, result ->
            // Достаём переданный товар из Bundle
            val product = result.getParcelable<Product>("product")
            // Если товар не null — добавляем его в список
            product?.let {
                addProduct(it)
            }
        }
    }

    private fun addProduct(product: Product) {
        // Добавляем новый товар в начало списка
        viewModel.products.add(0, product)
        // Сообщаем адаптеру, что добавлен новый элемент
        adapter.notifyItemInserted(0)
    }
    private fun openProductDetails(product: Product) {
        parentFragmentManager.setFragmentResult(
            "open_product",
            bundleOf("product" to product)

        )
        findNavController().navigate(R.id.infoProduct)

   }
}