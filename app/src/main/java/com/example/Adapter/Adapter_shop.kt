package com.example.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Data.Product
import com.example.mywork.R

// Адаптер наследуется от ListAdapter — он автоматически обновляет список
// Product — модель товара
// ProductViewHolder — класс, который держит ссылки на элементы item_product
class Adapter_shop(
    private val shopList: List<Product>, private val onItemClick: (Product) -> Unit

    ) : RecyclerView.Adapter<Adapter_shop.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.productName)
        val price: TextView = view.findViewById(R.id.productPrice)
        val stock: TextView = view.findViewById(R.id.productStock)
        val image: ImageView = view.findViewById(R.id.productImage)
        val card: View = view.findViewById(R.id.productCard1)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        val product = shopList[position]

        holder.name.text = product.name
        holder.price.text = "${product.price} руб."
        holder.stock.text = "В наличии: ${product.quantity}"
        holder.card.setOnClickListener {
            onItemClick(product)
        }
        holder.image.setImageURI(product.imageUri)
    }

    override fun getItemCount(): Int = shopList.size
}