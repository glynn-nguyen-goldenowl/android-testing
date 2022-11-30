package com.example.androidtesting.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    val name: String,
    val amount : Int,
    val price: Int,
    val imageUrl: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)