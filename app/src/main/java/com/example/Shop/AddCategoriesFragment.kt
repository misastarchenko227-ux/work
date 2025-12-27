package com.example.Shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Adapter.Adapter_AddCategories
import com.example.Data.Category
import com.example.mywork.R

class AddCategories : Fragment(R.layout.fragment_add_categories) {

    private lateinit var adapter: Adapter_AddCategories

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addCategories = listOf(
            Category(1, "куханные приборы", R.drawable.kitchen),
            Category(2, "обувь", R.drawable.shoes),
            Category(3, "верхняя одежда", R.drawable.outerwear),
            Category(4, "нижная одежда", R.drawable.undergarments),
            Category(5, "быт. техника", R.drawable.technic),
            Category(6, "мебель", R.drawable.furniture),
            Category(7, "дет. игрушки", R.drawable.toys)
        )

        adapter = Adapter_AddCategories(addCategories) { category ->
            parentFragmentManager.setFragmentResult(
                "category_result",
                bundleOf(
                    "name" to category.name,
                    "image" to category.Image
                )
            )
            findNavController().popBackStack()
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.RV_AddCategories)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
}
