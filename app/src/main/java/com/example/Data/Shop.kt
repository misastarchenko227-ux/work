package com.example.Data

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

data class Category(
    val id: Int,
    val name: String ,
    val Image:Int
)
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageUri: Uri,
    val name: String,
    val description: String?,
    val price: Int,
    val quantity: Int,
    val categoryName: String,
    val categoryImage: Int
)