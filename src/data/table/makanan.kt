package com.example.data.table

import com.example.data.model.FoodModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object makanan : Table() {
    val makanan_id = varchar("makanan_id", 5)
    val pedagang_id = varchar("pedagang_id", 20)
    val harga = decimal("harga", 20,2)
    val stok = integer("stok")
    val nama = varchar("nama", 40)
    val kategori_id = varchar("kategori_id", 5)

    fun toMakananModel(row:ResultRow) =
        FoodModel(
            row[makanan_id],
            row[pedagang_id],
            row[harga].toDouble(),
            row[stok],
            row[nama],
            row[kategori_id],
            kategori_makanan.toKategoriModel(row)
        )
}