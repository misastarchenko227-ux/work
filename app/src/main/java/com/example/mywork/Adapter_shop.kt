package com.example.mywork
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

// Адаптер наследуется от ListAdapter — он автоматически обновляет список
// Product — модель товара
// ProductViewHolder — класс, который держит ссылки на элементы item_product
class Adapter_shop:ListAdapter<Product,Adapter_shop.ProductViewHolder> (ProductDiffCallback()) {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<ImageView>(R.id.productImage)
        private val title = itemView.findViewById<TextView>(R.id.productName)
        private val price = itemView.findViewById<TextView>(R.id.productPrice)
        private val stock = itemView.findViewById<TextView>(R.id.productStock)

        // Метод, который заполняет элемент списка данными
        fun bind(product: Product) {
            // Загружаем картинку с помощью Glide
            Glide.with(itemView).load(product.imageUrl).into(image)
            title.text = product.title
            // Заполняем текстовые поля
            price.text = "$${product.price}"
            stock.text =  "В наличии: ${product.stock}"
            // Если товара нет — делаем элемент более прозрачным
            if (product.stock==0){
                itemView.alpha=0.4f

            }else{
                itemView.alpha=1f

            }
        }
    }
    // Создаём новый ViewHolder — это вызывается, когда появляется новый элемент списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }
    // Привязываем данные к ViewHolder
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}