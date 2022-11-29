package com.example.data.model

data class ComputerModel(
    val komputer_id:String,
    val kategori_id:String,
    val kategori:CategoryModel,
    val harga_perjam:String,
    val status:String
)
