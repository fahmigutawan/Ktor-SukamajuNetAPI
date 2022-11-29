package com.example.data.table

import com.example.data.model.CategoryModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object kategori_makanan :Table(){
    val kategori_id = varchar("kategori_id", 5)
    val kategori_word = varchar("kategori_word", 20)

    fun toKategoriModel(row: ResultRow) = CategoryModel(
        row[kategori_id],
        row[kategori_word]
    )
}