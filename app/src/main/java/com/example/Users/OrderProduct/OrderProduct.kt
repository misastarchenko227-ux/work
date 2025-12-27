package com.example.Users.OrderProduct

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.Adapter.Adapter_shop
import com.example.Data.Product
import com.example.ViewModel.AddProductViewModel
import com.example.mywork.R

class OrderProduct : Fragment(R.layout.fragment_order_product) {
    private lateinit var adapter: Adapter_shop
    private val viewModel: AddProductViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler(view)
    }

    private fun initRecycler(view: View) {
        adapter = Adapter_shop(viewModel.products) { it ->
            onProductClick(it)
        }

    }
    private fun onProductClick(product: Product) {

    }
}
