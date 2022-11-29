package com.example.data.model

data class FoodModel(
    val makanan_id: String,
    val pedagang_id: String,
    val harga: Double,
    val stok: Int,
    val nama: String,
    val kategori_id: String,
    val kategori: CategoryModel
)
