package com.example.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Data.Category
import com.example.mywork.R

class Adapter_AddCategories(val categoryList: List<Category>, val onClick: (Category) -> Unit) :
    RecyclerView.Adapter<Adapter_AddCategories.CategoryViewHolder>() {
    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = itemView.findViewById(R.id.nameCategor)
        val photo: ImageView = itemView.findViewById(R.id.photoCategor1)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_add_categories, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CategoryViewHolder,
        position: Int
    ) {
        val category = categoryList[position]
        holder.name.text = category.name
        holder.photo.setImageResource(category.Image)
        holder.itemView.setOnClickListener {
            onClick(category)
        }
    }

    override fun getItemCount(): Int = categoryList.size

}