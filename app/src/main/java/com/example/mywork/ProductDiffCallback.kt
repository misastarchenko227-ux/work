package com.example.mywork

import androidx.recyclerview.widget.DiffUtil

class ProductDiffCallback: DiffUtil.ItemCallback<Product>() {
    // Проверяет — это один и тот же товар или нет?
    override fun areItemsTheSame(
        oldItem: Product,
        newItem: Product
    ): Boolean =
        oldItem.id == newItem.id


    override fun areContentsTheSame(
        oldItem: Product,
        newItem: Product
    ): Boolean =
        oldItem == newItem

}